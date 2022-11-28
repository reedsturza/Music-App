package edu.moravian.csci215.finalproject

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * A Lyrics object contains all the lyrics for each song in the database
 */
@Entity
data class Lyrics(
    /** The primary key for each song and its lyrics **/
    @PrimaryKey
    var id: UUID = TODO(), // link the two tables somehow

    /** A String that contains the songs lyrics **/
    var lyrics: String = ""
)