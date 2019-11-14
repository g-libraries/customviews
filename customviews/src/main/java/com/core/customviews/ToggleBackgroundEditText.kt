package com.core.customviews

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText

/**
 * EditText which can switch background and animate it's alpha
 */
abstract class ToggleBackgroundEditText : AppCompatEditText {

    companion object {

        const val ALPHA_VISIBLE = 255
        const val ALPHA_HALF_VISIBLE = 100

        const val DEFAULT_FADE_IN_DURATION = 300L
        const val DEFAULT_FADE_OUT_DURATION = 300L

    }

    private var animations: ArrayList<Animator> = ArrayList()
    private var animatorSet: AnimatorSet? = null

    lateinit var backgrounds: Pair<Drawable?, Drawable?>

    var fadeInAnimDuration = DEFAULT_FADE_IN_DURATION
    var fadeOutAnimDuration = DEFAULT_FADE_OUT_DURATION

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    init {
        animations.add(
            ObjectAnimator.ofInt(ALPHA_VISIBLE, ALPHA_HALF_VISIBLE)
                .apply {
                    duration = fadeOutAnimDuration
                    addUpdateListener { updatedAnimation ->
                        background.alpha = updatedAnimation.animatedValue as Int
                    }
                })
        animations.add(
            ObjectAnimator.ofInt(ALPHA_HALF_VISIBLE, ALPHA_VISIBLE)
                .apply {
                    duration = fadeInAnimDuration
                    startDelay = fadeInAnimDuration
                    addUpdateListener { updatedAnimation ->
                        background.alpha = updatedAnimation.animatedValue as Int
                    }
                })
    }

    fun animateBackground() {
        animatorSet = AnimatorSet()
        animatorSet?.playTogether(animations)
        animatorSet?.start()
    }

    fun stopBackgroundAnimation() {
        animatorSet?.cancel()
        background.alpha = ALPHA_VISIBLE
    }

    fun toggleBackground(active: Boolean) {
        backgrounds.let {
            background = if (active) backgrounds.first else backgrounds.second
        }
    }

}