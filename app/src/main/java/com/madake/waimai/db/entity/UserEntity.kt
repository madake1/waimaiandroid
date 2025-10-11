package com.madake.waimai.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
 @PrimaryKey(autoGenerate = true) val id: Int = 0, // 内部自增id
 val phone: String,       // 手机号
 val password: String,    // 密码
 val birthday: String,    // 生日
 val nickName: String,    // 昵称
 val gender : Int,
 val headImg: String,     // 头像URL或本地路径
 val createTime: String  // 创建时间
)
