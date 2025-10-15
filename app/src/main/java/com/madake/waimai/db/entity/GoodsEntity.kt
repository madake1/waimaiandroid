package com.madake.waimai.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goods")
data class GoodsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Double,
    val stock: Int,
    val category: String?,
    val description: String?,
    val imageUrl: String?,
    val salesVolume: Int,
    val createTime: Long,
    val isAvailable: Boolean,
    val sellerId: Long,

    val zhekou: Double = 1.0,

    val isBillionSubsidy: Boolean = false, // 是否百亿补贴

    val sellerDiscount: Double = 0.0,
    val isPostPay: Boolean = false,       // 是否先用后付
    val isDiscounted: Boolean = false     // 是否参加全场打折
)
