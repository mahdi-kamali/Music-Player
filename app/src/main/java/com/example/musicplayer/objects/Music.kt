package com.example.musicplayer.objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Music  (
    val musicName : String ,
    val musicPath : String ,
    val musicImage : String ,
    val musicArtists : String ,
    val musicAlbume : String ,
    val musicDuration : String ,

    ) : Parcelable