package edu.moravian.csci215.AndroidMusicPlayer

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

/**
 * Application class to do work as soon as ready.
 * called by the system when your application is first loaded into memory
 */
class MusicApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // initialize the repositories
        MusicRepository.initialize(this)
        PlaylistRepository.initialize(this)

        val playerIntent = Intent(this, MediaPlayerService::class.java)
        startService(playerIntent)
        bindService(playerIntent, serviceConnection, BIND_AUTO_CREATE)
    }

    public var mediaPlayerService: MediaPlayerService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder: MediaPlayerService.LocalBinder = service as MediaPlayerService.LocalBinder
            mediaPlayerService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mediaPlayerService = null
        }
    }
}