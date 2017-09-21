package com.singletondev.animationandroid

import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.animation.DynamicAnimation
import android.support.animation.FlingAnimation
import android.support.animation.SpringAnimation
import android.support.animation.SpringForce
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    val flingAnimationX: FlingAnimation by lazy(LazyThreadSafetyMode.NONE){
        FlingAnimation(img,DynamicAnimation.X).setFriction(1.1f)
    }

    val flingAnimationY: FlingAnimation by lazy(LazyThreadSafetyMode.NONE){
        FlingAnimation(img,DynamicAnimation.Y).setFriction(1.1f)
    }

    val screenSize by lazy(LazyThreadSafetyMode.NONE) {
        val size = Point()
        this.windowManager.defaultDisplay.getSize(size)
        size

    }

    val gestureListener = object : GestureDetector.SimpleOnGestureListener(){
        override fun onDown(e: MotionEvent?): Boolean = true
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            flingAnimationX.setStartVelocity(velocityX)
            flingAnimationY.setStartVelocity(velocityY)

            flingAnimationX.start()
            flingAnimationY.start()

            return true
        }
    }

    val gestureDetector = GestureDetector(baseContext,gestureListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img.setOnTouchListener{
            _, motionEvent -> gestureDetector.onTouchEvent(motionEvent)
        }

        img.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                flingAnimationX.setMinValue(0f).setMaxValue((screenSize.x - img.width).toFloat())
                flingAnimationY.setMinValue(0f).setMaxValue((screenSize.y -img.height).toFloat())
                img.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

    }
}

///*var xDiffInTouchPointAndViewTopLeftCorner : Float = 0f
//var yDiffInTouchPointAndViewTopLeftCorner : Float = 0f
//
//private val springForce: SpringForce by lazy(LazyThreadSafetyMode.NONE){
//    SpringForce(0f).apply {
//        stiffness = SpringForce.STIFFNESS_MEDIUM
//        dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
//    }
//
//}
//
//private val springAnimationTranslationX: SpringAnimation by lazy(LazyThreadSafetyMode.NONE) {
//    SpringAnimation(img,DynamicAnimation.TRANSLATION_X).setSpring(springForce)
//}
//
//private val springAnimationTranslationY: SpringAnimation by lazy(LazyThreadSafetyMode.NONE){
//    SpringAnimation(img,DynamicAnimation.TRANSLATION_Y).setSpring(springForce)
//}
//
//private fun setupListener(){
//    img.setOnTouchListener { view, motionEvent ->
//        when(motionEvent.action){
//            MotionEvent.ACTION_DOWN -> {
//                xDiffInTouchPointAndViewTopLeftCorner = motionEvent.rawX - view.x
//                yDiffInTouchPointAndViewTopLeftCorner = motionEvent.rawY - view.y
//
//                springAnimationTranslationX.cancel()
//                springAnimationTranslationY.cancel()
//
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                img.x = motionEvent.rawX - xDiffInTouchPointAndViewTopLeftCorner
//                img.y = motionEvent.rawY - yDiffInTouchPointAndViewTopLeftCorner
//
//            }
//
//            MotionEvent.ACTION_UP -> {
//                springAnimationTranslationX.start()
//                springAnimationTranslationY.start()
//            }
//        }
//
//        true
//
//    }
//}*/
