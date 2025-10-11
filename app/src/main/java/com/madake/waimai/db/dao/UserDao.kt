package com.madake.waimai.db.dao


import androidx.room.*
import com.madake.waimai.db.entity.UserEntity

@Dao
interface UserDao {

    // 插入新用户，如果主键冲突则替换
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    // 更新用户信息
    @Update
    suspend fun update(user: UserEntity)


    // 根据手机号查用户（注册检查重复用）
    @Query("SELECT * FROM user WHERE phone = :phone LIMIT 1")
    suspend fun getUserByPhone(phone: String): UserEntity?

    @Query("DELETE FROM user")
    suspend fun clearAllUsers()
}

