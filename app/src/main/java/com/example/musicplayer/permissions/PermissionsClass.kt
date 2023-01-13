package com.example.musicplayer.permissions

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build

class PermissionsClass(permissions : Array<String> , activity: Activity) {


    private val permissions = permissions
    private var state = false
    private val activity = activity



    /* (REQ) Request All Permissions */
    fun askPermissions()  {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(permissions,100)
        }
    }





    /* (GET)  All Permissions Results */
    fun  permissionsState() : Boolean {
        for (i in 0..permissions.size-1) {

            val temp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.checkSelfPermission(permissions[i]) == PackageManager.PERMISSION_GRANTED
            } else {
                return true
            }

            if (temp) {
                state = true
            }
            else {
                state = false
                break
            }

        }

        return state
    }




}