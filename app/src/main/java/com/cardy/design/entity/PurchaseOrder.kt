package com.cardy.design.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.sql.Date


@Entity(tableName = "purchase_order")
data class PurchaseOrder(
    //订单号
        @PrimaryKey(autoGenerate = true)
        val orderId:Int=0,
    //经办人ID
        @ColumnInfo(name = "userId")
        var userId:String = "",
    //经办人名称
        @ColumnInfo(name = "userName")
        var userName:String = "",
    //采购原料名称
        @ColumnInfo(name = "materialName")
        var materialName:String = "",
    //采购原料型号
        @ColumnInfo(name = "materialModel")
        var materialModel:String = "",
    //采购数量
        @ColumnInfo(name = "count")
        var count:Int = 0,
    //总价格，默认为参考价*数量
        @ColumnInfo(name = "price")
        var price:Double = 0.0,
    //供货商
        @ColumnInfo(name = "supplier")
        var supplier:String = "",
    //购买日期
        @ColumnInfo(name = "purchaseDate")
        var purchaseDate: String = "",
    //预计送达日期
        @ColumnInfo(name = "deliveryDate")
        var deliveryDate: String = "",
    //当前状态，”申请中“，”运输中“，”已拒绝“，”已完成“
        @ColumnInfo(name = "state")
        var state:String = "",
    //管理员批注
        @ColumnInfo(name = "comment")
        var comment:String = ""
){
    constructor(materialName: String,materialModel: String,count: Int,price: Double,supplier: String,purchaseDate: String,deliveryDate: String,state: String,comment: String):this(){
        this.materialName = materialName
        this.materialModel = materialModel
        this.count = count
        this.price = price
        this.supplier = supplier
        this.purchaseDate = purchaseDate
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

    override fun toString(): String {
        return "订单号=$orderId, 名称=$materialName, 型号=$materialModel"
    }
}
