package com.cardy.design.entity

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
    var priority:Int
){
    companion object{
        const val PRIORITY_HIGH:Int=1
        const val PRIORITY_MIDDLE:Int=2
        const val PRIORITY_LOW:Int=3
    }
}
