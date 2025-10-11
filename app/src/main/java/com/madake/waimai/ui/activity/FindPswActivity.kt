package com.madake.waimai.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.madake.waimai.R
import com.madake.waimai.databinding.ActivityFindPswBinding
import com.madake.waimai.utils.CustomToast
import com.madake.waimai.utils.singleClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class FindPswActivity : AppCompatActivity() {
    private var captchaAnswer: Int = 0
    private lateinit var binding: ActivityFindPswBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFindPswBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val phone = intent.getStringExtra("phone")
        if(phone!=null) binding.phone.setText(phone) // 默认填充

        // 点击获取验证码
        binding.check2.singleClick {
            val a = Random.nextInt(1, 10)
            val b = Random.nextInt(1, 10)
            captchaAnswer = a + b
            binding.check2.text = "$a+$b=?"
        }

        // 显示/隐藏密码
        var isPswVisible = false
        binding.showpsw.singleClick {
            isPswVisible = !isPswVisible
            binding.psw.inputType =
                if (isPswVisible) android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        // 点击确认修改密码
        binding.login.singleClick {
            val inputCaptcha = binding.check1.text.toString().trim()
            val newPassword = binding.psw.text.toString().trim()
            val userPhone = binding.phone.text.toString().trim()

            if (inputCaptcha.isEmpty() || newPassword.isEmpty()) {
                CustomToast.show("请填写验证码和新密码")
                return@singleClick
            }

            if (inputCaptcha.toIntOrNull() != captchaAnswer) {
                CustomToast.show("验证码错误")
                return@singleClick
            }

            // 修改数据库密码
            lifecycleScope.launch {
                val success = withContext(Dispatchers.IO) {
                    UserRepository.updatePassword(userPhone, newPassword)
                }

                if (success) {
                    CustomToast.show("修改成功")
                    // 跳转首页
                    startActivity(Intent(this@FindPswActivity, HomeActivity::class.java))
                    finish()
                } else {
                    CustomToast.show("该账号不存在")
                }
            }
        }
    }

}
