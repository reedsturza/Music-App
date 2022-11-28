package edu.moravian.csci215.finalproject

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Database class which stores song and lyrics entities within the database
 */
@Database(entities = [ Song::class, Lyrics::class ], version=1)
abstract class MusicDatabase : RoomDatabase() {
    /**
     * Abstract function which hooks the DOA up to the database
     */
    abstract fun musicDao() : MusicDao
}