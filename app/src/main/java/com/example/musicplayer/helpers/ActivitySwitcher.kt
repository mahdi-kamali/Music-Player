package com.example.musicplayer.helpers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.musicplayer.R

class ActivitySwitcher() {

    companion object {

        fun switchToMainActivitys (activity: Activity, targetActivity: Class<*> , bundle : Bundle?) {
            val intent = Intent(activity,targetActivity)

            if (bundle != null) {
                intent.putExtra("Data" , bundle)
            }

            activity.startActivity(intent)





            activity.overridePendingTransition(R.anim.item_animation_fall_down, R.anim.fade_out);

        }

        fun switchToSubActivitys (activity: Activity  , targetActivity: Class<*>) {

            val intent = Intent(activity,targetActivity)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.item_animation_fall_down, R.anim.fade_out);

        }

        fun fallBackToParentActivity (activity: Activity) {
            activity.finish()
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }




    }



}