package com.example.musicplayer

import androidx.recyclerview.widget.DiffUtil
import com.example.musicplayer.objects.Music

class MyDiffUtil(oldList: ArrayList<Music> , newList : ArrayList<Music>) : DiffUtil.Callback() {

    private val oldList = oldList
    private val newList = newList

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].musicPath == newList[newItemPosition].musicPath
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        return when {
            oldList[oldItemPosition].musicName != newList[newItemPosition].musicName -> {
                false
            }
            oldList[oldItemPosition].musicPath != newList[newItemPosition].musicPath -> {
                false
            }
            oldList[oldItemPosition].musicImage != newList[newItemPosition].musicImage -> {
                false
            }
            oldList[oldItemPosition].musicArtists != newList[newItemPosition].musicArtists -> {
                false
            }
            oldList[oldItemPosition].musicDuration != newList[newItemPosition].musicDuration -> {
                false
            }
            else -> {
                true
            }


        }


    }
}