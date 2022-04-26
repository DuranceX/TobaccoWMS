package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.Customer
import java.lang.Exception
import kotlin.jvm.Throws

@Dao
interface CustomerDao {
    @Insert
    @Throws(Exception::class)
    fun insertCustomer(vararg customer: Customer):LongArray

    @Update
    @Throws(Exception::class)
    fun updateCustomer(vararg customer: Customer):Int

    @Delete
    fun deleteCustomer(vararg customer: Customer):Int

    @Query("SELECT * FROM customer ORDER BY priority,name")
    fun getAllCustomer():LiveData<List<Customer>>
}