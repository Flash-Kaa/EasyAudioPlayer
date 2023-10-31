package com.example.easyaudioplayer

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.File
import java.net.URI

class AudioPlayerViewModel: ViewModel() {
    val music = mutableStateOf<MediaPlayer?>(null)
    var volume by mutableStateOf(1f)
        private set

    fun setLoop(value: Boolean) {
        music.value?.isLooping = value
    }

    fun updateVolume(newVolume: Float) {
        music.value?.setVolume(newVolume, newVolume)
        volume = newVolume
    }

    fun updateMusic(context: Context, uri: Uri?) {
        if(uri == null) {
            return
        }

        music.value = MediaPlayer()

        music.value!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        music.value!!.setDataSource(context, uri)
        music.value!!.prepare()
    }
}

