package com.example.musicplayer.exoplayer

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.session.PlaybackStateCompat
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.example.musicplayer.CustomViewProvider
import com.example.musicplayer.R
import com.example.musicplayer.mvvm.entitys.PlayingSongEntity
import com.example.musicplayer.mvvm.viewmodel.MainViewModel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class ExoPlayerActivity : AppCompatActivity() {

    private lateinit var player: SimpleExoPlayer
    private lateinit var videoView : PlayerView
    private lateinit var mainViewModel: MainViewModel
    private lateinit var playingSongEntity: List<PlayingSongEntity>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player)
        videoView = findViewById(R.id.video_view)
        mainViewModel = CustomViewProvider(application,this).create(MainViewModel::class.java)



        lifecycleScope.launch(Dispatchers.Default){
            playingSongEntity = mainViewModel.getPlayingSongEntity()


            runOnUiThread {
                initializePlayer()
            }
        }


    }



    private fun  initializePlayer () {
        player = SimpleExoPlayer.Builder(this)
            .build()
            .also {
                videoView.player = it


                for (song in playingSongEntity) {
                    val mediaItem : MediaItem = MediaItem.fromUri(song.musicPath)
                    it.addMediaItem(mediaItem)
                }
                it.shuffleModeEnabled = false
                it.prepare()
                it.playWhenReady = true

            }
        player.seekToNext()
    }


    override fun onDestroy() {
        super.onDestroy()
        player.release()


    }


}