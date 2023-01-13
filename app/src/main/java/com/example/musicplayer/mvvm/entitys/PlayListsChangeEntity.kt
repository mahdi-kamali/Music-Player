package com.example.musicplayer.mvvm.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Play_List_Change_Entity")
data class PlayListsChangeEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int ,
    val playListName : String,
    val updateType : String,

        )