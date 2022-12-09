package edu.moravian.csci215.AndroidMusicPlayer

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * A lyrics entity for the song that is being played
 */
@Entity
data class Lyrics(
    /** The primary key and id for the song's lyrics (will be the same id as the songId) */
    @PrimaryKey
    val lyricsId: UUID = UUID.randomUUID(),

    /** the lyrics for the song that is being played */
    val lyrics: String = ""
)