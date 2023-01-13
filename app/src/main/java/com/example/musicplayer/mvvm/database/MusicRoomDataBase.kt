package com.example.musicplayer.mvvm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musicplayer.mvvm.dao.Dao
import com.example.musicplayer.mvvm.entitys.ImageEntity
import com.example.musicplayer.mvvm.entitys.PlayingListEntity
import com.example.musicplayer.mvvm.entitys.PlayListsChangeEntity
import com.example.musicplayer.mvvm.entitys.PlayingSongEntity

@Database(entities = [PlayingListEntity::class , PlayListsChangeEntity::class ,PlayingSongEntity::class, ImageEntity::class], version = 1, exportSchema = false)
abstract class MusicRoomDataBase : RoomDatabase() {

    abstract fun dao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MusicRoomDataBase? = null

        fun getDatabase(context: Context): MusicRoomDataBase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MusicRoomDataBase::class.java,
                    "room_data_base.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}