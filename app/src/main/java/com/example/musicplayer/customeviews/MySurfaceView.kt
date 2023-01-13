package com.example.musicplayer.customeviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder

import android.view.SurfaceView
import android.view.View
import com.example.musicplayer.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MySurfaceView(context: Context?, attrs: AttributeSet?) :
    View(context,attrs) {

    private val TAG = "Hello"
    private var bytes : ByteArray =  ByteArray(0)



    private val shapesPaint = Paint()
    private var radius = 0f
    private val delayTime = 4L
    private var rects : ArrayList<RectF> = ArrayList()
    private var pastRects : ArrayList<RectF> = ArrayList()

    /* Render State Boolean */
    private var screenActive = true



    /* Playing Animations State Array */
    private var playingAnimationMode : ArrayList<Boolean> = ArrayList()


    /* Animations Controlling Arrays */
    private var rectAnimationsState  : ArrayList<Boolean> = ArrayList()
    private var circleAnimationsState : ArrayList<Boolean> = ArrayList()










    init {
      //  holder.addCallback(this)
        init()
    }

    /* Class Initialiazer */
    private fun init() {



        /* Shape Paint Properties */
        shapesPaint.strokeWidth = 1f
        shapesPaint.isAntiAlias = true
        shapesPaint.style = Paint.Style.FILL;
        shapesPaint.color = resources.getColor(R.color.out,null)



        /* Fill All Animations Controlling Arrays To false */
        for (i in 0 until 64) {
            playingAnimationMode.add(false)
            rectAnimationsState.add(false)
            circleAnimationsState.add(false)
        }






        /* Start Rendering */
        startDrawing()






    }


    /* Update Visualizer's Data */
    fun updateVisualizer(bytes: ByteArray) {
        this.bytes = bytes
        this.screenActive = true
    }







    /* Call Drawing Function To Draw And Render Frame */
    fun startDrawing () {





        /* Refresh Frame */
        GlobalScope.launch(Dispatchers.Main) {

            while (screenActive) {
                invalidate()
                delay(10)
            }
        }


    }







    /* Anim(0) Bar Animation */
    fun startBarsAnimation () {
        playingAnimationMode[0] = true
        GlobalScope.launch(Dispatchers.Default) {




            while (playingAnimationMode[0]) {


                if (bytes.isNotEmpty()) {
                    var left = 0f
                    var top = 0f
                    var right = 0f
                    val bottom = height.toFloat() + 10
                    val size = bytes.size/8
                    var sample : ArrayList<Int> = ArrayList()


                    /* Set New Rects Size */
                    rects = ArrayList()
                    for (position in 0 until size) {


                        sample = getSamples(position,8)
                        top = sample.maxOrNull()!!.toFloat()



                        top = bottom - (top)
                        left = right
                        right = left + width/size




                        rects.add( RectF ( left,top,right,bottom ) )
                    }


                    /* Check The Past Rects Bars Animations State  */
                    if (pastRects.size != rects.size) { pastRects = rects }
                    else { animateBars() }






                    /* Delay For onDraw */
                    delay(60)
                }


            }











        }
    }



    /* Anim(1) Circle Animation */
    fun startCircleAnimation () {



        playingAnimationMode[1] = true
        GlobalScope.launch(Dispatchers.Default) {
            while (playingAnimationMode[1]) {



                while (radius != width/2f) {
                    radius += 1
                    delay(5)
                }

                while (radius != 0f) {
                    radius -= 1
                    delay(5)
                }



                delay(delayTime)
            }

            /* after Looping Ends , Radius Value Should Be 0 */
            radius = 0f


        }




    }











    /* Cancel All Animations */
    fun cancelAnimations () {

        for (animation in 0 until playingAnimationMode.size) {
            playingAnimationMode[animation] = false
        }


    }








    /* Start Calculating Bars Animation */
    private fun animateBars () {
        val pastRects = pastRects
        val newRects = rects


        for (position in 0 until pastRects.size) {

            val newTop = newRects[position].top
            var pastTop = pastRects[position].top


            /* Check The Bars Position's animation Has Ended Or No */
            if (!rectAnimationsState[position]) {

                GlobalScope.launch(Dispatchers.Default) {
                    while (newTop != pastTop && playingAnimationMode[0]) {

                        rectAnimationsState[position] = true



                        /* Max And Min Values For Holding Animation */
                        val min = 41f
                        val max = height/4f
                        val sumInTrueTime = 20
                        val sumEveryErortime = 2f



                        /* This If Statment Says When The New bars Value Is Bigger The Past Bars Values */
                        if (newTop > pastTop) {



                            val defrent = newTop - pastTop
                            var perTime = 0

                            when(defrent) {
                                in min..max -> {
                                    for (i in sumInTrueTime downTo 0 step 2) {
                                        if (defrent % i == 0f) {
                                            perTime = i
                                            break
                                        }
                                    }
                                    pastTop += perTime

                                }
                                else  -> { pastTop += sumEveryErortime }


                            }






                        }



                        /* This If Statment Says When The New bars Value Is Lower The Past Bars Values */
                        if (newTop < pastTop) {


                            val defrent = pastTop - newTop
                            var perTime = 0


                            when(defrent) {
                                in min..max -> {
                                    for (i in sumInTrueTime downTo 0 step 2) {
                                        if (defrent % i == 0f) {
                                            perTime = i
                                            break
                                        }
                                    }
                                    pastTop -= perTime

                                }
                                else  -> { pastTop -= sumEveryErortime }


                            }




                        }













                        pastRects[position].top = pastTop



                        delay(delayTime)

                    }
                    rectAnimationsState[position] = false

                }



            }





        }








    }








    /* Sample Data Alghorythm */
    private fun getSamples (position : Int , buffer : Int) : ArrayList<Int> {
        val sampleArray : ArrayList<Int> = ArrayList()
        for ( i in position * buffer until ( ( position + 1 ) * buffer)) {

            val byte = bytes[i]
            var temp : Int



            /* Reverse Nagative Data To Positive */
            if (byte < 0 )
            { temp  = (byte * -1) }
            else { temp = byte * 1 }



            /* Set Data To 0 If Data is Lower Then 1 */
            if (temp < 1) { temp = 0 }





            if (position in 2..7) { if (temp != 0) { temp *= 5 } }
            if (position in 8..63) { if (temp != 0) { temp *= 7 } }




            if (temp  >= height/4) { temp = height/8 }

            if (temp % 2 != 0) {
                temp += 1
            }


            sampleArray.add(temp )



        }

        return sampleArray
    }





    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        if (bytes.isNotEmpty()) {


            /* Rects Drawing */
            for (rect in pastRects) {
                canvas.drawRoundRect(rect,20f,20f,shapesPaint)
            }


            /* Circle Drawing */
            canvas.drawCircle(width/2f,height/2f,radius,shapesPaint)
        }




    }







    fun activeScreen () {
        this.screenActive = true
        init()
    }






}