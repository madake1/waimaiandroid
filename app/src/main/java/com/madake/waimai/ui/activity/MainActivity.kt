package com.madake.waimai.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.madake.waimai.databinding.ActivityMainBinding
import com.madake.waimai.utils.UserSession

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val totalSeconds = 5
    private var secondsLeft = totalSeconds
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        binding.skip.setOnClickListener {
            goToHome()
        }
        startCountdown()

        binding.tb.setOnClickListener {
            pauseCountdown()
            try {
                // 1️⃣ 尝试打开手机中的淘宝 App
                val uri = Uri.parse("taobao://m.taobao.com")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } catch (e: Exception) {
                // 2️⃣ 如果没装淘宝，就打开网页版
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://m.taobao.com"))
                startActivity(webIntent)
            }
        }

    }
    private fun startCountdown() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(secondsLeft * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                secondsLeft = (millisUntilFinished / 1000 + 1).toInt()
                binding.tvNumber.text = "$secondsLeft"
            }

            override fun onFinish() {
                goToHome()
            }
        }.start()
    }
    private fun pauseCountdown() {
        countDownTimer?.cancel()
        countDownTimer = null
    }


    private fun goToHome() {
        if (!UserSession.isSessionValid(this, timeoutMinutes = 1)) {
            // 超时，需要重新登录
            UserSession.clearSession(this)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
    override fun onResume() {
        super.onResume()
        startCountdown()  // 恢复倒计时
    }

    override fun onPause() {
        super.onPause()
        pauseCountdown()  // 暂停倒计时
    }


}