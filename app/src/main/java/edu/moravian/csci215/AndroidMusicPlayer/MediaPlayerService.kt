package edu.moravian.csci215.AndroidMusicPlayer

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RawRes
import java.io.File
import java.io.IOException

/** This is the music resource file that will be played. */
@RawRes const val MUSIC_RESOURCE = R.raw.monkey

/**
 * A service to play a music in the background across all activities.
 */
class MediaPlayerService : Service(), AudioManager.OnAudioFocusChangeListener {
    /** The media player object  */
    private var mediaPlayer: MediaPlayer? = null

    fun play() { mediaPlayer?.apply { if (!isPlaying) start() } }
    fun pause() { mediaPlayer?.apply { if (isPlaying) pause() } }
    fun stop() { mediaPlayer?.apply { if (isPlaying) stop() } }

    /**
     * Sets the volume of the music.
     * @param volume a value from 0 to 1 for the volume, linearly scaled
     */
    fun setVolume(volume: Float) { mediaPlayer?.apply { setVolume(volume, volume) } }

    /** Create the media player */
    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(applicationContext, MUSIC_RESOURCE).apply {
            isLooping = true
        }
    }

    /** Release and destroy the media player */
    private fun releaseMediaPlayer() {
        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null
    }

    /** When the service is started, initialize the media player and request audio focus */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (!requestAudioFocus()) { stopSelf() }
        initMediaPlayer()
        return super.onStartCommand(intent, flags, startId)
    }

    /** When the service is done, release the media player and remove audio focus */
    override fun onDestroy() {
        super.onDestroy()
        releaseMediaPlayer()
        removeAudioFocus()
    }

    /**
     * Sets the audio being played from a resource ID. This re-uses the current
     * media player object.
     * @param resourceId the resource audio for the audio, like R.raw.monkey.
     */
    private fun setAudioResource(musicFile: String) {
        val afd = File(applicationContext.filesDir, musicFile)
        try {
            mediaPlayer?.apply {
                reset()
                setDataSource(afd.absolutePath)
                prepare()
            }
        } catch (ex: IOException) {
            Log.e("MainActivity", "set audio resource failed", ex)
        }
    }

    ////////// Support audio focus management //////////
    private lateinit var audioManager: AudioManager
    private lateinit var audioFocusRequest: AudioFocusRequest

    /** The attributes for the audio we will be playing */
    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

    override fun onAudioFocusChange(focusState: Int) {
        when (focusState) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                mediaPlayer ?: initMediaPlayer()
                play()
                setVolume(1.0f)
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pause()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> setVolume(0.1f)
            AudioManager.AUDIOFOCUS_LOSS -> releaseMediaPlayer()
        }
    }

    private fun requestAudioFocus(): Boolean {
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setWillPauseWhenDucked(true)
                .setOnAudioFocusChangeListener(this)  // TODO: , handler
                .build()
            audioManager.requestAudioFocus(audioFocusRequest)
        } else {
            audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        }
    }

    private fun removeAudioFocus(): Boolean {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(audioFocusRequest)
        } else {
            audioManager.abandonAudioFocus(this)
        }
    }

    ////////// Support binding this service and an activity - required to have service and activity interact //////////
    inner class LocalBinder : Binder() {
        fun getService(): MediaPlayerService = this@MediaPlayerService
    }
    private val binder: IBinder = LocalBinder()
    override fun onBind(intent: Intent?): IBinder = binder
}
