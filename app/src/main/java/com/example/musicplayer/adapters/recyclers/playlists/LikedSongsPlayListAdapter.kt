package com.example.musicplayer.adapters.recyclers.playlists

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicplayer.CustomViewProvider
import com.example.musicplayer.R
import com.example.musicplayer.mvvm.viewmodel.MainViewModel
import com.example.musicplayer.objects.Music
import android.view.animation.AccelerateDecelerateInterpolator

import android.animation.ObjectAnimator
import android.widget.*
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.DiffUtil
import com.example.musicplayer.MyDiffUtil
import com.example.musicplayer.mvvm.entitys.PlayingListEntity
import com.example.musicplayer.mvvm.entitys.PlayListsChangeEntity


class LikedSongsPlayListAdapter (activity: Activity, lifecycleOwner: LifecycleOwner, playListName : String) :
    RecyclerView.Adapter<LikedSongsPlayListAdapter.MyViewHolder>() {

    private var TAG = "ListAdapter"
    private val playListName = playListName
    private val activity = activity
    private val mainViewModel =
        CustomViewProvider(activity.application, lifecycleOwner).create(MainViewModel::class.java)
    private var musics: ArrayList<Music> = mainViewModel.getPlayListMusics(playListName)
    private val tables = mainViewModel.getPlayListsTable()
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("UseCompatLoadingForDrawables")
    private val like = activity.resources.getDrawable(R.drawable.like_red)
    @SuppressLint("UseCompatLoadingForDrawables")
    private val liked = activity.resources.getDrawable(R.drawable.liked)
    @SuppressLint("UseCompatLoadingForDrawables")
    private val mainDisk = activity.resources.getDrawable(R.drawable.main_disk)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(from(parent.context).inflate(R.layout.playlist_adapter, parent, false))
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView





    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        /* Views */
        val parent = holder.parent
        val textId = holder.textId
        val textName = holder.textMusicName
        val likeImage = holder.likeImage
        val songImage = holder.songImage


        /* Values */
        val musicName = musics[position].musicName
        val musicPath = musics[position].musicPath
        val musicImage = musics[position].musicImage
        val musicArtists = musics[position].musicArtists
        val musicAlbum = musics[position].musicAlbume
        val musicDuration = musics[position].musicDuration
        val music = Music(musicName, musicPath, musicImage, musicArtists, musicAlbum, musicDuration)


        /* ID TextView */
        textId.text = position.toString()


        /* Name TextView */
        textName.text = musicName
        textName.setOnClickListener {
            mainViewModel.addSongToPlayingListEntity(
                PlayingListEntity(
                    1,
                    musicName,
                    musicPath,
                    musicImage,
                    musicArtists,
                    musicAlbum,
                    musicDuration,
                )
            )
        }


        /* Song Image */
        Glide
            .with(activity)
            .load(musicImage)
            .error(mainDisk)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(mainDisk)
            .into(songImage)






        /* Like Image */
        animationStart(parent, music, "FadeIn")
        if (mainViewModel.getPlayListMusics(tables[1]).contains(music)) {
            Glide
                .with(activity)
                .load(liked)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(likeImage)
        } else {
            Glide
                .with(activity)
                .load(like)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(likeImage)
        }
        likeImage.setOnClickListener {
            val addingState =
                mainViewModel.addSongToPlayList(arrayListOf(PlayListsChangeEntity(0,playListName, "Normal")), arrayListOf(music))

            if (addingState ) {
                animationStart(it as ImageView, music, "Towward")
            }
            if (!addingState) {animationStart(it as ImageView, music, "TurnBack")}


        }










    }








    override fun getItemCount(): Int {

        return musics.size
    }



    /* Change Data */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newMusics: ArrayList<Music>){

        val myDiffUtil = MyDiffUtil(musics,newMusics)
        val diffResult =  DiffUtil.calculateDiff(myDiffUtil)
        musics = newMusics

        activity.runOnUiThread {
            diffResult.dispatchUpdatesTo(this)
        }


    }



    /* Animation For Views */
    private fun animationStart (view: View ,music: Music ,  turnState : String) {

        val roatateValue = 360f * 2
        val animationDuration = 700L

        when (turnState) {
            "Towward" -> {
                val imageView = view as ImageView
                val animation = ObjectAnimator.ofFloat(imageView, "rotationY", 0.0f, roatateValue)
                animation.duration = animationDuration
                animation.repeatCount = 0
                animation.interpolator = AccelerateDecelerateInterpolator()
                animation.start()
                animation.setAutoCancel(true)


                val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
                alphaAnimation.duration = animationDuration
                alphaAnimation.repeatCount = 0
                alphaAnimation.repeatMode = Animation.REVERSE
                imageView.startAnimation(alphaAnimation)


                Glide
                    .with(activity)
                    .load(liked)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)
            }

            "TurnBack" -> {


                val imageView = view as ImageView
                val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
                alphaAnimation.duration = animationDuration
                alphaAnimation.repeatCount = 0
                alphaAnimation.repeatMode = Animation.REVERSE
                imageView.startAnimation(alphaAnimation)

                if (playListName == tables[0]) { mainViewModel.removeSongFromPlayList(arrayListOf(PlayListsChangeEntity(0,tables[1],"Normal")),music) }
                else { mainViewModel.removeSongFromPlayList(arrayListOf(PlayListsChangeEntity(0,playListName,"Normal")),music) }

                Glide
                    .with(activity)
                    .load(like)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)


            }

            "FadeIn" -> {

                val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
                alphaAnimation.duration = 100
                alphaAnimation.repeatCount = 0
                alphaAnimation.repeatMode = Animation.REVERSE
                view.startAnimation(alphaAnimation)
            }


        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textId: TextView = itemView.findViewById(R.id.txt_id)
        val textMusicName: TextView = itemView.findViewById(R.id.txt_name)
        val songImage: ImageView = itemView.findViewById(R.id.item_image)
        val likeImage: ImageView = itemView.findViewById(R.id.liked_image)
        val parent: ViewGroup = itemView.findViewById(R.id.txt_name_root)





    }


}
