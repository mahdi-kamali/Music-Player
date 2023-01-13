package com.example.musicplayer.mvvm.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Playing_List_Entity")
data class PlayingListEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val musicName: String,
    val musicPath: String,
    val musicImage : String,
    val musicArtists : String,
    val musicAlbum : String,
    val musicDuration: String
)