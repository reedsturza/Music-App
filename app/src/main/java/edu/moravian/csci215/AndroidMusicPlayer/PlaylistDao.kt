package edu.moravian.csci215.AndroidMusicPlayer

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * The data access object for performing queries involving playlists
 */
@Dao
interface PlaylistDao {
    /**
     * @return flow of all songs in the MusicDatabase
     */
    @get:Query("SELECT * FROM playlist")
    val allPlaylists: Flow<List<Playlist>>

    /**
     * Get a song from its ID.
     * @param playlistId
     * @return single playlist
     */
    @Query("SELECT * FROM playlist WHERE playlistId=(:playlistId) LIMIT 1")
    suspend fun getPlaylistFromId(playlistId: UUID): Playlist

    /**
     * Insert a playlist into the database
     * @param playlist benig the playlist added
     */
    @Insert
    suspend fun insertPlaylist(playlist: Playlist)

    /**
     * Remove a playlist from the database.
     * @param playlist playlist being removed
     */
    @Delete
    suspend fun removePlaylist(playlist: Playlist)

    /**
     * Updates the playlist
     * @param playlist
     */
    @Update
    suspend fun updatePlaylist(playlist: Playlist)
}