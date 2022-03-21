package com.yunuskocgurbuz.kotlincomposeshopping.entity

import androidx.room.*


@Entity(tableName = "shoppingData")
data class ShoppingEntity(
    @ColumnInfo(name = "itemName")
    var itemName: String?,

    @ColumnInfo(name = "itemCount")
    var itemCount: Int?,

    @ColumnInfo(name = "date")
    var date: String?

) {
    @PrimaryKey(autoGenerate = true)
    var uuId : Int = 0
}
