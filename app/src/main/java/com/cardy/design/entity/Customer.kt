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
    @ColumnInfo(name = "manager_green")
    var avatar: Uri,
    //客户主要购买产品
    @ColumnInfo(name = "mainPurchase")
    var mainPurchase:Array<String>,
){
    companion object{
        const val PRIORITY_HIGH:Int=1
        const val PRIORITY_MIDDLE:Int=2
        const val PRIORITY_LOW:Int=3
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Customer

        if (name != other.name) return false
        if (address != other.address) return false
        if (priority != other.priority) return false
        if (avatar != other.avatar) return false
        if (!mainPurchase.contentEquals(other.mainPurchase)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + priority
        result = 31 * result + avatar.hashCode()
        result = 31 * result + mainPurchase.contentHashCode()
        return result
    }
}
