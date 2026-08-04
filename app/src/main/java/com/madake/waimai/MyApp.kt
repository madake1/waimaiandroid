package com.madake.waimai

import android.app.Application
import android.content.Context
import com.amap.api.location.AMapLocationClient
import com.amap.api.maps.MapsInitializer

// 顶层全局变量
lateinit var mcontext: Context
lateinit var minstance: MyApplication

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 初始化全局变量
        minstance = this
        mcontext = applicationContext

        // ==========================
        // 高德 SDK 隐私合规初始化
        // ==========================

        // 已向用户展示隐私协议（开发阶段先设为 true）
        MapsInitializer.updatePrivacyShow(this, true, true)
        AMapLocationClient.updatePrivacyShow(this, true, true)

        // 用户已同意隐私协议（开发阶段先设为 true）
        MapsInitializer.updatePrivacyAgree(this, true)
        AMapLocationClient.updatePrivacyAgree(this, true)
    }
}