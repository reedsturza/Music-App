package edu.moravian.csci215.finalproject

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    /** The current coroutine job */
    private var job: Job? = null
}