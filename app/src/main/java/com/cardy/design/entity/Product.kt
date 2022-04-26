package com.cardy.design.entity

import android.media.Image
import android.net.Uri
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
    @ColumnInfo(name = "model")
    var model:String,
    //产品图片地址
    @ColumnInfo(name = "image")
    var image: String,
    //产品建议价格
    @ColumnInfo(name = "price")
    var price:Double,
    //产品原料
    @ColumnInfo(name = "usedMaterial")
    var usedMaterial:String

)
