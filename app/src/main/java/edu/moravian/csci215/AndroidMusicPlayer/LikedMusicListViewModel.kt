package edu.moravian.csci215.AndroidMusicPlayer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

/**
 * View model for a list of music
 */
class LikedMusicListViewModel : ViewModel() {
    /** The repository we are using to perform queries */
    private val repo = MusicRepository.get()

    /** The internal, mutable, list of events (as a flow) */
    private val _likedSongs: MutableStateFlow<List<Song>> = MutableStateFlow(emptyList())
    /** the external list of songs (as a flow) */
    val likedSongs: StateFlow<List<Song>> = _likedSongs.asStateFlow()

    /** When this object is created, start collecting the items from the database */
    init {
        viewModelScope.launch {
            // uses the liked songs function to collect all the liked songs
            repo.allLikedSongs(true).collect() {
                _likedSongs.value = it
            }
        }
    }
}