package com.example.easyaudioplayer

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.easyaudioplayer.ui.theme.EasyAudioPlayerTheme
import java.net.URI

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<AudioPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EasyAudioPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TextMusic()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        newIntent(intent)
    }

    fun newIntent(intent: Intent?) {
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM)
        }

        viewModel.updateMusic(applicationContext, uri)
    }

    @Composable
    fun TextMusic() {
        ConstraintLayout {
            val (picture, line, pauseAndPlayButton) = createRefs()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(50.dp)
                    .constrainAs(picture) {
                        top.linkTo(parent.top)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(50.dp)
                    .constrainAs(line) {
                        top.linkTo(picture.bottom)
                    },
            ) {
                drawLine(
                    color = Color.Green,
                    start = Offset.Zero,
                    end = Offset(300.dp.toPx(), 0f),
                    strokeWidth = 8.dp.toPx()
                )

                drawCircle(
                    color = Color.Green,
                    radius = 12.dp.toPx(),
                    center = Offset.Zero
                )
            }

            Button(
                modifier = Modifier
                    .size(80.dp)
                    .constrainAs(pauseAndPlayButton) {
                        top.linkTo(line.bottom)
                        centerHorizontallyTo(parent)
                    },
                enabled = true,
                onClick = {
                    if(viewModel.music.value?.isPlaying == true) {
                        viewModel.music.value!!.pause()
                    } else {
                        viewModel.music.value?.start()
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Icon(
                    imageVector = Icons.Outlined.PlayArrow,
                    contentDescription = "play and pause button",
                    modifier = Modifier.size(200.dp)
                )
            }
        }
    }
}
