package com.madake.waimai.ui.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.madake.waimai.R
import com.madake.waimai.databinding.ActivityRegisterBinding
import com.madake.waimai.Bean.UserDTO
import com.madake.waimai.mcontext
import com.madake.waimai.utils.CustomToast
import com.madake.waimai.utils.UserSession
import com.madake.waimai.utils.singleClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.birthday.singleClick {
            // 获取今天的日期
            val calendar = Calendar.getInstance()

            // 弹出日期选择器
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    // month 从 0 开始，所以要 +1
                    val selectedDate = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }

                    // 格式化日期
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val dateStr = sdf.format(selectedDate.time)

                    // 设置 EditText 内容
                    binding.birthday.setText(dateStr)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePicker.show()
        }

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
        binding.register.singleClick {
            val phone = binding.phone.text.toString().trim()
            val nickname = binding.nickname.text.toString().trim()
            val password = binding.psw.text.toString().trim()
            val birthday = binding.birthday.text.toString().trim()
            val genderId = binding.gender.checkedRadioButtonId
            val headImg = binding.avator.tag as? String ?: ""
            if (phone.isEmpty() || nickname.isEmpty() || password.isEmpty() || birthday.isEmpty() || genderId == -1) {
                CustomToast.show("请完整填写信息")
                return@singleClick
            }
            if (!phone.matches(Regex("\\d{11}"))) {
                CustomToast.show( "请填写11位手机号")
                return@singleClick
            }
            if(nickname.length>6){
                CustomToast.show("昵称不能超过6个字")
                return@singleClick
            }
            if(password.length !in 6..16){
                CustomToast.show("密码需要6到16位")
                return@singleClick
            }
            // 获取性别
            val gender = when (genderId) {
                R.id.rb_male -> 1
                R.id.rb_female -> 2
                else -> 0
            }
            // 插入数据库
            // 构造 DTO
            val dto = UserDTO(
                phone = phone,
                password = password,
                birthday = birthday,
                nickName = nickname,
                headImg = headImg, // 这里取默认头像或用户自定义
                gender = genderId
            )
            lifecycleScope.launch {
                // 在 IO 线程执行数据库操作
                val result = withContext(Dispatchers.IO) {
                    UserRepository.register(dto)
                }
                // 回到主线程显示 Toast
                CustomToast.show(result.msg)

                if (result.success) {
                    // 保存当前用户 ID（假设 UserRepository.register 返回的结果里包含新插入的用户 ID）
                    val userId = withContext(Dispatchers.IO) {
                        UserRepository.getUserIdByPhone(dto.phone)
                    }
                    userId?.let { UserSession.saveLogin(this@RegisterActivity, it) }

                    // 跳转到主页
                    startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                    finish()
                }
            }


        }

    }
}