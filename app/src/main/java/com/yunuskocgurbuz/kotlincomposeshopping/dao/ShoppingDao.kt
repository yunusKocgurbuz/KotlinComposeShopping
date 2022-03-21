package com.yunuskocgurbuz.kotlincomposeshopping.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yunuskocgurbuz.kotlincomposeshopping.entity.ShoppingEntity

@Dao
interface ShoppingDao {

    @Query("SELECT * FROM shoppingData")
    fun getAllShopping(): LiveData<List<ShoppingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopping(item: ShoppingEntity)

}