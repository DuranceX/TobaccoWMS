package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.Supplier

@Dao
interface SupplierDao {
    @Insert
    fun insertSupplier(vararg supplier: Supplier):Long

    @Update
    fun updateSupplier(vararg supplier: Supplier):Long

    @Delete
    fun deleteSupplier(vararg supplier: Supplier):Long

    @Query("SELECT * FROM supplier")
    fun getAllSupplier(): LiveData<List<Supplier>>
}