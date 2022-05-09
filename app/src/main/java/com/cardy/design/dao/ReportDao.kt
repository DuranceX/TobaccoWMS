package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.cardy.design.entity.*

@Dao
interface ReportDao {
    @Query("SELECT * FROM CustomerAmount")
    fun getCustomerAmount():List<CustomerAmount>

    @Query("SELECT * FROM SupplierAmount")
    fun getSupplierAmount():List<SupplierAmount>

    @Query("SELECT * FROM ProductSaleAmount")
    fun getProductSaleAmount():List<ProductSaleAmount>

    @Query("SELECT * FROM MaterialPurchaseAmount")
    fun getMaterialPurchaseAmount():List<MaterialPurchaseAmount>

    @Query("SELECT sum(price) FROM sale_order WHERE saleDate > :date")
    fun getLastMonthSalePrice(date:String):Double

    @Query("SELECT sum(price) FROM purchase_order WHERE purchaseDate > :date")
    fun getLastMonthPurchasePrice(date:String):Double
}