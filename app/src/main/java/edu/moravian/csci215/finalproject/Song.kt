package edu.moravian.csci215.finalproject

import android.media.MediaPlayer
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
    var id: UUID = UUID.randomUUID(),

    /** song name is for the name of the song **/
    var songName: String = "",

    /** media player for each individual song **/
    var mediaPlayer: MediaPlayer = MediaPlayer(),

    /** play list name for the playlist the song is in **/
    var playListName: String = ""
)