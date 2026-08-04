package com.madake.waimai.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.madake.waimai.R

//*****************点击防抖**********************//
private var <T : View>T.triggerLastTime: Long
    get() = if (getTag(R.id.triggerLastTimeKey) != null) getTag(R.id.triggerLastTimeKey) as Long else 0
    set(value) {
        setTag(R.id.triggerLastTimeKey, value)
    }

/**
 * 给view添加一个延迟的属性（用来屏蔽连击操作）
 */
private var <T : View> T.triggerDelay: Long
    get() = if (getTag(R.id.triggerDelayKey) != null) getTag(R.id.triggerDelayKey) as Long else -1
    set(value) {
        setTag(R.id.triggerDelayKey, value)
    }

fun View.clickAble(): Boolean {
    var clickable = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        triggerLastTime = currentClickTime
        clickable = true
    }

    return clickable
}

//设置一个防抖的点击事件
fun <T : View> T.singleClick(delay: Long = 1000L, block: (T) -> Unit) {
    triggerDelay = delay
    setOnClickListener {
        if (clickAble()) {
            block(this)
        }
    }
}
//*****************点击防抖**********************//

fun ImageView.load(resName: String) {
    val context = this.context
    val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
    if (resId != 0) {
        Glide.with(context)
            .load(resId)
            .centerCrop()
            .into(this)
    } else {
        // 找不到资源时显示占位图
        Glide.with(context)
            .load(R.drawable.ic_launcher_foreground) // 占位图，可替换
            .centerCrop()
            .into(this)
    }
}
