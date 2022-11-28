package edu.moravian.csci215.finalproject

import android.app.Application

/**
 * Application class to do work as soon as ready.
 * called by the system when your application is first loaded into memory
 */
class MusicApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MusicRepository.initialize(this)
    }
}