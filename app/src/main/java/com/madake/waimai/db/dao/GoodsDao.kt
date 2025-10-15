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

    @Query("SELECT * FROM goods WHERE isBillionSubsidy = 1 ORDER BY createTime DESC LIMIT :limit OFFSET :offset")
    suspend fun getBillionSubsidyGoods(offset: Int, limit: Int): List<GoodsEntity>

    @Query("SELECT * FROM goods ORDER BY salesVolume DESC LIMIT :limit OFFSET :offset")
    suspend fun getHotGoods(offset: Int, limit: Int): List<GoodsEntity>

    // 所有商品（默认按上架时间或 id 降序，可按需修改排序）
    @Query("SELECT * FROM goods ORDER BY createTime DESC LIMIT :limit OFFSET :offset")
    suspend fun getAllGoods(limit: Int, offset: Int): List<GoodsEntity>

    @Query("DELETE FROM goods")
    suspend fun clearAllGoods()
}
