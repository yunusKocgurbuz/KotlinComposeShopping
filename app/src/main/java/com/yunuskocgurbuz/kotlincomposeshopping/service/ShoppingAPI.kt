package com.yunuskocgurbuz.kotlincomposeshopping.service


import com.yunuskocgurbuz.kotlincomposeshopping.model.ShoppingModel
import retrofit2.http.GET



interface ShoppingAPI {

    //@Headers("Content-Type: application/json")
    @GET("ShoppingList.json")
    suspend fun getShoppingList(): ShoppingModel


}