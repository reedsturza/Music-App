package edu.moravian.csci215.AndroidMusicPlayer

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

private const val DATABASE_NAME = "playlist-database"
class PlaylistRepository private constructor(context: Context) {
    private val coroutineScope: CoroutineScope = GlobalScope

    /**
     * Room.databaseBuilder() creates a concrete implementation of the abstract MusicDatabase
     * @param context.applicationContext to access the filesystem
     * @param NodeDatabase::class.java database room creates
     * @param DATABASE_NAME name of the database
     */
    private val database: PlaylistDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            PlaylistDatabase::class.java,
            DATABASE_NAME
        )
        .createFromAsset(DATABASE_NAME)
        .build()

    /**
     * fun for all playlists in the HierarchyDAO
     * So other components can preform any operations they need on the database
     */
    fun allPlaylists(): Flow<List<Playlist>> = database.playlistDao().allPlaylists

    /**
     * function for the getPlaylistId function in HierarchyDAO
     * So other component can preform any operations they need on the database
     * @param playlistId
     */
    suspend fun getPlaylistId(playlistId: UUID): Playlist = database.playlistDao().getPlaylistFromId(playlistId)

    /**
     * insert a playlist to the database
     * @param playlist the playlist being inserted
     */
    suspend fun insertPlaylist(playlist: Playlist) = database.playlistDao().insertPlaylist(playlist)

    /**
     * remove playlist from the database
     * @param playlist playlist being removed
     */
    suspend fun removePlaylist(playlist: Playlist) = database.playlistDao().removePlaylist(playlist)

    /**
     * function for the updateEvent function in HierarchyDOA
     * So other components can preform any operations they need on the database
     * @param event
     */
    fun updatePlaylist(playlist: Playlist) {
        coroutineScope.launch {
            database.playlistDao().updatePlaylist(playlist)
        }
    }

    companion object {
        private var INSTANCE: PlaylistRepository? = null

        /**
         * Initializes a new instance of the repository
         * @param context
         */
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                println(context)
                INSTANCE = PlaylistRepository(context)
                println(INSTANCE)
                println(INSTANCE?.database)
            }
        }

        /**
         * getter function that throws an exception if the initialize() wasn't called before it
         * @return repository returns a non null repository instance
         */
        fun get(): PlaylistRepository {
            return INSTANCE ?: throw IllegalStateException("PlaylistRepository must be initialized")
        }
    }
}