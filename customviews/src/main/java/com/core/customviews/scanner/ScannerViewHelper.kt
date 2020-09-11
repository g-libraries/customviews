package com.core.customviews.scanner

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import kotlin.math.roundToLong

class ScannerViewHelper {

    private val oneWayAnimDuration = 1000L
    private val delay = 100L
    private lateinit var scannerView: View
    private lateinit var qrIV: ImageView

    fun initHesScanner(scannerView: View, qrIV: ImageView) {
        this.scannerView = scannerView
        this.qrIV = qrIV
    }
    private fun upAnim(): ObjectAnimator {
        val animator =
            ObjectAnimator.ofFloat(
                scannerView,
                View.TRANSLATION_Y,
                -qrIV.height.toFloat()
            )
        with(animator) {
            interpolator = android.view.animation.AccelerateDecelerateInterpolator()
            duration = oneWayAnimDuration
            doOnStart {
                scannerView.rotation = 0f
                scannerView.alpha = 0f
            }
            doOnEnd {
                scannerView.translationY = (-qrIV.height.toFloat() - scannerView.height.toFloat())
                scannerView.invalidate()
            }
        }

        return animator
    }

    private fun downAnim(): ObjectAnimator {
        val animator =
            ObjectAnimator.ofFloat(
                scannerView,
                View.TRANSLATION_Y,
                0f - scannerView.height.toFloat()
            )
        with(animator) {
            interpolator = android.view.animation.AccelerateDecelerateInterpolator()
            duration = oneWayAnimDuration
            doOnStart {
                scannerView.alpha = 0f
                scannerView.rotation = 180f
            }
            doOnEnd {
                scannerView.alpha = 0f
                scannerView.translationY = 0f
            }
        }

        return animator
    }

    private fun fadeAnim(
        durationMillis: Long,
        startAlpha: Float,
        endAlpha: Float,
        delay: Long = 0L
    ): ObjectAnimator {
        val fadeIn =
            ObjectAnimator.ofFloat(
                scannerView,
                View.ALPHA,
                startAlpha,
                endAlpha
            )
        with(fadeIn) {
            interpolator = android.view.animation.LinearInterpolator()
            duration = durationMillis
            startDelay = delay
        }

        return fadeIn
    }

    fun startAnimation() {
        val set = AnimatorSet()
        val fade = AnimatorSet()
        fade.playSequentially(
            fadeAnim((oneWayAnimDuration / 2.5).roundToLong(), 0f, 1f, delay = delay),
            fadeAnim(oneWayAnimDuration / 4, 1f, 1f),
            fadeAnim(oneWayAnimDuration / 8, 1f, 0f),
            fadeAnim((oneWayAnimDuration / 2.5).roundToLong(), 0f, 1f, delay = delay + oneWayAnimDuration / 8),
            fadeAnim(oneWayAnimDuration / 4, 1f, 1f),
            fadeAnim(oneWayAnimDuration / 8, 1f, 0f)
        )
        set.playSequentially(
            upAnim(), downAnim()
        )
        set.start()
        fade.start()
    }
}