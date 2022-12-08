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

class CreatePlaylistViewModel(private val playlistId: UUID) : ViewModel() {
    private val repo = PlaylistRepository.get()

    // The crime (internal, as a mutable state flow).
    private val _playlist: MutableStateFlow<Playlist?> = MutableStateFlow(null) //could be Event()
    // The crime (external, as a state flow).
    val playlist: StateFlow<Playlist?> = _playlist.asStateFlow()
    // gets the event from the database when the view model is initialized
    init {
        viewModelScope.launch {
            _playlist.value = repo.getPlaylistId(playlistId)
        }
    }

    /**
     * Update the event in this view model.
     * @param onUpdate a function or lambda that takes an event (the old event)
     *                 and returns a new event instance to replace it.
     */
    fun updatePlaylist(onUpdate: (Playlist) -> Playlist) {
        _playlist.update { oldPlaylist ->
            oldPlaylist?.let { onUpdate(it) }
        }
    }

    /**
     * Updates the event in the database when the view model is done
     */
    override fun onCleared() {
        super.onCleared()
        playlist.value?.let { repo.updatePlaylist(it) }
    }

    /**
     * EventViewModelFactory is responsible to create the instance of EventViewModel
     * @param eventId
     */
    class CreatePlaylistViewModelFactory(private val playlistId: UUID): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreatePlaylistViewModel(playlistId) as T
        }
    }
}