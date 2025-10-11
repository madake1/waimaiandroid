package com.madake.waimai.utils

import android.content.Context

object UserSession {
    private const val PREF_NAME = "user_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_LOGIN_TIME = "login_time"

    fun saveLogin(context: Context, userId: Int) {
        val sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sp.edit()
            .putInt(KEY_USER_ID, userId)
            .putLong(KEY_LOGIN_TIME, System.currentTimeMillis())
            .apply()
    }

    fun getLoggedInUserId(context: Context): Long? {
        val sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val userId = sp.getLong(KEY_USER_ID, -1)
        return if (userId != -1L) userId else null
    }

    fun isSessionValid(context: Context, timeoutMinutes: Long = 1): Boolean {
        val sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val loginTime = sp.getLong(KEY_LOGIN_TIME, -1)
        if (loginTime == -1L) return false
        val elapsed = System.currentTimeMillis() - loginTime
        return elapsed <= timeoutMinutes * 60 * 1000 // 超过设定分钟数则失效
    }

    fun clearSession(context: Context) {
        val sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sp.edit().clear().apply()
    }
}
