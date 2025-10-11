package com.madake.waimai.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import com.madake.waimai.R
import com.madake.waimai.mcontext

class CustomToast private constructor() {

    companion object {
        /**
         * 显示自定义 Toast
         * @param message 内容
         * @param dotColor 圆点颜色，可选
         */
        fun show(message: String, @ColorInt dotColor: Int? = null) {
            // 使用全局 applicationContext，避免 WindowManager 异常
            val context: Context = mcontext.applicationContext

            val inflater = LayoutInflater.from(context)
            val layout: View = inflater.inflate(R.layout.toast_custom, null)

            // 设置文字
            val textView = layout.findViewById<TextView>(R.id.toast_text)
            textView.text = message

            // 设置圆点图标
            val imageView = layout.findViewById<ImageView>(R.id.toast_icon)
            imageView.setImageResource(R.drawable.icon_circle)

            // 动态改变颜色
            val drawable = imageView.drawable.mutate() as GradientDrawable
            drawable.setColor(dotColor ?: 0xFFFFEB3B.toInt()) // 默认黄色

            // 创建并显示 Toast
            val toast = Toast(context)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = layout
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
        }
    }
}
