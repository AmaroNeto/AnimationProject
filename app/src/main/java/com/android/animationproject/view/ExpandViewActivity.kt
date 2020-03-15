package com.android.animationproject.view

import android.animation.AnimatorSet
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.android.animationproject.R
import com.android.animationproject.utils.AnimatorUtils

class ExpandViewActivity : AppCompatActivity() {

    private lateinit var cardBox: CardView
    private lateinit var intro: ConstraintLayout
    private lateinit var detail: ConstraintLayout
    private var shouldExpand = true

    private var oldHeight = 0
    private var newHeight= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expand_view)

        cardBox = findViewById(R.id.card_container)
        intro = findViewById(R.id.intro)
        detail = findViewById(R.id.detail)

        setViewHeight()

        cardBox.setOnClickListener {
            if(shouldExpand) {
                expandAnimation()
                shouldExpand = shouldExpand.not()
            } else {
                collapseAnimation()
                shouldExpand =  shouldExpand.not()
            }
        }
    }

    private fun setViewHeight() {
        intro.viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    oldHeight = intro.getHeight()
                    intro.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })

        detail.viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    newHeight = detail.getHeight()
                    detail.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    detail.visibility = View.GONE
                }
            })
    }

    private fun expandAnimation() {
        val changeColor = AnimatorUtils.changeColor(
            cardBox::setCardBackgroundColor,
            ContextCompat.getColor(this, R.color.red),
            ContextCompat.getColor(this,R.color.white),
            500)

        val resize = AnimatorUtils.resize(cardBox, oldHeight, newHeight, 300)
        val fadeOut = AnimatorUtils.hideWithFadeOut(intro, 200)
        val fadeIn = AnimatorUtils.showWithFadeIn(detail, 300)

        AnimatorSet().apply {
            playTogether(changeColor, resize, fadeOut)
            play(fadeIn).after(fadeOut)
            start()
        }
    }

    private fun collapseAnimation() {
        val changeColor = AnimatorUtils.changeColor(
            cardBox::setCardBackgroundColor,
            ContextCompat.getColor(this, R.color.white),
            ContextCompat.getColor(this, R.color.red),
            500)

        val resize = AnimatorUtils.resize(cardBox, newHeight, oldHeight, 250)
        val fadeOut = AnimatorUtils.hideWithFadeOut(detail, 200)
        val fadeIn = AnimatorUtils.showWithFadeIn(intro, 300)

        AnimatorSet().apply {
            playTogether(changeColor, resize, fadeOut)
            play(fadeIn).after(fadeOut)
            start()
        }
    }
}
