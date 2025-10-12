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
}
