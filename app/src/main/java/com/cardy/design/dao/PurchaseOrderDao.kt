package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.PurchaseOrder

@Dao
interface PurchaseOrderDao {
    @Insert
    fun insertPurchaseOrder(vararg PurchaseOrder: PurchaseOrder):LongArray

    @Update
    fun updatePurchaseOrder(vararg PurchaseOrder: PurchaseOrder):Int

    @Delete
    fun deletePurchaseOrder(vararg PurchaseOrder: PurchaseOrder):Int

    @Query("SELECT orderId,userId,user.username AS userName,materialName,materialModel,count,price,supplier,purchaseDate,deliveryDate,state,comment " +
            "FROM purchase_order,user WHERE userId = user.id " +
            "ORDER BY ( CASE WHEN state = '申请中' THEN 0 WHEN state = '已拒绝' THEN 1 WHEN state = '运输中' THEN 2 ELSE 3 END )")
    fun getAllPurchaseOrder(): LiveData<List<PurchaseOrder>>
}