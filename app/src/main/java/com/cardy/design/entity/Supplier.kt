package com.cardy.design.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supplier")
data class Supplier(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    //供货商名称
    @ColumnInfo(name = "name")
    var name:String = "",
    //供货商地址
    @ColumnInfo(name = "address")
    var address:String="",
    //供货商优先级
    @ColumnInfo(name = "priority")
    var priority:Int=3,
    //供货商头像地址
    @ColumnInfo(name = "logo")
    var logo:String="",
    //供货商主要供应原料
    @ColumnInfo(name = "mainSupply")
    var mainSupply:String="",
){
    constructor(name: String,address: String,priority: Int,logo: String,mainSupply: String):this(){
        this.name = name
        this.address = address
        this.priority = priority
        this.logo = logo
        this.mainSupply = mainSupply
    }

    companion object{
        const val PRIORITY_HIGH:Int=1
        const val PRIORITY_MIDDLE:Int=2
        const val PRIORITY_LOW:Int=3
    }
}
