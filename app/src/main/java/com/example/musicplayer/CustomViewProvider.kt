package com.example.musicplayer

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicplayer.mvvm.viewmodel.MainViewModel


class CustomViewProvider(private val mApplication: Application ,private val lifecycleOwner: LifecycleOwner) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(mApplication,lifecycleOwner) as T

    }
}