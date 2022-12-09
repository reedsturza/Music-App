package edu.moravian.csci215.AndroidMusicPlayer

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Database class which stores song entities within the database
 */
@Database(entities = [ Lyrics::class ], version=1)
abstract class LyricsDatabase : RoomDatabase() {
    /**
     * Abstract function which hooks the DOA up to the database
     */
    abstract fun lyricsDao() : LyricsDao
}