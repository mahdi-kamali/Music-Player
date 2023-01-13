package com.example.musicplayer.activitys.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.CustomViewProvider
import com.example.musicplayer.R
import com.example.musicplayer.adapters.recyclers.playlists.PlayingListEntityAdapter
import com.example.musicplayer.helpers.ActivitySwitcher
import com.example.musicplayer.mvvm.viewmodel.MainViewModel
import com.example.musicplayer.objects.Music

class PlayingListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playing_list)

        val mainViewModel = CustomViewProvider(application,this).create(MainViewModel::class.java)
        val recyclerView : RecyclerView = findViewById(R.id.playing_list_recycler)
        val playingListEntityAdapter  = PlayingListEntityAdapter(this,mainViewModel)

        recyclerView.adapter = playingListEntityAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)




        /* Set PlayingList Data */
        mainViewModel.getLivePlayingListEntity().observe(this, {

            val dataMusics : ArrayList<Music> = ArrayList()
            for (music in it) {
                val temp =
                    Music(
                        music.musicName,
                        music.musicPath,
                        music.musicImage,
                        music.musicArtists,
                        music.musicAlbum,
                        music.musicDuration
                    )

                dataMusics.add(temp)

            }
            playingListEntityAdapter.setData(dataMusics)


        })









    }

    override fun onBackPressed() {
        super.onBackPressed()
        ActivitySwitcher.fallBackToParentActivity(this)


    }


}