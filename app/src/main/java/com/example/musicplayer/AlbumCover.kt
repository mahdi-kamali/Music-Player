package com.example.musicplayer

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import wseemann.media.FFmpegMediaMetadataRetriever
import android.media.MediaMetadataRetriever
import java.lang.Exception



class AlbumCover(private val activity: Activity) {


    fun  test (path : String) : Bitmap{



        val retriever = FFmpegMediaMetadataRetriever()
        retriever.setDataSource(path)

        val data: ByteArray = retriever.embeddedPicture
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

        retriever.release()
        return bitmap




    }


     fun getAlbumImage(path: String): Bitmap? {
         return try {

             val mmr = MediaMetadataRetriever()
             mmr.setDataSource(path)
             val data = mmr.embeddedPicture
             if (data != null) BitmapFactory.decodeByteArray(data, 0, data.size) else {
                // return BitmapFactory.decodeResource(mainActivity.resources,R.drawable.disk_blue);
                 return null
             }
         }
         catch (e: Exception) {

             return null
            // BitmapFactory.decodeResource(mainActivity.resources,R.drawable.disk_blue);

         }



     }


}