package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.Supplier

@Dao
interface SupplierDao {
    @Insert
    fun insertSupplier(vararg supplier: Supplier):LongArray

    @Update
    fun updateSupplier(vararg supplier: Supplier):Int

    @Delete
    fun deleteSupplier(vararg supplier: Supplier):Int

    @Query("SELECT * FROM supplier ORDER BY priority")
    fun getAllSupplier(): LiveData<List<Supplier>>
}