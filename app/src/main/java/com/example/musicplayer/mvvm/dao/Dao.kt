package com.example.musicplayer.mvvm.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.musicplayer.mvvm.entitys.ImageEntity
import com.example.musicplayer.mvvm.entitys.PlayListsChangeEntity
import com.example.musicplayer.mvvm.entitys.PlayingListEntity
import com.example.musicplayer.mvvm.entitys.PlayingSongEntity

@Dao
interface Dao {




    /* Music Playing Song */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPlayingSongEntity (playingSongEntity: PlayingSongEntity)

    @Query("SELECT * FROM playing_song_entity ORDER BY id ASC")
     fun getLivePlayingSongEntity() : LiveData<List<PlayingSongEntity>>


    @Query("SELECT * FROM playing_song_entity ORDER BY id ASC")
     fun getPlayingSongEntity() : List<PlayingSongEntity>




    /* Playing List Entity */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToPlayingListEntity(playingListEntity: PlayingListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSongsToPlayingListEntity (playingSongEntity: ArrayList<PlayingListEntity>)

    @Query("SELECT * FROM playing_list_entity ORDER BY id ASC")
     fun getLivePlayingListEntity(): LiveData<List<PlayingListEntity>>

    @Query("SELECT * FROM playing_list_entity ORDER BY id ASC")
     fun getPlayingListEntity() : List<PlayingListEntity>

    @Query("DELETE FROM playing_list_entity")
    suspend fun clearPlayingListEntity()






    /* PlayList Changes Entity */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPlayListDataChangeEntity (playListsChangeEntity: PlayListsChangeEntity)

    @Query ("SELECT * FROM play_list_change_entity ORDER BY id ASC")
    fun getLivePlayListDataChangeEntity () : LiveData<List<PlayListsChangeEntity>>


    @Query("DELETE FROM play_list_change_entity")
    fun clearPlayListDataChangeEntity()





    /* Image Entity */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImageEntity (imageEntity: ImageEntity)

    @Query("SELECT * FROM images_table ORDER BY id ASC")
    fun getLiveImageEntity() : LiveData<List<ImageEntity>>

}