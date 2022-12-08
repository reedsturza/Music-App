package edu.moravian.csci215.AndroidMusicPlayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // In a fragment: (requireActivity() as MainActivity).musicPlayer.play()

    val musicPlayer: MediaPlayerService
        get() = (application as MusicApplication).mediaPlayerService!!
}
