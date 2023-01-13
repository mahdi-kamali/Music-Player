package com.example.musicplayer.activitys.player


import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions

import com.example.musicplayer.CustomViewProvider
import com.example.musicplayer.R
import com.example.musicplayer.R.*
import com.example.musicplayer.customeviews.MySurfaceView
import com.example.musicplayer.helpers.ActivitySwitcher
import com.example.musicplayer.mvvm.entitys.PlayingListEntity
import com.example.musicplayer.mvvm.entitys.PlayingSongEntity
import com.example.musicplayer.mvvm.viewmodel.MainViewModel
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.glide.transformations.BlurTransformation


class PlayerActivity : AppCompatActivity() {

    private  val TAG = "PlayerActivityTag"
    private  var mediaPlayer: MediaPlayer = MediaPlayer()
    private  var next: ImageView? = null
    private  var playOrPause: ImageView? = null
    private  var previous: ImageView? = null
    private  var animationImageView: Animation? = null
    private  var animationNextButton : Animation? = null
    private  var playingImage : CircleImageView? = null
    private  var txtMusicName : TextView? = null
    private  var txtMusicArtists : TextView? = null
    private  var backButton : ImageView? = null
    private  var background : ImageView? = null
    private  var playingListImageView : ImageView? = null
    private  var backgroundBitmap : Bitmap? = null
    private  var nextBackgroundBitmap : Bitmap? = null
    private  var playerImgPositon : ImageView? = null
    private lateinit var visualizerView: MySurfaceView
    private  var visualizer: Visualizer = Visualizer(0)
    private var uri = "${Environment.getExternalStorageDirectory()}/3.mp3"



    private lateinit var mainViewModel: MainViewModel
    private lateinit var playingSongEntity : PlayingSongEntity
    private lateinit var playingListEntity : List<PlayingListEntity>






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_player)


        /* Views */
        background = findViewById(id.player_background)
        backButton = findViewById(id.back_to_acitivity)
        playingListImageView = findViewById(id.player_playing_list)
        playingImage = findViewById(id.img_player)
        txtMusicName=  findViewById(id.txt_music_name_player)
        txtMusicArtists = findViewById(id.txt_music_artists_player)
        next = findViewById(id.next)
        playOrPause = findViewById(id.playorpause)
        previous = findViewById(id.previous)
        visualizerView  =  findViewById(R.id.visualizer_view)






        /* Values */
        mainViewModel = CustomViewProvider(application, this).create(MainViewModel::class.java)
        animationImageView = AnimationUtils.loadAnimation(this, anim.bounce_imageviews)
        animationNextButton = AnimationUtils.loadAnimation(this, anim.bounce_buttons)




        mainViewModel.getLivePlayingListEntity().observe(this, {
            playingListEntity = it
        })


        mainViewModel.getLivePlayingSongEntity().observe(this, {
            playingSongEntity = it.last()




            setImage(playingSongEntity,playingImage!!)
            setTitles(playingSongEntity)
           // setBackground(playingSongEntity)





            uri = playingSongEntity.musicPath


            if (playMusic(visualizerView)) {
                visualizerView.startBarsAnimation()
                //    visualizerView.startCircleAnimation()
            }

















        })




        playingListImageView?.setOnClickListener {
            ActivitySwitcher.switchToSubActivitys(this, PlayingListActivity::class.java)
        }



        playOrPause?.setOnClickListener {



        }






    }




    private fun setImage (playingSongEntity: PlayingSongEntity , imageView: ImageView) {


        val multi = MultiTransformation(
            CircleCrop()
        )



        Glide
            .with(this)
            .load(playingSongEntity.musicImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(drawable.vinyl_disk)
            .apply(RequestOptions.bitmapTransform(multi))
            .into(imageView)

             imageView.startAnimation(animationImageView)

    }


    private fun setTitles (playingSongEntity: PlayingSongEntity) {

        txtMusicName?.text = (playingSongEntity.musicName)
        txtMusicArtists?.text = (playingSongEntity.musicArtists)


    }


    private fun setBackground (playingSongEntity: PlayingSongEntity) {




        Glide
            .with(this)
            .load(playingSongEntity.musicImage)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(10, 1)))
            .into(background!!)


        val slide = AnimationUtils.loadAnimation(this, com.example.musicplayer.R.anim.out)
        background!!.startAnimation(slide)













    }


    private fun getPosition (playingSongEntity: PlayingSongEntity) : Int {


        for (song in playingListEntity) {
            if (song.musicPath == playingSongEntity.musicPath) {
                return playingListEntity.indexOf(song)
            }
        }


        return -1


    }




    override fun onBackPressed() {
        super.onBackPressed()
        ActivitySwitcher.fallBackToParentActivity(this)
    }







    private fun initVisualizer(mediaPlayer: MediaPlayer , mySurfaceView: MySurfaceView) {








        visualizer.release()
        visualizer = Visualizer(mediaPlayer.audioSessionId)
        visualizer.measurementMode = Visualizer.MEASUREMENT_MODE_NONE
        visualizer.scalingMode = Visualizer.SCALING_MODE_NORMALIZED


        // Set the size of the byte array returned for visualization
        visualizer.captureSize = 512
        // Whenever audio data is available, update the visualizer view


        visualizer.setDataCaptureListener(object : Visualizer.OnDataCaptureListener {


            override fun onWaveFormDataCapture(
                visualizer: Visualizer,
                bytes: ByteArray,
                samplingRate: Int
            ) {
                // Do nothing, we are only interested in the FFT (aka fast Fourier transform)
            }

            override fun onFftDataCapture(
                visualizer: Visualizer,
                bytes: ByteArray,
                samplingRate: Int
            ) {

                mySurfaceView.updateVisualizer(bytes)



            }
        }, Visualizer.getMaxCaptureRate(), false, true)



        // Start everything
        visualizer.enabled = true








    }





    private fun playMusic (mySurfaceView: MySurfaceView) : Boolean {

        try {

            mediaPlayer.release()
            mediaPlayer = MediaPlayer.create(this, Uri.parse(uri))
            mediaPlayer.start()
            mediaPlayer.isLooping = true
            initVisualizer(mediaPlayer, mySurfaceView)
            return true

        }
        catch ( e : Exception ) {

            return false

        }



    }





    override fun onDestroy() {
        super.onDestroy()
        recycle()

    }


    private fun recycle () {

        mediaPlayer.release()
        playingListEntity = emptyList()
        mediaPlayer  = MediaPlayer()
        next  = null
        playOrPause = null
        previous = null
        animationImageView =  null
        playingImage = null
        txtMusicName = null
        txtMusicArtists = null
        backButton  = null
        background = null
        playingListImageView  = null
        backgroundBitmap = null


    }



}


