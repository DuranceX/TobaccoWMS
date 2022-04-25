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
    //经办人ID
    @ColumnInfo(name = "userId")
    var userId:String = "",
    //经办人名称
    @ColumnInfo(name = "userName")
    var userName:String = "",
    //售出产品名称
    @ColumnInfo(name = "productName")
    var productName:String = "",
    //售出产品型号
    @ColumnInfo(name = "productModel")
    var productModel:String = "",
    //销售数量
    @ColumnInfo(name = "count")
    var count:Int = 0,
    //销售总价格
    @ColumnInfo(name = "price")
    var price:Double = 0.0,
    //客户名称
    @ColumnInfo(name = "customer")
    var customer:String = "",
    //销售日期
    @ColumnInfo(name = "saleDate")
    var saleDate: String = "",
    //预计送达日期
    @ColumnInfo(name = "deliveryDate")
    var deliveryDate: String ="",
    //订单状态
    @ColumnInfo(name = "state")
    var state:String = "",
    //管理员批注
    @ColumnInfo(name = "comment")
    var comment:String = ""
){
    constructor(productName: String,productModel: String,count: Int,price: Double,customer: String,saleDate: String,deliveryDate: String,state: String,comment: String):this(){
        this.productName = productName
        this.productModel = productModel
        this.count = count
        this.price = price
        this.customer = customer
        this.saleDate = saleDate
        this.deliveryDate = deliveryDate
        this.state = state
        this.comment = comment
    }

    companion object{
        const val STATE_REQUEST:String="申请中"
        const val STATE_DELIVERY:String="运输中"
        const val STATE_REFUSED:String="已拒绝"
        const val STATE_COMPLETE:String="已完成"
    }
}
