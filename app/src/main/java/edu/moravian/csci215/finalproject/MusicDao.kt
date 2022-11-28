package edu.moravian.csci215.finalproject

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * The data access object for performing queries involving song and lyrics.
 */
@Dao
interface MusicDao {
    /**
     * @return flow of all events in the MusicDatabase
     */
    @get:Query("SELECT * FROM song")
    val allSongs: Flow<List<Song>>

    /**
     * Get a song from its ID.
     * @param id the
     * @return single song
     */
    @Query("SELECT * FROM song WHERE id=(:id) LIMIT 1")
    suspend fun getSongById(id: UUID): Song

    /**
     * Add a song to a playlist in the database.
     * @param song the song to add
     */
    @Insert
    suspend fun addSong(song: Song)

    /**
     * Update a playlist in the database when a song is added or removed.
     * @param song the song to update
     */
    @Update
    suspend fun updatePlaylist(song: Song)

    /**
     * Remove a song from a playlist in the database.
     * @param song the song to remove
     */
    @Delete
    suspend fun removeSong(song: Song)

    /**
     * Remove a song from a playlist in the database by its ID.
     * @param id the id of the song to remove
     */
    @Query("DELETE FROM song WHERE id = (:id)")
    suspend fun removeSongById(id: UUID)
}