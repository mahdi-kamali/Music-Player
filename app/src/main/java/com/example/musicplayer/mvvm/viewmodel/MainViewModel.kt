package com.example.musicplayer.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.musicplayer.database.DBHandler
import com.example.musicplayer.mvvm.database.MusicRoomDataBase
import com.example.musicplayer.mvvm.entitys.PlayingListEntity
import com.example.musicplayer.mvvm.entitys.PlayListsChangeEntity
import com.example.musicplayer.mvvm.entitys.PlayingSongEntity
import com.example.musicplayer.mvvm.repository.MainRepository
import com.example.musicplayer.objects.Music
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application , lifecycleOwner: LifecycleOwner): AndroidViewModel(application)  {
    private val TAG = "MainViewModel"


    private val dataBase : DBHandler = DBHandler(application)
    private val roomDataBase = MusicRoomDataBase.getDatabase(application)
    private val livePlayListsTable : MutableLiveData<ArrayList<String>> = MutableLiveData()
    private val livePlayListsItems : LinkedHashMap<String,MutableLiveData<ArrayList<Music>>> = LinkedHashMap()
    private val musicStateRepository = MainRepository(roomDataBase.dao())
    private val lifecycleOwner = lifecycleOwner


    init {

        /* Set Value Of PlayLists Table */
        livePlayListsTable.value = dataBase.getTables()


        /* Create LivePlayList For Each PlayList Available  */
        for (playListTable in livePlayListsTable.value!!) {
            livePlayListsItems.put(playListTable, MutableLiveData(dataBase.getItems(playListTable)))
        }





    }



    /* GET All  PlayLists Name */
    fun getPlayListsTable () : ArrayList<String> {

        return dataBase.getTables()

    }
    fun getLivePlayListsTable () :LiveData<ArrayList<String>> {

        return livePlayListsTable

    }



    /* Create Or Delete  PlayList */
    fun createNewPlyList (playListName: String): Boolean {

        return if (dataBase.createNewTable(playListName)) {
            livePlayListsItems.put(playListName, MutableLiveData(dataBase.getItems(playListName)))
            livePlayListsTable.postValue(dataBase.getTables())
            true
        } else {
            false
        }


    }
    fun deletePlayList (playListName: String) : Boolean {
        return if (dataBase.deleteTable(playListName)) {

            livePlayListsTable.postValue(dataBase.getTables())
            true
        }
        else {
            false
        }


    }



    /* PlayList Musics */
    fun getPlayListMusics (playListName: String) : ArrayList<Music>  {

        return dataBase.getItems(playListName)

    }
    fun getLivePlayListMusics (playListName: String):LiveData<ArrayList<Music>> {
        return livePlayListsItems[playListName]!!
    }




    /* ADD Song To a PlayList */
    fun addSongToPlayList (playListsChanges: ArrayList<PlayListsChangeEntity>, musics : ArrayList<Music>) : Boolean  {

        val addingState : ArrayList<Boolean> = ArrayList()
            for (playList in playListsChanges) {

                val data = getPlayListMusics(playList.playListName)

                for (music in musics) {
                    if (data.contains(music)) {
                        return false
                    }
                    else {
                        addingState.add(dataBase.addItemToDataBase(playList.playListName,music.musicName,music.musicPath,music.musicImage,music.musicArtists,music.musicAlbume,music.musicDuration))

                    }
                }
            }

            setPlayListDataChange(playListsChanges)

        return !addingState.contains(false)




    }
    fun removeSongFromPlayList (playListsChanges: ArrayList<PlayListsChangeEntity>, music : Music) {

        val musicName = music.musicName
        val musicPath = music.musicPath
        val musicImage = music.musicImage
        val musicArtist = music.musicArtists
        val musicAlbum = music.musicAlbume
        val musicDuration = music.musicDuration

        for (playList in playListsChanges) {
            if (dataBase.deleteItem(playList.playListName,musicPath))
            {
                livePlayListsItems[playList.playListName]!!.postValue(dataBase.getItems(playList.playListName))
                setPlayListDataChange(playListsChanges)
            }
        }



    }
    fun removeAllSongsFromPlayList (playListsChangeEntity: PlayListsChangeEntity) {
        dataBase.deleteAllItem(playListsChangeEntity.playListName)
        if (playListsChangeEntity.updateType == "Full") {setPlayListDataChange(arrayListOf(playListsChangeEntity))  }
        if (playListsChangeEntity.updateType == "Normal") {setPlayListDataChange(arrayListOf(playListsChangeEntity)) }
        if (playListsChangeEntity.updateType == "Non") { }

    }



    /* Playing List Entity  */
    fun addSongToPlayingListEntity (playingListEntity: PlayingListEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            musicStateRepository.addSongToPlayingListEntity(playingListEntity)
        }
    }
    fun addSongsToPlayingListEntity (playingListEntity: ArrayList<PlayingListEntity> ) {
        viewModelScope.launch(Dispatchers.IO) {
            musicStateRepository.addSongsToPlayingListEntity(playingListEntity)
        }
    }
    fun getLivePlayingListEntity () : LiveData<List<PlayingListEntity>> {


        return musicStateRepository.getLivePlayingListEntity()

    }
    fun getPlayingListEntity () :List<PlayingListEntity> {

        return musicStateRepository.getPlayingListEntity()
    }
    fun setPlayingListEntity(musics: ArrayList<Music>)   {
        viewModelScope.launch(Dispatchers.IO)
        {


            val data : ArrayList<PlayingListEntity> = ArrayList()
            for (item in musics) {
                val playingListEntity = PlayingListEntity(
                    0,
                    item.musicName,
                    item.musicPath,
                    item.musicImage,
                    item.musicArtists,
                    item.musicArtists,
                    item.musicDuration
                )
                data.add(playingListEntity)
            }



            clearPlayingListEntity()
            addSongsToPlayingListEntity(data)



        }
    }
    suspend fun clearPlayingListEntity() {

        musicStateRepository.clearPlayingListEntity()




    }


    /* PlayLists Items Change Events */
     fun getLivePlayListDataChangeEntity() : LiveData<List<PlayListsChangeEntity>> {
        return musicStateRepository.getLivePlayListDataChangeEntity()
    }
    fun setPlayListDataChange (playListsChanges : ArrayList<PlayListsChangeEntity>) {


        lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            musicStateRepository.setPlayListDataChange(playListsChanges)
        }


    }




    /* PlayingSong Entity */
     fun getLivePlayingSongEntity () : LiveData<List<PlayingSongEntity>> {
       return musicStateRepository.getLivePlayingSongEntity()
    }
     fun getPlayingSongEntity () : List<PlayingSongEntity> {
        return musicStateRepository.getPlayingSongEntity()
    }
     fun setPlayingSongEntity (playingListEntity: PlayingListEntity) {

        val playingSongEntity = PlayingSongEntity(
            0,
            playingListEntity.musicName,
            playingListEntity.musicPath,
            playingListEntity.musicImage,
            playingListEntity.musicArtists,
            playingListEntity.musicAlbum,
            playingListEntity.musicDuration,
            "",
            ""
        )


        viewModelScope.launch(Dispatchers.IO) {
            musicStateRepository.setPlayingSongEntity(playingSongEntity)
        }
    }











}