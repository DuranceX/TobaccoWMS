package com.cardy.design.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date


@Entity(tableName = "sale_order")
data class SaleOrder(
    //订单号
    @PrimaryKey(autoGenerate = true)
    val orderId:Int=1,
    //售出产品名称
    @ColumnInfo(name = "productName")
    var productName:String,
    //售出产品型号
    @ColumnInfo(name = "productModel")
    var productModel:String,
    //销售数量
    @ColumnInfo(name = "count")
    var count:Int,
    //销售总价格
    @ColumnInfo(name = "price")
    var price:Float,
    //客户名称
    @ColumnInfo(name = "customer")
    var customer:String,
    //销售日期
    @ColumnInfo(name = "saleDate")
    var saleDate: Date,
    //预计送达日期
    @ColumnInfo(name = "deliveryDate")
    var deliveryDate: Date,
    //订单状态
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
