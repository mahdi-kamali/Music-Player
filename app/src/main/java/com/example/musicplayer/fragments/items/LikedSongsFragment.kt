package com.example.musicplayer.fragments.items

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.CustomViewProvider
import com.example.musicplayer.R
import com.example.musicplayer.mvvm.viewmodel.MainViewModel

import com.example.musicplayer.adapters.recyclers.playlists.LikedSongsPlayListAdapter


class LikedSongsFragment() : Fragment(R.layout.main_page) {
    private var TAG =  "MainFragment"






    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        /* Get PlayListName */
        val playListName = requireArguments().getString("getPlayListName") as String
        Log.i(TAG, "onViewCreated: $playListName")


        /* RecyclerView */
        val recyclerView: RecyclerView = view.findViewById(R.id.main_page_list)
        val likedSongsPlayListAdapter = LikedSongsPlayListAdapter(requireActivity(), viewLifecycleOwner, playListName)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = likedSongsPlayListAdapter
        recyclerView.setItemViewCacheSize(1)





        /* ViewModel Object */
        val mainViewModel = CustomViewProvider(
            requireActivity().application, viewLifecycleOwner
        ).create(MainViewModel::class.java)


        /* Set List Adapter's Data */





    }

}


