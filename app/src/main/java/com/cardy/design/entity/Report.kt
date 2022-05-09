package com.cardy.design.entity

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView("SELECT customer, SUM(price) AS price, count(customer) AS times FROM sale_order GROUP BY customer")
data class CustomerAmount(
    val customer:String,
    val price:Double,
    val times:Int
)

@DatabaseView("SELECT supplier, SUM(price) AS price, count(supplier) AS times FROM purchase_order GROUP BY supplier")
data class SupplierAmount(
    val supplier:String,
    val price:Double,
    val times:Int
)

@DatabaseView("SELECT productName,productModel,sum(price) AS price FROM sale_order GROUP BY productModel")
data class ProductSaleAmount(
    val productName:String,
    val productModel:String,
    val price:Double
)

@DatabaseView("SELECT materialName,materialModel,sum(price) AS price FROM purchase_order GROUP BY materialModel")
data class MaterialPurchaseAmount(
    val materialName:String,
    val materialModel:String,
    val price:Double
)

