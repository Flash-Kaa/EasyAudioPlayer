package com.example.easyaudioplayer

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.easyaudioplayer.ui.theme.EasyAudioPlayerTheme
import dev.vivvvek.seeker.Seeker
import dev.vivvvek.seeker.SeekerDefaults

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
            val (picture, volumeIcon, volume, line, pauseAndPlay, loop) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .padding(40.dp)
                    .constrainAs(picture) {
                        top.linkTo(parent.top)
                        centerHorizontallyTo(parent)
                    }
            )

            Seeker(
                value = viewModel.volume,
                onValueChange = { viewModel.updateVolume(it) },
                colors = SeekerDefaults.seekerColors(
                    progressColor = Color.Green,
                    trackColor = Color.Gray,
                    thumbColor = Color.Green
                ),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .constrainAs(line) {
                        top.linkTo(picture.bottom)
                    }
            )

            Button(
                modifier = Modifier
                    .size(150.dp)
                    .padding(16.dp)
                    .constrainAs(pauseAndPlay) {
                        top.linkTo(line.bottom)
                        centerHorizontallyTo(parent)
                    },
                enabled = viewModel.music.value != null,
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

            Icon(
                painter = painterResource(
                    id = if(viewModel.volume == 0f) {
                            R.drawable.volume_off
                        } else if (viewModel.volume < 0.5f) {
                            R.drawable.volume_down
                        } else {
                            R.drawable.volume_up
                        }
                ),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(volumeIcon) {
                        end.linkTo(volume.start)
                        centerVerticallyTo(volume)
                    }
            )

            Seeker(
                value = viewModel.volume,
                onValueChange = { viewModel.updateVolume(it) },
                colors = SeekerDefaults.seekerColors(
                    progressColor = Color.LightGray,
                    trackColor = Color.Gray,
                    thumbColor = Color.LightGray
                ),
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(0.35f)
                    .constrainAs(volume) {
                        bottom.linkTo(parent.bottom)
                        centerHorizontallyTo(pauseAndPlay)
                    }
            )

            IconButton(
                onClick = {
                    viewModel.setLoop(viewModel.music.value?.isLooping == false)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(loop) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                colors = IconButtonDefaults.iconButtonColors(
                    if(viewModel.music.value?.isLooping == true) {
                        Color.LightGray
                    } else {
                        Color.Unspecified
                    }
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.loop),
                    contentDescription = "loop"
                )
            }
        }
    }
}
