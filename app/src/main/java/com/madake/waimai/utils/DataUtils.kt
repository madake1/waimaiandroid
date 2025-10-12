package com.madake.waimai.utils

import com.madake.waimai.db.dao.GoodsDao
import com.madake.waimai.db.dao.SellerDao
import com.madake.waimai.db.entity.GoodsEntity
import com.madake.waimai.db.entity.SellerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

object MockDataGenerator {

    // 商家名集合，多一些
    private val sellerNames = listOf(
        "红酒坊", "水果屋", "时尚鞋包", "美味食品", "电子数码",
        "零食铺", "家居用品", "潮流服饰", "母婴乐园", "户外运动馆",
        "图书书城", "文具铺子", "美妆工坊", "护肤馆", "家电专卖"
    )

    private val ownerNames = listOf(
        "张三", "李四", "王五", "赵六", "钱七", "孙八",
        "周九", "吴十", "郑十一", "冯十二"
    )

    // 商品类别
    private val categories = listOf(
        "推荐", "手机", "数码", "食品", "零食", "鞋包",
        "服饰", "水果", "蔬菜", "家电", "电器", "美妆",
        "护肤", "母婴", "运动", "户外", "家居", "图书", "文具"
    )

    /**
     * 随机生成商家并插入数据库
     */
    suspend fun generateSellers(sellerDao: SellerDao, count: Int = 20) = withContext(Dispatchers.IO) {
        val sellers = mutableListOf<SellerEntity>()
        repeat(count) {
            val name = sellerNames.random() + Random.nextInt(1, 1000) // 保证名称不重复
            val seller = SellerEntity(
                name = name,
                ownerName = ownerNames.random(),
                phone = "1${Random.nextInt(300000000, 999999999)}",
                rating = Random.nextFloat() * 5,
                isActive = true
            )
            sellers.add(seller)
        }
        sellerDao.insertSellers(sellers)
        sellers
    }

    /**
     * 随机生成商品并插入数据库
     * @param goodsDao: 商品 Dao
     * @param sellers: 已存在的商家列表
     * @param count: 商品数量
     * @param imageCount: assets/images 下图片数量
     */
    suspend fun generateGoods(
        goodsDao: GoodsDao,
        sellers: List<SellerEntity>,
        count: Int = 100,
        imageCount: Int = 216
    ) = withContext(Dispatchers.IO) {
        val goodsList = mutableListOf<GoodsEntity>()
        repeat(count) {
            val seller = sellers.random()
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
                sellerId = seller.id,
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
