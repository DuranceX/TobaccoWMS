package com.cardy.design.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supplier")
data class Supplier(
    //供货商名称
    @PrimaryKey
    var name:String,
    //供货商地址
    @ColumnInfo(name = "address")
    var address:String,
    //供货商优先级
    @ColumnInfo(name = "priority")
    var priority:Int,
    //供货商头像地址
    @ColumnInfo(name = "logo")
    var logo:String,
    //客户主要购买产品
    @ColumnInfo(name = "mainSupply")
    var mainSupply:String,
){
    companion object{
        const val PRIORITY_HIGH:Int=1
        const val PRIORITY_MIDDLE:Int=2
        const val PRIORITY_LOW:Int=3
    }
}
