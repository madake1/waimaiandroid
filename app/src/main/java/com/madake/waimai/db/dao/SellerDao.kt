package com.madake.waimai.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.madake.waimai.db.entity.SellerEntity

@Dao
interface SellerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeller(seller: SellerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSellers(sellers: List<SellerEntity>)

    @Query("SELECT * FROM sellers")
    suspend fun getAllSellers(): List<SellerEntity>

    // 查询 n 个商家，如果 n > 总数，则返回全部
    @Query("SELECT * FROM sellers LIMIT :n")
    suspend fun getNSellers(n: Int): List<SellerEntity>

    // 查询所有商家的 ID
    @Query("SELECT id FROM sellers")
    suspend fun getAllSellerIds(): List<Long>

    // 根据 ID 列表批量查询商家实体
    @Query("SELECT * FROM sellers WHERE id IN (:ids)")
    suspend fun getSellersByIds(ids: List<Long>): List<SellerEntity>
}
