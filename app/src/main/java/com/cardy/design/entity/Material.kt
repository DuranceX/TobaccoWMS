package com.cardy.design.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "material")
data class Material(
    //原料名称
    @ColumnInfo(name = "name")
    var name:String,
    //原料型号
    @PrimaryKey
    @ColumnInfo(name = "model")
    var model:String,
    //原料参考价格
    @ColumnInfo(name = "price")
    var price:Float
)
