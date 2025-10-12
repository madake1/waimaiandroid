package com.madake.waimai.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.animation.doOnEnd

class TextColorAnimator(
    private val textView: TextView,
    private val text: String,
    private val start: Int = 0,
    private val end: Int = text.length,
    private val colors: List<Int> = listOf(Color.RED, Color.YELLOW, Color.GREEN),
    private val duration: Long = 800L // 渐变动画时长
) {

    private var index = 0
    private var animator: ValueAnimator? = null

    fun start() {
        animateNextColor()
    }

    private fun animateNextColor() {
        if (colors.isEmpty()) return

        val fromColor = colors[index % colors.size]
        val toColor = colors[(index + 1) % colors.size]
        index++

        animator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
            setDuration(duration)
            addUpdateListener { animation ->
                val color = animation.animatedValue as Int
                val spannable = SpannableString(text)
                spannable.setSpan(
                    ForegroundColorSpan(color),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                textView.text = spannable
            }
            doOnEnd { animateNextColor() } // 动画结束后切换到下一组颜色
            start()
        }
    }

    fun stop() {
        animator?.cancel()
        animator = null
    }
}
