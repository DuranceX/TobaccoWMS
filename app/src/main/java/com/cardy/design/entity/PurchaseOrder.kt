package com.cardy.design.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date


@Entity(tableName = "purchase_order")
data class PurchaseOrder(
    //订单号
    @PrimaryKey(autoGenerate = true)
    val orderId:Int=1,
    //采购原料名称
    @ColumnInfo(name = "materialName")
    var materialName:String,
    //采购原料型号
    @ColumnInfo(name = "materialModel")
    var materialModel:String,
    //采购数量
    @ColumnInfo(name = "count")
    var count:Int,
    //总价格，默认为参考价*数量
    @ColumnInfo(name = "price")
    var price:Float,
    //供货商
    @ColumnInfo(name = "supplier")
    var supplier:String,
    //购买日期
    @ColumnInfo(name = "purchaseDate")
    var purchaseDate: Date,
    //预计送达日期
    @ColumnInfo(name = "deliveryDate")
    var deliveryDate: Date,
    //当前状态，”申请中“，”运输中“，”已拒绝“，”已完成“
    @ColumnInfo(name = "state")
    var state:String,
    //管理员批注
    @ColumnInfo(name = "comment")
    var comment:String
){
    companion object{
        const val STATE_REQUEST:String="申请中"
        const val STATE_DELIVERY:String="运输中"
        const val STATE_REFUSED:String="已拒绝"
        const val STATE_COMPLETE:String="已完成"
    }
}
