package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.PurchaseOrder
import java.lang.Exception
import kotlin.jvm.Throws

@Dao
interface PurchaseOrderDao {
    @Insert
    @Throws(Exception::class)
    fun insertPurchaseOrder(vararg PurchaseOrder: PurchaseOrder):LongArray

    @Update
    @Throws(Exception::class)
    fun updatePurchaseOrder(vararg PurchaseOrder: PurchaseOrder):Int

    @Delete
    fun deletePurchaseOrder(vararg PurchaseOrder: PurchaseOrder):Int

    @Query("SELECT * FROM purchase_order ORDER BY ( CASE WHEN state = '申请中' THEN 0 WHEN state = '已拒绝' THEN 1 WHEN state = '运输中' THEN 2 ELSE 3 END )")
    fun getAllPurchaseOrder(): LiveData<List<PurchaseOrder>>

    @Query("SELECT * from purchase_order")
    fun getAllPurchaseOrderTest():LiveData<List<PurchaseOrder>>
}