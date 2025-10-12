package com.madake.waimai.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.madake.waimai.R
import com.madake.waimai.databinding.ActivityHomeBinding
import com.madake.waimai.ui.fragment.HomeFragment
import com.madake.waimai.ui.fragment.MessageFragment
import com.madake.waimai.utils.MockDataGenerator
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        selectTab(R.id.nav_home)
        binding.navHome.setOnClickListener { selectTab(R.id.nav_home) }
        binding.navMessage.setOnClickListener { selectTab(R.id.nav_message) }
        binding.navCart.setOnClickListener { selectTab(R.id.nav_cart) }
        binding.navProfile.setOnClickListener { selectTab(R.id.nav_profile) }
    }

private fun selectTab(tabId: Int) {
    // 重置所有按钮
    listOf(
        Pair(binding.iconHome, binding.textHome),
        Pair(binding.iconMessage, binding.textMessage),
        Pair(binding.iconCart, binding.textCart),
        Pair(binding.iconProfile, binding.textProfile)
    ).forEach {
        it.first.setColorFilter(ContextCompat.getColor(this, R.color.gray2))
        it.second.setTextColor(ContextCompat.getColor(this, R.color.gray2))
    }

    // 设置选中按钮
    when(tabId) {
        R.id.nav_home -> {
            binding.iconHome.setColorFilter(ContextCompat.getColor(this, R.color.blue))
            binding.textHome.setTextColor(ContextCompat.getColor(this, R.color.blue))
            replaceFragment(HomeFragment())
        }
        R.id.nav_message -> {
            binding.iconMessage.setColorFilter(ContextCompat.getColor(this, R.color.blue))
            binding.textMessage.setTextColor(ContextCompat.getColor(this, R.color.blue))
                replaceFragment(MessageFragment())
        }
        R.id.nav_cart -> {
            binding.iconCart.setColorFilter(ContextCompat.getColor(this, R.color.blue))
            binding.textCart.setTextColor(ContextCompat.getColor(this, R.color.blue))
//                replaceFragment(CartFragment())
        }
        R.id.nav_profile -> {
            binding.iconProfile.setColorFilter(ContextCompat.getColor(this, R.color.blue))
            binding.textProfile.setTextColor(ContextCompat.getColor(this, R.color.blue))
//                replaceFragment(ProfileFragment())
        }
    }
}

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment) // content 是你 FragmentContainerView 的 id
            .commit()
    }



}