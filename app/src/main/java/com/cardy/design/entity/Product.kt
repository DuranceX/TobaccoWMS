package com.cardy.design.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    //产品名称
    @ColumnInfo(name = "name")
    var name:String,
    //产品型号
    @PrimaryKey
    var model:String,
    //产品建议价格
    @ColumnInfo(name = "price")
    var price:Float
)
