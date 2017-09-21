package com.singletondev.animationandroid

import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.animation.*
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    var xDiffInTouchPointAndViewTopLeftCorner : Float = 0f
    var yDiffInTouchPointAndViewTopLeftCorner : Float = 0f

    private val springForce: SpringForce by lazy(LazyThreadSafetyMode.NONE){
    SpringForce(0f).apply {
        stiffness = SpringForce.STIFFNESS_MEDIUM
        dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        }

    }

    private val springAnimationTranslationX: SpringAnimation by lazy(LazyThreadSafetyMode.NONE) {
        SpringAnimation(img,DynamicAnimation.TRANSLATION_X)
    }

    private val springAnimationTranslationY: SpringAnimation by lazy(LazyThreadSafetyMode.NONE){
        SpringAnimation(img,DynamicAnimation.TRANSLATION_Y)
    }


    private val springAnimationTranslationX1: SpringAnimation by lazy(LazyThreadSafetyMode.NONE) {
        createSpringAnim(img,DynamicAnimation.TRANSLATION_X)
    }

    private val springAnimationTranslationY1: SpringAnimation by lazy(LazyThreadSafetyMode.NONE){
        createSpringAnim(img,DynamicAnimation.TRANSLATION_Y)
    }

    private val springAnimationTranslationX2: SpringAnimation by lazy(LazyThreadSafetyMode.NONE) {
        createSpringAnim(img2,DynamicAnimation.TRANSLATION_X)
    }

    private val springAnimationTranslationY2: SpringAnimation by lazy(LazyThreadSafetyMode.NONE){
        createSpringAnim(img2,DynamicAnimation.TRANSLATION_Y)
    }

    private val springAnimationTranslationX3: SpringAnimation by lazy(LazyThreadSafetyMode.NONE) {
        createSpringAnim(img3,DynamicAnimation.TRANSLATION_X)
    }

    private val springAnimationTranslationY3: SpringAnimation by lazy(LazyThreadSafetyMode.NONE){
        createSpringAnim(img3,DynamicAnimation.TRANSLATION_Y)
    }


    private fun createSpringAnim (view : View, property: FloatPropertyCompat<View>) : SpringAnimation{
        return SpringAnimation(view,property).setSpring(SpringForce())
    }


    private fun setupAnimChainedListener(){

        springAnimationTranslationX1.addUpdateListener{
            _, value ,_ -> springAnimationTranslationX2.animateToFinalPosition(value)
        }

        springAnimationTranslationY1.addUpdateListener {
            _, value, _ -> springAnimationTranslationY2.animateToFinalPosition(value)
        }

        springAnimationTranslationX2.addUpdateListener {
            _, value, _ -> springAnimationTranslationX3.animateToFinalPosition(value)
        }

        springAnimationTranslationY2.addUpdateListener {
            _, value, _ -> springAnimationTranslationY3.animateToFinalPosition(value)
        }


        img.setOnTouchListener{
            view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = motionEvent.rawX - xDiffInTouchPointAndViewTopLeftCorner
                    val deltaY = motionEvent.rawY - yDiffInTouchPointAndViewTopLeftCorner

                    view.translationX = deltaX + view.translationX
                    view.translationY = deltaY + view.translationY

                    springAnimationTranslationX1.animateToFinalPosition(view.translationX)
                    springAnimationTranslationY1.animateToFinalPosition(view.translationY)

                }
            }
            yDiffInTouchPointAndViewTopLeftCorner = motionEvent.rawY
            xDiffInTouchPointAndViewTopLeftCorner = motionEvent.rawX

            return@setOnTouchListener true
        }

    }

    /*
    implement this method
     */
    private fun setupListener(){
    img.setOnTouchListener { view, motionEvent ->
        when(motionEvent.action){
            MotionEvent.ACTION_DOWN -> {
                xDiffInTouchPointAndViewTopLeftCorner = motionEvent.rawX - view.x
                yDiffInTouchPointAndViewTopLeftCorner = motionEvent.rawY - view.y

                springAnimationTranslationX.cancel()
                springAnimationTranslationY.cancel()

            }

            MotionEvent.ACTION_MOVE -> {
                img.x = motionEvent.rawX - xDiffInTouchPointAndViewTopLeftCorner
                img.y = motionEvent.rawY - yDiffInTouchPointAndViewTopLeftCorner

            }

            MotionEvent.ACTION_UP -> {
                springAnimationTranslationX.start()
                springAnimationTranslationY.start()
            }
        }

        true

    }
}

    //Fling animation
    //-----------------------------------------------------------------------
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

        setupAnimChainedListener()

//        img.setOnTouchListener{
//            _, motionEvent -> gestureDetector.onTouchEvent(motionEvent)
//        }
//
//        img.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
//            override fun onGlobalLayout() {
//                flingAnimationX.setMinValue(0f).setMaxValue((screenSize.x - img.width).toFloat())
//                flingAnimationY.setMinValue(0f).setMaxValue((screenSize.y -img.height).toFloat())
//                img.viewTreeObserver.removeOnGlobalLayoutListener(this)
//            }
//        })

    }
}

