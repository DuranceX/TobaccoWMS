package com.cardy.design.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class Customer(
    //客户名称
    @PrimaryKey
    var name:String,
    //客户地址
    @ColumnInfo(name = "address")
    var address:String,
    //客户优先级
    @ColumnInfo(name = "priority")
    var priority:Int,
    //客户头像地址
    @ColumnInfo(name = "logo")
    var logo: String,
    //客户主要购买产品
    @ColumnInfo(name = "mainPurchase")
    var mainPurchase:String,
){
    companion object{
        const val PRIORITY_HIGH:Int=1
        const val PRIORITY_MIDDLE:Int=2
        const val PRIORITY_LOW:Int=3
    }
}
