package com.madake.waimai.ui.fragment

import HomeViewModel
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.madake.waimai.databinding.FragmentHomeBinding
import com.madake.waimai.db.entity.Category
import com.madake.waimai.ui.adapter.CategoryAdapter
import com.madake.waimai.ui.adapter.HorizontalGoodsAdapter
import com.madake.waimai.ui.home.ProductAdapter
import com.madake.waimai.utils.CustomToast
import com.madake.waimai.utils.MockDataGenerator
import com.madake.waimai.utils.TextColorAnimator
import com.madake.waimai.utils.singleClick
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by lazy {
        HomeViewModel(requireContext())
    }
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
        setupHorizontalRecyclerViews()
        setupProductRecyclerView()
        setupSearchBox()
        setupUI()
        setData()
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

    private fun setupHorizontalRecyclerViews() {
        val billionAdapter = HorizontalGoodsAdapter(mutableListOf())
        binding.rvHorizontal1.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHorizontal1.adapter = billionAdapter

        val hotAdapter = HorizontalGoodsAdapter(mutableListOf())
        binding.rvHorizontal2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHorizontal2.adapter = hotAdapter

        // 观察 LiveData
        homeViewModel.billionGoods.observe(viewLifecycleOwner) { list ->
            billionAdapter.updateData(list)
        }

        homeViewModel.hotGoods.observe(viewLifecycleOwner) { list ->
            hotAdapter.updateData(list)
        }

        // 滑动到底自动加载下一页
        binding.rvHorizontal1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as LinearLayoutManager
                if (lm.findLastVisibleItemPosition() >= billionAdapter.itemCount - 2) {
                    homeViewModel.loadNextBillionGoods()
                }
            }
        })

        binding.rvHorizontal2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as LinearLayoutManager
                if (lm.findLastVisibleItemPosition() >= hotAdapter.itemCount - 2) {
                    homeViewModel.loadNextHotGoods()
                }
            }
        })

        // 初始加载第一页
        homeViewModel.loadNextBillionGoods()
        homeViewModel.loadNextHotGoods()
    }


    private fun setupProductRecyclerView() {
        binding.rvProduct.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val adapter = ProductAdapter(mutableListOf())
        binding.rvProduct.adapter = adapter

        // 观察 LiveData
        homeViewModel.allGoods.observe(viewLifecycleOwner) { list ->
            adapter.updateData(list)
        }
        binding.rvProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as StaggeredGridLayoutManager
                val lastVisiblePositions = IntArray(lm.spanCount)
                lm.findLastVisibleItemPositions(lastVisiblePositions)
                val lastVisibleItem = lastVisiblePositions.maxOrNull() ?: 0

                if (lastVisibleItem >= adapter.itemCount - 2) {
                    homeViewModel.loadNextAllGoods()
                }
            }
        })

        homeViewModel.loadNextAllGoods()
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
    private fun setData(){
        binding.tvPromotion.singleClick {
            lifecycleScope.launch {
                MockDataGenerator.generateSellers(requireContext(), 5)
                CustomToast.show("生成商家成功")
            }
        }
        binding.tvHot.singleClick {
            lifecycleScope.launch {
                val goods = MockDataGenerator.generateGoods(requireContext(), 20, 5)
                if (goods.isEmpty()) {
                    CustomToast.show("先生成商家")
                } else {
                    CustomToast.show("生成商品成功")
                }
            }
        }
    }
}
