package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.SaleOrder

@Dao
interface SaleOrderDao {
    @Insert
    fun insertSaleOrder(vararg saleOrder: SaleOrder):LongArray

    @Update
    fun updateSaleOrder(vararg saleOrder: SaleOrder):Int

    @Delete
    fun deleteSaleOrder(vararg saleOrder: SaleOrder):Int

    @Query("SELECT orderId,userId,user.username AS userName,productName,productModel,count,price,customer,saleDate,deliveryDate,state,comment " +
            "FROM sale_order,user WHERE userId = user.id " +
            "ORDER BY ( CASE WHEN state = '申请中' THEN 0 WHEN state = '已拒绝' THEN 1 WHEN state = '运输中' THEN 2 ELSE 3 END )")
    fun getAllSaleOrder(): LiveData<List<SaleOrder>>
}