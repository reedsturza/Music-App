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
     * @param song
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
//                    INSTANCE?.insertSong(Song(songId = UUID.fromString("ccc726c1-1d0d-48f0-b0d3-826eaa81f61b"),songName = "monkey", artist = "artist!", rawPath = R.raw.monkey))
//                    INSTANCE?.insertSong(Song(songId = UUID.fromString("7c43c391-fd7d-48b8-92a1-1cb327498632"),songName = "Sweet Emotion", artist = "Aerosmith", rawPath = R.raw.sweet_emotion, liked = true))
//                    INSTANCE?.insertSong(Song(songId = UUID.fromString("cf5ecc77-b8ca-4bbd-a4c6-6ece4af287ab"),songName= "Aint No Rest For The Wicked", artist = "Cage The Elephant", rawPath = R.raw.aint_no_rest_for_the_wicked))
//                    INSTANCE?.insertSong(Song(songId = UUID.fromString("6f44e776-a740-4422-be96-6e61e639c1dc"), songName = "Don't Stop Believing", artist = "Journey", rawPath = R.raw.dont_stop_believin))
//                    INSTANCE?.insertSong(Song(songId = UUID.fromString("02f0d4f8-56e2-4c2a-bfed-e73328aa1559"), songName = "Pursuit of Happiness", artist = "Kid Cudi", rawPath = R.raw.pursuit_of_happiness))
//                    INSTANCE?.insertSong(Song(songId = UUID.fromString("b5a663b5-8aef-4658-91e0-7890c0b0a8c4"), songName = "Pursuit of Happiness (Steve Aoki Remix)", artist = "Kid Cudi AUDIO", rawPath = R.raw.pursuit_of_happiness_steve_aoki_remix))
//                    INSTANCE?.insertSong(Song(songId = UUID.fromString("9fa5186d-97dd-4e6a-9f86-857c284027b4"), songName = "Return of the Mack", artist = "Mark Morrison", rawPath = R.raw.return_of_the_mack))
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