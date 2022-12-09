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

    /** the artist the song was released by */
    val artist: String = "",

    /** the raw path for the music to play */
    val rawPath: String = "",

    /** whether a the song is liked or not */
    val liked: Boolean = false
)