package com.example.musicplayer.activitys.mainactivity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.musicplayer.fragments.controller.FragmentsControlClass
import com.example.musicplayer.mvvm.viewmodel.MainViewModel
import com.example.musicplayer.objects.Music
import com.example.musicplayer.permissions.PermissionsClass
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import de.hdodenhof.circleimageview.CircleImageView
import android.graphics.Bitmap
import kotlin.math.roundToInt

import android.media.MediaPlayer

import android.widget.TextView
import androidx.core.net.toUri
import com.example.musicplayer.AlbumCover
import com.example.musicplayer.CustomViewProvider
import com.example.musicplayer.R
import com.example.musicplayer.mvvm.entitys.PlayingListEntity
import com.example.musicplayer.mvvm.entitys.PlayListsChangeEntity
import java.io.*
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.musicplayer.activitys.player.PlayerActivity
import com.example.musicplayer.exoplayer.ExoPlayerActivity
import com.example.musicplayer.helpers.ActivitySwitcher


class MainActivity : AppCompatActivity() {


    private val TAG = "MainActivity"










    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /* Parents */
        val durationRoot : ConstraintLayout = findViewById(R.id.duration_root)





        /* Views */
        val tabLayout : TabLayout =  findViewById(R.id.playList_tables)
        val viewPager2 : ViewPager2 = findViewById(R.id.view_pager)
        val musicImageCover : CircleImageView = findViewById(R.id.img_cover)






        /* Permissions Part */
        val permissions : Array<String> =
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.RECORD_AUDIO,
            )
        val permission = PermissionsClass(permissions,this)
        permission.askPermissions()



        /* ViewModel Object */
        val mainViewModel : MainViewModel = CustomViewProvider(application,this).
        create(MainViewModel::class.java)
        mainViewModel.createNewPlyList("Internet Musics")



        /* Main Activity Controller */
        val mainActivityOptions = MainActivityOptions(this,mainViewModel)




        /* ViewPager And TabLayout Part */
        val fragmentManager  =  supportFragmentManager
        val fragmentsControlClass = FragmentsControlClass(this,
            mainActivityOptions,
            mainViewModel,
            fragmentManager,
            lifecycle)
        viewPager2.adapter = fragmentsControlClass

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int
            )
            {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

        viewPager2.offscreenPageLimit = 1;





        /* Observe TabLayout List */
        mainViewModel.getLivePlayListsTable().observe(this, {
            mainActivityOptions.refreshTabLayout()
        })





        /* Views Click */
        durationRoot.setOnClickListener {

            val myIntent = Intent(this, PlayerActivity::class.java)
            this.startActivity(myIntent)
            this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }




        ActivitySwitcher.switchToSubActivitys(this,ExoPlayerActivity::class.java)

    }










    /* MainActivity Control Class */
    class MainActivityOptions (mainActivity: MainActivity, mainViewModel: MainViewModel) {

        private val TAG = "MainActivityOptions"
        private val mainActivity = mainActivity
        private val mainViewModel = mainViewModel
        private val lifecycleOwner : LifecycleOwner = mainActivity
        private val albumCover = AlbumCover(mainActivity)
        private val tabLayout: TabLayout = mainActivity.findViewById(R.id.playList_tables)
        private val musicImageCover : CircleImageView = mainActivity.findViewById(R.id.img_cover)
        private val musicNameTextView : TextView = mainActivity.findViewById(R.id.music_name)
        private val musicArtistsNameTextView : TextView = mainActivity.findViewById(R.id.artist_name)
        private val musicAlbumeTextView : TextView = mainActivity.findViewById(R.id.album_name)
        @SuppressLint("UseCompatLoadingForDrawables")
        private val blueDisk = mainActivity.resources.getDrawable(R.drawable.main_disk)
        var mediaPlayer = MediaPlayer()
        var musicPlayerEntity = PlayingListEntity(0,"Null","Null","Null","Null","Null","Null")


        init {

            /* Find All Songs */
           // this.getAllSongsInPhoneToList()
            this.initializeMusicPlayer()
            mainViewModel.setPlayListDataChange(arrayListOf(PlayListsChangeEntity(1,"All Musics" , "Normal")))

        }



        /* Refresh TabLayout */
        fun refreshTabLayout() {
            val tables = mainViewModel.getPlayListsTable()
            for (position in 0 until tables.size) {
                if (tabLayout.getTabAt(position) == null) {
                    val tab: TabLayout.Tab = tabLayout.newTab()
                    tab.text = mainViewModel.getPlayListsTable()[position]
                    tabLayout.addTab(tab)


                }
                else {
                    tabLayout.getTabAt(position)!!.text = tables[position]
                }
                setLongClickListenerForTab(position)


            }
        }



        /* TabLayout Tabs LongClick Listeners */
        private fun setLongClickListenerForTab (position : Int) {

            val linearLayout : LinearLayout = tabLayout.getChildAt(0) as LinearLayout


            linearLayout.getChildAt(position).setOnLongClickListener {
                Toast.makeText(mainActivity, "Tab clicked $position", Toast.LENGTH_SHORT).show()
                true
            }


        }







        /* This Method Returns Duration Of Music */
        fun getDuration(musicDuration : Float): String {
            val seconds = (musicDuration / 1000)
            val minutes = (seconds / 60).toInt()


            var secondString = ((seconds % 60)).roundToInt().toString()
            val minuteString = minutes.toString()



            if (secondString.length == 1) {
                if (secondString == "0") {
                    secondString = "1"
                }
                secondString = "0$secondString"
            }
            return "$minuteString : $secondString"

        }



        @SuppressLint("SetTextI18n")
        fun initializeMusicPlayer () {



        }

        fun playMusic (music: PlayingListEntity) {


            try {
                val musicFile = File(music.musicPath).toUri()
                mediaPlayer = MediaPlayer.create(mainActivity,musicFile)
                mediaPlayer.start()
                this.musicPlayerEntity = music


            }
            catch (e : Exception) {

            }

        }






    }





}