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
    var model:String,
    //产品图片地址
    var imageUri: Uri,
    //产品建议价格
    @ColumnInfo(name = "price")
    var price:Float,
    //产品原料
    @ColumnInfo(name = "usedMaterial")
    var usedMaterial:Array<String>

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (name != other.name) return false
        if (model != other.model) return false
        if (price != other.price) return false
        if (!usedMaterial.contentEquals(other.usedMaterial)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + model.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + usedMaterial.contentHashCode()
        return result
    }
}
