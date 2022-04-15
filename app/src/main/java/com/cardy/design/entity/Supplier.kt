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
    @ColumnInfo(name = "avatar")
    var avatar:String,
    //客户主要购买产品
    @ColumnInfo(name = "mainSupply")
    var mainSupply:Array<String>,
){
    companion object{
        const val PRIORITY_HIGH:Int=1
        const val PRIORITY_MIDDLE:Int=2
        const val PRIORITY_LOW:Int=3
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Supplier

        if (name != other.name) return false
        if (address != other.address) return false
        if (priority != other.priority) return false
        if (avatar != other.avatar) return false
        if (!mainSupply.contentEquals(other.mainSupply)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + priority
        result = 31 * result + avatar.hashCode()
        result = 31 * result + mainSupply.contentHashCode()
        return result
    }
}
