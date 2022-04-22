package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.Customer

@Dao
interface CustomerDao {
    @Insert
    fun insertCustomer(vararg customer: Customer):LongArray

    @Update
    fun updateCustomer(vararg customer: Customer):Int

    @Delete
    fun deleteCustomer(vararg customer: Customer):Int

    @Query("SELECT * FROM customer")
    fun getAllCustomer():LiveData<List<Customer>>
}