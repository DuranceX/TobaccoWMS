package com.cardy.design.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "inventory")
data class Inventory(
    //库存物品名称
    @ColumnInfo(name = "name")
    var name:String,
    //库存物品型号
    @PrimaryKey
    var model:String,
    //库存物品状态，暂时无用
    @ColumnInfo(name = "state")
    var state:String,
    //库存数量
    @ColumnInfo(name = "hostCount")
    var hostCount:Int,
    //运输中数量
    @ColumnInfo(name = "deliveryCount")
    var deliveryCount:Int,
    //已售出数量
    @ColumnInfo(name = "consumeCount")
    var consumeCount:Int,
    //存放区域
    @ColumnInfo(name = "area")
    var area:String,
    //存放类型，0为原料，1为产品
    @ColumnInfo(name = "type")
    var type:Int,
){
    companion object{
        const val TYPE_MATERIAL:Int=0
        const val TYPE_PRODUCT:Int=1
    }
}
