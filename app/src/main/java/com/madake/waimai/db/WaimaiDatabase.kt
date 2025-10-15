package com.madake.waimai.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.madake.waimai.db.dao.GoodsDao
import com.madake.waimai.db.dao.SellerDao
import com.madake.waimai.db.dao.UserDao
import com.madake.waimai.db.entity.GoodsEntity
import com.madake.waimai.db.entity.SellerEntity
import com.madake.waimai.db.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        GoodsEntity::class,
        SellerEntity::class // 如果你还没写 SellerEntity，等会加上
    ],
    version = 3,
    exportSchema = false
)
abstract class WaimaiDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun goodsDao(): GoodsDao
    abstract fun sellerDao(): SellerDao

    companion object {
        @Volatile
        private var INSTANCE: WaimaiDatabase? = null

        fun getInstance(context: Context): WaimaiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WaimaiDatabase::class.java,
                    "waimai_db"
                )
                    .fallbackToDestructiveMigration() // ⚠️开发期推荐加上，防止版本不匹配崩溃
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
