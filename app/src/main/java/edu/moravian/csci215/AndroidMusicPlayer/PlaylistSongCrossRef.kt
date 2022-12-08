package edu.moravian.csci215.AndroidMusicPlayer

import androidx.room.Entity
import java.util.*

@Entity(primaryKeys = ["playlistId", "songId"])
data class PlaylistSongCrossRef (
    val playlistId: UUID,
    val songId: UUID
)