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
class MusicListViewModel : ViewModel() {
    /** The repository we are using to perform queries */
    private val repo = MusicRepository.get()

    /** The internal, mutable, list of events (as a flow) */
    private val _songs: MutableStateFlow<List<Song>> = MutableStateFlow(emptyList())
    /** the external list of songs (as a flow) */
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    /**
     * function to insert the song into the databse
     * @param songName
     * @param artist
     */
    suspend fun insertSong(song: Song) { repo.insertSong(song) }

    /** When this object is created, start collecting the items from the database */
    init {
        viewModelScope.launch {
            repo.allSongs().collect() {
                _songs.value = it
            }
        }
    }

}