package com.example.musicplayer.fragments.items

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.CustomViewProvider
import com.example.musicplayer.R
import com.example.musicplayer.mvvm.viewmodel.MainViewModel
import android.provider.MediaStore

import android.graphics.Bitmap
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.musicplayer.AlbumCover
import com.example.musicplayer.adapters.recyclers.playlists.AllMusicPlayListAdapter
import com.example.musicplayer.mvvm.entitys.PlayListsChangeEntity
import com.example.musicplayer.objects.Music
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.roundToInt


class AllMusicsFragment() : Fragment(R.layout.main_page) {
    private var TAG =  "MainFragment"
    private lateinit var albumCover : AlbumCover
    private lateinit var mainViewModel : MainViewModel




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        albumCover = AlbumCover(requireActivity())

        /* Get PlayListName */
        val playListName = requireArguments().getString("getPlayListName") as String
        Log.i(TAG, "onViewCreated: $playListName")



        /* RecyclerView */
        val recyclerView: RecyclerView = view.findViewById(R.id.main_page_list)
        val allMusicPlayListAdapter = AllMusicPlayListAdapter(requireActivity(), viewLifecycleOwner, playListName)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = allMusicPlayListAdapter
        recyclerView.setItemViewCacheSize(1)





        /* ViewModel Object */
         mainViewModel = CustomViewProvider(
            requireActivity().application, viewLifecycleOwner
        ).create(MainViewModel::class.java)


        /* Set List Adapter's Data */
        getAllSongsInPhoneToList(requireActivity())

        mainViewModel.getLivePlayListDataChangeEntity().observe(viewLifecycleOwner, {

                for (playList in it) {
                    if (playList.playListName == playListName) {
                        if (playList.updateType == "Normal") {
                            allMusicPlayListAdapter.setData(mainViewModel.getPlayListMusics(playListName))
                        }
                        if (playList.updateType == "Full") {
                            getAllSongsInPhoneToList(requireActivity())
                        }


                    }
                }

            })






    }

    @SuppressLint("Recycle")
    fun getAllSongsInPhoneToList (fragmentActivity: FragmentActivity)  {
        fragmentActivity.lifecycleScope.launch(Dispatchers.IO)
        {
            val contentResolver = fragmentActivity.contentResolver
            val available = mainViewModel.getPlayListMusics("All Musics")
            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
            val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
            val cursor = contentResolver.query(uri, null, selection, null, sortOrder)



            if (cursor!!.moveToFirst()) {

                if (cursor.count >= 0) {


                    val musics : ArrayList<Music> = ArrayList()

                    while (cursor.moveToNext()) {


                        val musicName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                        val musicPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                        val musicArtists = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                        val musicAlbumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                        val musicDuration = cursor.getFloat(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                        val musicImage = File(fragmentActivity.filesDir, "$musicName.png")


                        val realDuration = getDuration(musicDuration)



                        val music = Music(musicName,musicPath,musicImage.path,musicArtists,musicAlbumName,realDuration)




                        if (available.contains(music)) {
                        }

                        else {
                            try {

                                val bitmap = albumCover.getAlbumImage(musicPath)
                                val file = File(fragmentActivity.filesDir, "$musicName.png")

                                FileOutputStream(file).use { out ->
                                    bitmap?.compress(Bitmap.CompressFormat.PNG, 100, out)
                                }
                            }

                            catch (e: IOException) { e.printStackTrace() }

                            if (musicDuration.toInt() > (0.5*1000)*60){
                                }
                            musics.add(music)
                        }


                    }



                    mainViewModel.addSongToPlayList(arrayListOf(PlayListsChangeEntity(0,"All Musics" , "Normal")),musics)





                }



            }



        }
    }
    fun getDuration(musicDuration : Float): String {
        val seconds = (musicDuration / 1000)
        val minutes = (seconds / 60).toInt()


        var secondString = ((seconds % 60)).roundToInt().toString()
        val minuteString = minutes.toString()



        if (secondString.length == 1) {
            if (secondString == "0") {
                secondString = "1"
            }
            secondString = "0$secondString"
        }
        return "$minuteString : $secondString"

    }


}


