package com.yunuskocgurbuz.kotlincomposeshopping.repository

import androidx.lifecycle.LiveData
import com.yunuskocgurbuz.kotlincomposeshopping.dao.ShoppingDao
import com.yunuskocgurbuz.kotlincomposeshopping.entity.ShoppingEntity

class ShoppingSQLiteRepository(private  val shoppingDao: ShoppingDao) {

    val readAllShopping: LiveData<List<ShoppingEntity>> = shoppingDao.getAllShopping()

    suspend fun addShopping(item: ShoppingEntity){
        shoppingDao.insertShopping(item)
    }

}