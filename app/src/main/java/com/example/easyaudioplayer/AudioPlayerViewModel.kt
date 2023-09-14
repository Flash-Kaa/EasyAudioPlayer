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

    fun updateMusic(context: Context, uri: Uri?) {
        if(uri == null) {
            return
        }

        val mp = MediaPlayer()

        mp.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mp.setDataSource(context, uri)
        mp.prepare()
        music.value = mp
    }
}

