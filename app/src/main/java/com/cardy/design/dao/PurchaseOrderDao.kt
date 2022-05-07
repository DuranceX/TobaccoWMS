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

    @Query("SELECT * FROM purchase_order ORDER BY ( CASE WHEN state = '申请中' THEN 0 WHEN state = '待发货' THEN 1 WHEN state = '已拒绝' THEN 2 WHEN state = '运输中' THEN 3 ELSE 4 END )")
    fun getAllPurchaseOrder(): LiveData<List<PurchaseOrder>>

    @Query("SELECT * FROM purchase_order " +
            "WHERE orderId LIKE :arg OR userId LIKE :arg OR userName LIKE :arg OR materialName LIKE :arg OR materialModel LIKE :arg OR supplier LIKE :arg OR state LIKE :arg " +
            "ORDER BY ( CASE WHEN state = '申请中' THEN 0 WHEN state = '待发货' THEN 1 WHEN state = '已拒绝' THEN 2 WHEN state = '运输中' THEN 3 ELSE 4 END )")
    fun getAllQueriedPurchaseOrder(arg:String): LiveData<List<PurchaseOrder>>

    @Query("SELECT * from purchase_order WHERE state == :state")
    fun getSelectedStateOrder(state:String):List<PurchaseOrder>
}