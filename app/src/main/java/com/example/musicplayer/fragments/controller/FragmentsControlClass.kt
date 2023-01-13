package com.example.musicplayer.fragments.controller

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicplayer.activitys.mainactivity.MainActivity
import com.example.musicplayer.fragments.items.AllMusicsFragment
import com.example.musicplayer.fragments.items.LikedSongsFragment
import com.example.musicplayer.fragments.items.MainFragment
import com.example.musicplayer.mvvm.viewmodel.MainViewModel


class FragmentsControlClass(
    private val mainActivity: MainActivity,
    private val mainActivityOptions: MainActivity.MainActivityOptions,
    private val mainViewModel: MainViewModel,
    private val fragmentManager: FragmentManager,
    private val lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager,lifecycle) {

    private val TAG = "FragmentsControlClass"
    private var playListsFragments : LinkedHashMap<String, MainFragment> = LinkedHashMap()
    private var tables = mainViewModel.getPlayListsTable()






    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)



        /* Check PlayList Tables */
        mainViewModel.getLivePlayListsTable().observe(mainActivity, {
            tables = it
            checkFragments()
        })



    }


    override fun getItemCount(): Int {
        
        return tables.size
    }




    /*(CREATE) Fragment */
    override fun createFragment(position: Int): Fragment {


        val bundle = Bundle()
        bundle.putString("getPlayListName", tables[position])


        fragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            fragment.arguments = bundle
        }



        return when(position) {
            0 -> AllMusicsFragment()
            1 -> LikedSongsFragment()
            else -> MainFragment()


        }








    }





    @SuppressLint("NotifyDataSetChanged")
    fun checkFragments () {
        tables = mainViewModel.getPlayListsTable()




        for (position in 0 until tables.size) {

            if (playListsFragments.containsKey(tables[position]))
            {

            }
            else
            {
                playListsFragments.put(tables[position], MainFragment())
                val bundle = Bundle()
                bundle.putString("getPlayListName", tables[position])
                val targetPlayList = playListsFragments[tables[position]]
                targetPlayList!!.arguments = bundle
                notifyDataSetChanged()


            }

        }






    }








}