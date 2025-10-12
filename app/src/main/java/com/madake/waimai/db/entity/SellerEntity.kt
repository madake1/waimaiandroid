package com.madake.waimai.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sellers")
data class SellerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,                // 店铺名称
    val ownerName: String,           // 负责人姓名
    val phone: String,               // 联系电话
    val email: String? = null,       // 邮箱（可选）
    val address: String? = null,     // 地址
    val description: String? = null, // 店铺简介
    val rating: Float = 0f,          // 评分
    val createTime: Long = System.currentTimeMillis(), // 注册时间
    val isActive: Boolean = true   // 是否启用
)
