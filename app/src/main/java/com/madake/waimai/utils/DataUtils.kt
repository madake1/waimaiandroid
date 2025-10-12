package com.madake.waimai.utils

import android.content.Context
import com.madake.waimai.db.WaimaiDatabase
import com.madake.waimai.db.entity.GoodsEntity
import com.madake.waimai.db.entity.SellerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

object MockDataGenerator {

    private val sellerNames = listOf(
        "红酒坊", "水果屋", "时尚鞋包", "美味食品", "电子数码",
        "零食铺", "家居用品", "潮流服饰", "母婴乐园", "户外运动馆",
        "图书书城", "文具铺子", "美妆工坊", "护肤馆", "家电专卖",
        "茶叶馆", "运动馆", "厨房用品", "宠物用品", "摄影器材"
    )

    private val ownerNames = listOf(
        "张三", "李四", "王五", "赵六", "钱七", "孙八",
        "周九", "吴十", "郑十一", "冯十二"
    )

    private val categories = listOf(
        "推荐", "手机", "数码", "食品", "零食", "鞋包",
        "服饰", "水果", "蔬菜", "家电", "电器", "美妆",
        "护肤", "母婴", "运动", "户外", "家居", "图书", "文具"
    )

    // 内部持有数据库 DAO
    private fun sellerDao(context: Context) = WaimaiDatabase.getInstance(context).sellerDao()
    private fun goodsDao(context: Context) = WaimaiDatabase.getInstance(context).goodsDao()

    /** 生成 n 个商家 */
    suspend fun generateSellers(context: Context, n: Int): List<SellerEntity> = withContext(Dispatchers.IO) {
        val dao = sellerDao(context)
        val sellers = mutableListOf<SellerEntity>()
        repeat(n) {
            val name = sellerNames.random() + Random.nextInt(1, 1000)
            val seller = SellerEntity(
                name = name,
                ownerName = ownerNames.random(),
                phone = "1${Random.nextInt(300000000, 999999999)}",
                rating = Random.nextFloat() * 5,
                isActive = true
            )
            sellers.add(seller)
        }
        dao.insertSellers(sellers)
        sellers
    }

    /** 生成 n1 个商品，并分配给 n2 个商家 */
    suspend fun generateGoods(
        context: Context,
        n1: Int,
        n2: Int,
        imageCount: Int = 216
    ): List<GoodsEntity> = withContext(Dispatchers.IO) {
        val goodsDao = goodsDao(context)
        val sellerDao = sellerDao(context)

        // 获取数据库中所有商家 ID
        val allIds = sellerDao.getAllSellerIds()
        if (allIds.isEmpty()) return@withContext emptyList<GoodsEntity>()

        // 随机选择 n2 个商家 ID，如果不足 n2 就全部
        val selectedIds = if (allIds.size >= n2) allIds.shuffled().take(n2) else allIds

        // 开始生成商品
        val goodsList = mutableListOf<GoodsEntity>()
        repeat(n1) {
            val sellerId = selectedIds.random()
            val price = Random.nextDouble(10.0, 999.0)
            val isBillion = Random.nextBoolean()
            val isToday = Random.nextBoolean()
            val sellerDiscount = Random.nextDouble(0.0, price)
            val billionSubsidy = if (isBillion) Random.nextDouble(0.0, price) else 0.0
            val category = categories.random()
            val imageIndex = Random.nextInt(1, imageCount + 1)
            val imageUrl = "images/img$imageIndex.png"

            val goods = GoodsEntity(
                name = "$category 商品${Random.nextInt(1,1000)}",
                price = price,
                stock = Random.nextInt(10, 500),
                category = category,
                description = "这是一个 $category 商品",
                imageUrl = imageUrl,
                salesVolume = Random.nextInt(0, 1000),
                createTime = System.currentTimeMillis(),
                isAvailable = true,
                sellerId = sellerId,
                isBillionSubsidy = isBillion,
                billionSubsidyAmount = billionSubsidy,
                isTodayRecommend = isToday,
                sellerDiscount = sellerDiscount
            )
            goodsList.add(goods)
        }

        goodsDao.insertGoodsList(goodsList)
        goodsList
    }
}
