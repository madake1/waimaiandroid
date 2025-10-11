package com.madake.waimai.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.madake.waimai.databinding.ActivityLoginBinding
import com.madake.waimai.utils.CustomToast
import com.madake.waimai.utils.UserSession
import com.madake.waimai.utils.singleClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private var lastFailedPhone: String? = null
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 在 onCreate 中
        var isPasswordVisible = false

        binding.showpsw.singleClick {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                // 显示密码
                binding.psw.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.showpsw.text = "隐藏密码"
            } else {
                // 隐藏密码
                binding.psw.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.showpsw.text = "显示密码"
            }
            // 保持光标在末尾
            binding.psw.setSelection(binding.psw.text?.length ?: 0)
        }


        binding.login.singleClick {
            val phone = binding.phone.text.toString().trim()
            val password = binding.psw.text.toString().trim()

            if (phone.isEmpty() || password.isEmpty()) {
                CustomToast.show("请输入手机号或密码")
                return@singleClick
            }

            lifecycleScope.launch {
                val result = withContext(Dispatchers.IO) {
                    UserRepository.login(phone, password)
                }

                if (result.success && result.user != null) {
                    // 登录成功
                    UserSession.saveLogin(this@LoginActivity, result.user.id)
                    CustomToast.show("登录成功")
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                } else {
                    if(result.msg=="密码不对"){
                        lastFailedPhone = phone
                    }
                    CustomToast.show(result.msg)
                }
            }
        }
        binding.findpsw.singleClick {
            val intent = Intent(this, FindPswActivity::class.java)
            intent.putExtra("phone", lastFailedPhone ?: "")
            startActivity(intent)
        }


        binding.register.singleClick {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }
        binding.clear.singleClick {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    UserRepository.clearUsers()
                }
                CustomToast.show( "用户表已清空")
            }
        }

    }
}