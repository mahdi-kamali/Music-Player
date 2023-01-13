package com.example.musicplayer.helpers

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.musicplayer.R

class AnimationSelector {


    companion object{

        const val FADE_IN = 0
        const val FADE_OUT = 1
        const val BOUNCE = 2



        fun startAnimation(view : View , animationKey : Int) {

            view.startAnimation(selectAnimation(animationKey,view.context))
        }




        private fun selectAnimation (animationKey : Int , context: Context) : Animation {

            return when (animationKey) {

                FADE_IN -> { AnimationUtils.loadAnimation(context, R.anim.fade_in) }
                FADE_OUT -> { AnimationUtils.loadAnimation(context, R.anim.fade_out) }
                BOUNCE -> { AnimationUtils.loadAnimation(context, R.anim.bounce_imageviews) }


                else -> { AnimationUtils.loadAnimation(context, R.anim.fade_in) }


            }



        }

    }



}