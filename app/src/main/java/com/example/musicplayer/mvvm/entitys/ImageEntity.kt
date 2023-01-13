package com.example.musicplayer.mvvm.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.cert.CertPath


@Entity(tableName = "Images_table")
class ImageEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int ,
    val imagePath: String

        )