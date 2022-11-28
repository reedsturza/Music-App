package edu.moravian.csci215.finalproject

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

private const val DATABASE_NAME = "music-database"
class MusicRepository private constructor(context: Context) {
    private val coroutineScope: CoroutineScope = GlobalScope

    /**
     * Room.databaseBuilder() creates a concrete implementation of the abstract MusicDatabase
     * @param context.applicationContext to access the filesystem
     * @param NodeDatabase::class.java database room creates
     * @param DATABASE_NAME name of the database
     */
    private val database: MusicDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            MusicDatabase::class.java,
            DATABASE_NAME
        )
        .createFromAsset(DATABASE_NAME)
        .build()

    /**
     * val for the allSongs in HierarchyDOA
     * So other components can preform any operations they need on the database
     */
    val allEvents: Flow<List<Song>> = database.musicDao().allSongs

    /**
     * function for the getSongById function in HierarchyDOA
     * So other components can preform any operations they need on the database
     * @param id
     * @return Song
     */
    suspend fun getSongById(id: UUID): Song = database.musicDao().getSongById(id)

    /**
     * function for the addSong function in HierarchyDOA
     * So other components can preform any operations they need on the database
     * @param song
     */
    suspend fun addEvent(song: Song) = database.musicDao().addSong(song)

    /**
     * function for the updatePlaylist function in HierarchyDOA
     * So other components can preform any operations they need on the database
     * @param song
     */
    fun updatePlaylist(song: Song) {
        coroutineScope.launch {
            database.musicDao().updatePlaylist(song)
        }
    }

    /**
     * function for the removeSong function in HierarchyDOA
     * So other components can preform any operations they need on the database
     * @param song
     */
    suspend fun removeEvent(song: Song) = database.musicDao().removeSong(song)

    /**
     * function for the removeSongById function in HierarchyDOA
     * So other components can preform any operations they need on the database
     * @param id
     */
    suspend fun removeSongById(id: UUID) = database.musicDao().removeSongById(id)

    companion object {
        private var INSTANCE: MusicRepository? = null

        /**
         * Initializes a new instance of the repository
         * @param context
         */
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = MusicRepository(context)
            }
        }

        /**
         * getter function that throws an exception if the initialize() wasn't called before it
         * @return repository returns a non null repository instance
         */
        fun get(): MusicRepository {
            return INSTANCE ?: throw IllegalStateException("MusicRepository must be initialized")
        }
    }
}