package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.PurchaseOrder

@Dao
interface PurchaseOrderDao {
    @Insert
    fun insertPurchaseOrder(vararg PurchaseOrder: PurchaseOrder):Long

    @Update
    fun updatePurchaseOrder(vararg PurchaseOrder: PurchaseOrder):Long

    @Delete
    fun deletePurchaseOrder(vararg PurchaseOrder: PurchaseOrder):Long

    @Query("SELECT * FROM purchase_order ORDER BY" +
            "( CASE WHEN state = '申请中' THEN 0 WHEN state = '已拒绝' THEN 1 WHEN state = '运输中' THEN 2 ELSE 3 END )")
    fun getAllPurchaseOrder(): LiveData<List<PurchaseOrder>>
}