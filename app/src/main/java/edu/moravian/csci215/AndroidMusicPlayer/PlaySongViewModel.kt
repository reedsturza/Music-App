package edu.moravian.csci215.AndroidMusicPlayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

/**
 * View model for a single song. Must be constructed with the factory
 * providing an song ID.
 */
class PlaySongViewModel(private val songId: UUID) : ViewModel() {
    /** the repository that we are using to preform queries */
    private val repo = MusicRepository.get()

    // the song (internal as mutable state flow)
    private val _song: MutableStateFlow<Song?> = MutableStateFlow(null)

    // the song (external, as a state flow)
    val song: StateFlow<Song?> = _song.asStateFlow()

    // gets the song from the database when the view model is initialized
    init {
        viewModelScope.launch {
            _song.value = repo.getSongById(songId)
        }
    }
}

    /**
     * PlaySongViewModelFacotry is responsible for creating the instance of the PlaySongViewModel
     * @param songId
     */
    class PlaySongViewModelFactory(private val songId: UUID): ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            return PlaySongViewModel(songId) as T
        }
    }