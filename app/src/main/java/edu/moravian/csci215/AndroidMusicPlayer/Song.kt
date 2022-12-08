package edu.moravian.csci215.AndroidMusicPlayer

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * A song object contains all the information about a certain song
 */
@Entity
data class Song(
    /** The id of the song is the primary key in the database. */
    @PrimaryKey
    val songId: UUID = UUID.randomUUID(),

    /** song name is for the name of the song **/
    val songName: String = "",

    /** the artist that released the song */
    val artist: String = ""
)