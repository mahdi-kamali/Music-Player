package com.example.musicplayer.mvvm.repository

import androidx.lifecycle.LiveData
import com.example.musicplayer.mvvm.dao.Dao
import com.example.musicplayer.mvvm.entitys.PlayingListEntity
import com.example.musicplayer.mvvm.entitys.PlayListsChangeEntity
import com.example.musicplayer.mvvm.entitys.PlayingSongEntity

class MainRepository(private val dao: Dao) {









    /* PlayLists */
    suspend fun setPlayListDataChange (playListsChanges : ArrayList<PlayListsChangeEntity>) {

        clearPlayListDataChangeEntity()
        for (playList in playListsChanges) {
            dao.setPlayListDataChangeEntity(playList)
        }


    }

     fun getLivePlayListDataChangeEntity () : LiveData<List<PlayListsChangeEntity>> {
        return dao.getLivePlayListDataChangeEntity()
    }

     fun clearPlayListDataChangeEntity () {
        dao.clearPlayListDataChangeEntity()
    }


    /* PlayingList Entity */
    suspend fun addSongToPlayingListEntity(playingListEntity: PlayingListEntity) {
         dao.addToPlayingListEntity(playingListEntity)
    }


    suspend fun addSongsToPlayingListEntity(playingListEntity: ArrayList<PlayingListEntity>) {

            dao.addSongsToPlayingListEntity(playingListEntity)

    }



     fun getLivePlayingListEntity () : LiveData<List<PlayingListEntity>> {
        return dao.getLivePlayingListEntity()
    }

     fun getPlayingListEntity () :List<PlayingListEntity> {
        return dao.getPlayingListEntity()
    }

    suspend fun clearPlayingListEntity() {
         dao.clearPlayingListEntity()
    }




    /* PlayingSong Entity */
     fun getLivePlayingSongEntity () : LiveData<List<PlayingSongEntity>> {
        return dao.getLivePlayingSongEntity()
    }
     fun getPlayingSongEntity () : List<PlayingSongEntity> {
        return dao.getPlayingSongEntity()
    }
    suspend fun setPlayingSongEntity (playingSongEntity: PlayingSongEntity) {
        dao.setPlayingSongEntity(playingSongEntity)
    }













}