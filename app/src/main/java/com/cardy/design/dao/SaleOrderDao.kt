package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.PurchaseOrder
import com.cardy.design.entity.SaleOrder
import java.lang.Exception
import kotlin.jvm.Throws

@Dao
interface SaleOrderDao {
    @Insert
    @Throws(Exception::class)
    fun insertSaleOrder(vararg saleOrder: SaleOrder):LongArray

    @Update
    @Throws(Exception::class)
    fun updateSaleOrder(vararg saleOrder: SaleOrder):Int

    @Delete
    fun deleteSaleOrder(vararg saleOrder: SaleOrder):Int

    @Query("SELECT * FROM sale_order ORDER BY ( CASE WHEN state = '申请中' THEN 0 WHEN state = '已拒绝' THEN 1 WHEN state = '运输中' THEN 2 ELSE 3 END )")
    fun getAllSaleOrder(): LiveData<List<SaleOrder>>

    @Query("SELECT * from sale_order WHERE state == :state")
    fun getSelectedStateOrder(state:String):List<SaleOrder>
}