package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.cardy.design.entity.PurchaseAmount
import com.cardy.design.entity.PurchaseCount

@Dao
interface ReportDao {
    @Query("SELECT * FROM PurchaseAmount")
    fun getPurchaseAmount():LiveData<List<PurchaseAmount>>

    @Query("SELECT * FROM PurchaseCount")
    fun getPurchaseCount():LiveData<List<PurchaseCount>>
}