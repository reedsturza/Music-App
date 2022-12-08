package edu.moravian.csci215.AndroidMusicPlayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Update
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

/**
 * View model for a list of playlists
 */
class PlaylistViewModel : ViewModel() {
    /** The repository we are using to perform queries */
    private val repo = PlaylistRepository.get()

    /** The internal, mutable, list of events (as a flow) */
    private val _playlists: MutableStateFlow<List<Playlist>> = MutableStateFlow(emptyList())
    /** the external list of songs (as a flow) */
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()

    /** The current coroutine job */
    private var job: Job? = null

    /**
     * function to get the playlist by Id
     * @param playlistId
     */
    suspend fun getPlaylistById(playlistId: UUID) { repo.getPlaylistId(playlistId) }

    /**
     * function to insert a playlist into the datsbase
     * @param playlist playlist to insert
     */
    suspend fun insertPlaylist(playlist: Playlist) { repo.insertPlaylist(playlist) }

    /**
     * remove a playlist from the database
     * @param playlist playlist to remove
     */
    suspend fun removePlaylist(playlist: Playlist) { repo.removePlaylist(playlist) }

    /** When this object is created, start collecting the items from the database */
    init {
        viewModelScope.launch {
            repo.allPlaylists().collect() {
                _playlists.value = it
            }
        }
    }
}