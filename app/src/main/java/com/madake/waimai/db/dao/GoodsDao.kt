package com.madake.waimai.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.madake.waimai.db.entity.GoodsEntity

@Dao
interface GoodsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoods(goods: GoodsEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoodsList(goodsList: List<GoodsEntity>)

    @Query("SELECT * FROM goods")
    suspend fun getAllGoods(): List<GoodsEntity>
}
