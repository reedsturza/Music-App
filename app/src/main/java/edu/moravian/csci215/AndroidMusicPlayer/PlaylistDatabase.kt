package edu.moravian.csci215.AndroidMusicPlayer

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Database class which stores playlist names within the database
 */
@Database(entities = [ Playlist::class ], version=1)
abstract class PlaylistDatabase : RoomDatabase() {
    /**
     * Abstract function which hooks the DOA up to the database
     */
    abstract fun playlistDao() : PlaylistDao
}