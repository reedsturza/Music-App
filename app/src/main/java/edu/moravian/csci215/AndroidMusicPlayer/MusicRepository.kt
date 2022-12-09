package edu.moravian.csci215.AndroidMusicPlayer

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
        // .createFromAsset(DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    /**
     * fun for the allSongs in HierarchyDAO
     * So other components can preform any operations they need on the database
     */
    fun allSongs(): Flow<List<Song>> = database.musicDao().allSongs

    /**
     * function to get all the liked songs in the database
     */
    suspend fun allLikedSongs(liked: Boolean): Flow<List<Song>> = database.musicDao().allLikedSongs(liked)

    /**
     * update the song, mostly for the like song feature
     */
    fun updateSong(song: Song) {
        coroutineScope.launch {
            database.musicDao().updateSong(song)
        }
    }
    /**
     * function for the getSongById function in HierarchyDAO
     * So other components can preform any operations they need on the database
     * @param songId
     */
    suspend fun getSongById(songId: UUID): Song = database.musicDao().getSongById(songId)

    /**
     * function to insert a song into the database
     * @param songName
     */
    suspend fun insertSong(song: Song) = database.musicDao().insertSong(song)

    companion object {
        private var INSTANCE: MusicRepository? = null

        /**
         * Initializes a new instance of the repository
         * @param context
         */
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                println(context)
                INSTANCE = MusicRepository(context)
                println(INSTANCE)
                println(INSTANCE?.database)

//                GlobalScope.launch {
//                    INSTANCE?.insertSong(Song(songName = "monkey", artist = "artist!", rawPath = R.raw.monkey))
//                    INSTANCE?.insertSong(Song(songName = "MOnke", artist = "ada", rawPath = R.raw.monkey, liked = true))
//                }
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