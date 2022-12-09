package edu.moravian.csci215.AndroidMusicPlayer

import androidx.room.Dao
import androidx.room.Query
import java.util.*

@Dao
interface LyricsDao {
    /**
     * Get a lyrics from its ID.
     * @param lyricsId
     * @return Lyrics entity
     */
    @Query("SELECT * FROM lyrics WHERE lyricsId=(:lyricsId) LIMIT 1")
    suspend fun getLyricsById(lyricsId: UUID): Lyrics
}