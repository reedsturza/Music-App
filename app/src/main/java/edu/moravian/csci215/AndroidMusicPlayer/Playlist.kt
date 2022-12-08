package edu.moravian.csci215.AndroidMusicPlayer

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * A Lyrics object contains all the lyrics for each song in the database
 */
@Entity
data class Playlist(
    /** The primary key for each song and its lyrics **/
    @PrimaryKey
    var playlistId: UUID = UUID.randomUUID(), // link the two tables somehow

    /** A String that contains the songs lyrics **/
    var playlistName: String = "",
)