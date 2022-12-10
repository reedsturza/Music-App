package edu.moravian.csci215.AndroidMusicPlayer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

/**
 * View model for a single song. Must be constructed with the factory
 * providing an song ID.
 */
class PlaySongViewModel(private val songId: UUID) : ViewModel() {
    /** the repository that we are using to preform queries */
    private val repo = MusicRepository.get()
    private val preferencesRepository = PreferencesRepository.get()

    // the song (internal as mutable state flow)
    private val _song: MutableStateFlow<Song?> = MutableStateFlow(null)

    // the song (external, as a state flow)
    val song: StateFlow<Song?> = _song.asStateFlow()

    // gets the song from the database when the view model is initialized
    init {
        viewModelScope.launch {
            // shared prefs to save what song was being played
            preferencesRepository.storedQuery.collectLatest {
                try {
                    val s = repo.getSongById(songId)
                    _song.value = s
                } catch (ex: Exception) {
                    Log.e("Shared Prefs", "Failed to fetch songID", ex)
                }
            }
        }
    }

    /**
     * Update the song in this view model
     * @param onUpdate a function or lambda that takes an song (the old song)
     *                  and returns a new song instance to replace it
     */
    fun updateSong(onUpdate: (Song) -> Song) {
        _song.update { oldSong ->
            oldSong?.let { onUpdate(it) }
        }
    }

    /**
     * Updates the event in the database when the view model is done
     */
    override fun onCleared() {
        super.onCleared()
        song.value?.let { repo.updateSong(it) }
    }

    fun setQuery(query: String) {
        viewModelScope.launch {
            preferencesRepository.setStoredQuery(query)
        }
    }
}

/**
 * PlaySongViewModelFactory is responsible for creating the instance of the PlaySongViewModel
 * @param songId
 */
class PlaySongViewModelFactory(private val songId: UUID): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return PlaySongViewModel(songId) as T
    }
}