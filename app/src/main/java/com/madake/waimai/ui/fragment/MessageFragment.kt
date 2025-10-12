package com.madake.waimai.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.madake.waimai.R
import com.madake.waimai.utils.CustomToast
import com.madake.waimai.utils.MockDataGenerator
import com.madake.waimai.utils.load  // 确保扩展函数导入
import com.madake.waimai.utils.singleClick
import kotlinx.coroutines.launch

class MessageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imgView: ImageView = view.findViewById(R.id.img)

        imgView.singleClick {
            lifecycleScope.launch {
                if (MockDataGenerator.clearAll(requireContext())) {
                    CustomToast.show("已清空所有商家和商品")
                }
            }
        }


        val drawableName = "img150"  // drawable 里图片的名字，不带后缀
        try {
            imgView.load(drawableName)
            Log.d("madake1", "尝试加载图片: $drawableName")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("madake1", "加载图片失败")
        }
    }
}
