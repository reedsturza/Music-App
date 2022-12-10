package edu.moravian.csci215.AndroidMusicPlayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

/**
 * View model for a single song. Must be constructed with the factory
 * providing an song ID.
 */
class LyricsViewModel(private val lyricsId: UUID) : ViewModel() {
    /** the repository that we are using to preform queries */
    private val repo = LyricsRepository.get()

    // the lyrics (internal as mutable state flow)
    private val _lyrics: MutableStateFlow<Lyrics?> = MutableStateFlow(null)

    // the lyrics (external, as a state flow)
    val lyrics: StateFlow<Lyrics?> = _lyrics.asStateFlow()

    // gets the lyrics from the database when the view model is initialized
    init {
        viewModelScope.launch {
            _lyrics.value = repo.getLyricsById(lyricsId)
        }
    }
}

/**
 * LyricsViewModelFactory is responsible for creating the instance of the LyricsViewModel
 * @param lyricsId
 */
class LyricsViewModelFactory(private val lyricsId: UUID): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return LyricsViewModel(lyricsId) as T
    }
}