package com.cardy.design.entity

import androidx.room.DatabaseView

@DatabaseView("SELECT customer, SUM(price) AS price FROM sale_order GROUP BY customer ORDER BY SUM(price) DESC")
data class PurchaseAmount(
    val customer:String,
    val price:Float
)

@DatabaseView("SELECT customer, count(customer) AS times FROM sale_order GROUP BY customer ORDER BY count(customer) DESC")
data class PurchaseCount(
    val customer: String,
    val times:Int
)
