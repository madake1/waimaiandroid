package com.madake.waimai.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.madake.waimai.db.dao.UserDao
import com.madake.waimai.db.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class WaimaiDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        @Volatile
        private var INSTANCE: WaimaiDatabase? = null

        fun getInstance(context: Context): WaimaiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WaimaiDatabase::class.java,
                    "waimai_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}