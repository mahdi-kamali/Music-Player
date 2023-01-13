package com.example.musicplayer.mvvm.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.musicplayer.objects.Music

@Entity(tableName = "Playing_Song_Entity")
data class PlayingSongEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int ,
    val musicName: String,
    val musicPath: String,
    val musicImage : String,
    val musicArtists : String,
    val musicAlbum : String,
    val musicDuration: String,
    var lastTimePlayedDuration: String,
    var playingMode: String)