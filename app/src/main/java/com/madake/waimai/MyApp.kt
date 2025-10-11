package com.madake.waimai

import android.app.Application
import android.content.Context

// 顶层全局变量
lateinit var mcontext: Context
lateinit var minstance: MyApplication

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化全局变量
        minstance = this
        mcontext = applicationContext
    }
}

