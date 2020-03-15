package com.android.animationproject.utils

import android.animation.*
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

object AnimatorUtils {

    fun changeColor(
        backgroundColor: (Int) -> Unit,
        from: Int, to: Int,
        time: Long
    ) = ValueAnimator().apply {
        setIntValues(from, to)
        setEvaluator(ArgbEvaluator())
        addUpdateListener { valueAnimator -> backgroundColor(valueAnimator.animatedValue as Int) }
        duration = time
    }

    fun resize(
        view: View,
        oldHeight: Int,
        newHeight: Int,
        time: Long
    ) =
        ObjectAnimator.ofInt(oldHeight, newHeight).apply {
            duration = time
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                view.layoutParams.height = it.animatedValue as Int
                view.requestLayout()
            }
        }

    fun showWithFadeIn(view: View, duration: Long) =
        ObjectAnimator.ofFloat(view, View.ALPHA, 1f).apply {
            setDuration(duration)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                     view.alpha = 0f
                     view.visibility = View.VISIBLE
                }
            })
        }

    fun hideWithFadeOut(view: View, duration: Long) =
        ObjectAnimator.ofFloat(view, View.ALPHA, 0f).apply {
            setDuration(duration)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.GONE
                }
            })
        }
}