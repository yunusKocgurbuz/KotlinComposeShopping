package com.yunuskocgurbuz.kotlincomposeshopping.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yunuskocgurbuz.kotlincomposeshopping.dao.ShoppingDao
import com.yunuskocgurbuz.kotlincomposeshopping.entity.ShoppingEntity


@Database(entities = [ShoppingEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase: RoomDatabase() {

    abstract fun ShoppingDao(): ShoppingDao

    companion object{
        @Volatile
        private var INSTANCE: ShoppingDatabase? = null

        fun getInstance(context: Context): ShoppingDatabase{
            synchronized(this){
                return INSTANCE?: Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}