package com.madake.waimai.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goods")
data class GoodsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Double,
    val stock: Int, // 库存数量
    val category: String?, // 分类
    val description: String?,
    val imageUrl: String?,
    val salesVolume: Int,
    val createTime: Long, // 上架时间
    val isAvailable: Boolean, // 是否上架
    val sellerId: Long, // 商家ID
    val zhekou: Double = 1.0,
    val isBillionSubsidy: Boolean = false, // 是否百亿补贴
    val billionSubsidyAmount: Double = 0.0, // 百亿补贴金额，小于价格
    val isTodayRecommend: Boolean = false, // 是否今日推荐
    val sellerDiscount: Double = 0.0 // 商家优惠金额，小于价格
)
