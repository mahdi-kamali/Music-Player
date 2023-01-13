package com.example.musicplayer.adapters.recyclers.playlists

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicplayer.CustomViewProvider
import com.example.musicplayer.MyDiffUtil
import com.example.musicplayer.R
import com.example.musicplayer.mvvm.entitys.PlayingListEntity
import com.example.musicplayer.mvvm.entitys.PlayingSongEntity
import com.example.musicplayer.mvvm.viewmodel.MainViewModel
import com.example.musicplayer.objects.Music

class PlayingListEntityAdapter(appCompatActivity: AppCompatActivity, mainViewModel: MainViewModel) :
    RecyclerView.Adapter<PlayingListEntityAdapter.ViewHolder>() {

    private val activity  =  appCompatActivity
    private val mainViewModel = mainViewModel
    private var musics : ArrayList<Music> = ArrayList()
    @SuppressLint("UseCompatLoadingForDrawables")
    private val like = activity.resources.getDrawable(R.drawable.like_red)
    @SuppressLint("UseCompatLoadingForDrawables")
    private val liked = activity.resources.getDrawable(R.drawable.liked)
    @SuppressLint("UseCompatLoadingForDrawables")
    private val mainDisk = activity.resources.getDrawable(R.drawable.main_disk)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(activity).inflate(R.layout.playlist_adapter,parent,false)
        return ViewHolder(view)


    }



    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        /* Views */
        val textId: TextView = holder.textId
        val textMusicName: TextView = holder.textMusicName
        val textArtistsName : TextView = holder.textArtistsName
        val songImage: ImageView = holder.songImage
        val likeImage: ImageView = holder.likeImage
        val parent : ViewGroup = holder.parent


        /* Values */
        val id = position.toString()
        val musicName = musics[position].musicName
        val musicPath = musics[position].musicPath
        val musicImage = musics[position].musicImage
        val musicArtist = musics[position].musicArtists


        /* TextId */
        textId.text = position.toString()



        /* Text MusicName */
        textMusicName.text = musicName


        /* Song Image */
        Glide
            .with(activity)
            .load(musicImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(mainDisk)
            .into(songImage)

        /* Text Artist Name */
        textArtistsName.text = musicArtist


        /* Parent */
        parent.setOnClickListener {
            Toast.makeText(activity, "Clicked $position", Toast.LENGTH_SHORT).show()
        }




    }



    override fun getItemCount(): Int {
        return musics.size
    }



    fun setData(newMusics: ArrayList<Music>){




        val myDiffUtil = MyDiffUtil(musics,newMusics)
        val diffResult =  DiffUtil.calculateDiff(myDiffUtil)
        musics = newMusics

        activity.runOnUiThread {
            diffResult.dispatchUpdatesTo(this)
        }


    }






    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textId: TextView = itemView.findViewById(R.id.txt_id)
        val textMusicName: TextView = itemView.findViewById(R.id.txt_name)
        val textArtistsName : TextView = itemView.findViewById(R.id.txt_Artists_name)
        val songImage: ImageView = itemView.findViewById(R.id.item_image)
        val likeImage: ImageView = itemView.findViewById(R.id.liked_image)
        val parent : ViewGroup = itemView.findViewById(R.id.playlist_adapter_root)


    }

}