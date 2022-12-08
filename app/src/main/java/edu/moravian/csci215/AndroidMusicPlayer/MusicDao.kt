package edu.moravian.csci215.AndroidMusicPlayer

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * The data access object for performing queries involving songs
 */
@Dao
interface MusicDao {
    /**
     * @return flow of all songs in the MusicDatabase
     */
    @get:Query("SELECT * FROM song")
    val allSongs: Flow<List<Song>>

    /**
     * Get a song from its ID.
     * @param songId
     * @return String songName
     */
    @Query("SELECT * FROM song WHERE songId=(:songId) LIMIT 1")
    suspend fun getSongById(songId: UUID): Song

    /**
     * Insert a song into the database
     * @param song the song being inserted
     */
    @Insert
    suspend fun insertSong(song: Song)
}