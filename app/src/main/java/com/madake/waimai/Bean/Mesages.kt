package com.madake.waimai.Bean

import com.madake.waimai.db.entity.UserEntity

data class RegisterResult(
    val success: Boolean,
    val msg: String
)
data class LoginResult(
    val success: Boolean,
    val msg: String,
    val user: UserEntity? = null
)
