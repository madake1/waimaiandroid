package com.madake.waimai.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.madake.waimai.R
import com.madake.waimai.databinding.FragmentHomeBinding
import com.madake.waimai.db.entity.Category
import com.madake.waimai.db.entity.GoodsEntity
import com.madake.waimai.mcontext
import com.madake.waimai.ui.adapter.CategoryAdapter
import com.madake.waimai.utils.CustomToast
import com.madake.waimai.utils.TextColorAnimator

class HomeFragment : Fragment() {
    private var animatorPromotion: TextColorAnimator? = null
    private var animatorHot: TextColorAnimator? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val categories = mutableListOf(
        Category("推荐", true),
        Category("手机"),
        Category("数码"),
        Category("食品"),
        Category("零食"),
        Category("鞋包"),
        Category("服饰"),
        Category("水果"),
        Category("蔬菜"),
        Category("家电"),
        Category("电器"),
        Category("美妆"),
        Category("护肤"),
        Category("母婴"),
        Category("运动"),
        Category("户外"),
        Category("家居"),
        Category("图书"),
        Category("文具")
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategoryRecyclerView()
//        setupHorizontalRecyclerViews()
//        setupProductRecyclerView()
        setupSearchBox()
        setupUI()
    }

    private fun setupSearchBox() {
        binding.searchEdit.setOnClickListener {
            // 可以跳转搜索页面或者弹出搜索输入框
        }
    }

    private fun setupCategoryRecyclerView() {
        val adapter = CategoryAdapter(categories) { category, position ->
            // 分类点击逻辑，比如刷新下方横向列表或瀑布流
            CustomToast.show("切换类别！")
        }
        binding.rvCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.adapter = adapter
    }

//    private fun setupHorizontalRecyclerViews() {
//        // 横向 RecyclerView 1
//        binding.rvHorizontal1.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.rvHorizontal1.adapter = HorizontalGoodsAdapter(generateDummyGoods())
//
//        // 横向 RecyclerView 2
//        binding.rvHorizontal2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.rvHorizontal2.adapter = HorizontalGoodsAdapter(generateDummyGoods())
//    }
//
//    private fun setupProductRecyclerView() {
//        binding.rvProduct.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        binding.rvProduct.adapter = ProductAdapter(generateDummyGoods())
//    }

    private fun generateDummyGoods(): List<GoodsEntity> {
        // 随机生成一些商品
        return listOf(
            GoodsEntity(name = "苹果", price = 5.0, stock = 100, category = "水果", description = "新鲜水果", imageUrl = null, salesVolume = 10, createTime = System.currentTimeMillis(), isAvailable = true, sellerId = 1),
            GoodsEntity(name = "面包", price = 3.5, stock = 50, category = "食品", description = "美味面包", imageUrl = null, salesVolume = 20, createTime = System.currentTimeMillis(), isAvailable = true, sellerId = 1),
            GoodsEntity(name = "手机壳", price = 15.0, stock = 200, category = "手机", description = "保护手机", imageUrl = null, salesVolume = 5, createTime = System.currentTimeMillis(), isAvailable = true, sellerId = 1),
        )
    }

    private fun setupUI(){
        // 百亿补贴四个字红黄绿渐变
        animatorPromotion = TextColorAnimator(
            binding.tvPromotion,
            "\uD83D\uDD25百亿补贴\uD83D\uDD25",
            2,
            6,
            listOf(Color.RED, Color.YELLOW, Color.GREEN),
            800L
        )
        animatorPromotion?.start()

        // 热门推荐整段文字蓝紫渐变
        animatorHot = TextColorAnimator(
            binding.tvHot,
            "\uD83D\uDD25热门推荐\uD83D\uDD25",
            2,
            6, // 如果只想前四个字变色，可改
            listOf(Color.BLUE, Color.MAGENTA, Color.CYAN),
            1000L
        )
        animatorHot?.start()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        animatorPromotion?.stop()
        animatorPromotion = null
        animatorHot?.stop()
        animatorHot = null
    }
}
