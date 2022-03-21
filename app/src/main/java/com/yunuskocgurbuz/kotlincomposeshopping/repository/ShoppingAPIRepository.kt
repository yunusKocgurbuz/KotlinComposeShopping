package com.yunuskocgurbuz.kotlincomposeshopping.repository

import com.yunuskocgurbuz.kotlincomposeimageapp.util.Resource
import com.yunuskocgurbuz.kotlincomposeshopping.model.Category
import com.yunuskocgurbuz.kotlincomposeshopping.model.ShoppingModel
import com.yunuskocgurbuz.kotlincomposeshopping.service.ShoppingAPI
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ShoppingAPIRepository @Inject constructor(
    private val api: ShoppingAPI
){
    suspend fun getShoppingList(): Resource<ShoppingModel> {
        val response = try {
            api.getShoppingList()

        }catch (e: Exception){


            return Resource.Error("Error API connect!!!" + e.toString())

        }

        return Resource.Success(response)
    }

}