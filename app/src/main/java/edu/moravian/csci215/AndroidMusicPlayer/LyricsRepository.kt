package edu.moravian.csci215.AndroidMusicPlayer

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

private const val DATABASE_NAME = "lyrics-database"
class LyricsRepository private constructor(context: Context) {
    private val coroutineScope: CoroutineScope = GlobalScope

    /**
     * Room.databaseBuilder() creates a concrete implementation of the abstract MusicDatabase
     * @param context.applicationContext to access the filesystem
     * @param NodeDatabase::class.java database room creates
     * @param DATABASE_NAME name of the database
     */
    private val database: LyricsDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            LyricsDatabase::class.java,
            DATABASE_NAME
        )
        // .createFromAsset(DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    /**
     * function for the getLyricsById function in HierarchyDAO
     * So other components can preform any operations they need on the database
     * @param lyricsId
     */
    suspend fun getLyricsById(lyricsId: UUID): Lyrics = database.lyricsDao().getLyricsById(lyricsId)

    companion object {
        private var INSTANCE: LyricsRepository? = null

        /**
         * Initializes a new instance of the repository
         * @param context
         */
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                println(context)
                INSTANCE = LyricsRepository(context)
                println(INSTANCE)
                println(INSTANCE?.database)
            }
        }

        /**
         * getter function that throws an exception if the initialize() wasn't called before it
         * @return repository returns a non null repository instance
         */
        fun get(): LyricsRepository {
            return INSTANCE ?: throw IllegalStateException("LyricsRepository must be initialized")
        }
    }
}