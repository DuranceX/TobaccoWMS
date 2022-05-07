package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.Supplier
import java.lang.Exception
import kotlin.jvm.Throws

@Dao
interface SupplierDao {
    @Insert
    @Throws(Exception::class)
    fun insertSupplier(vararg supplier: Supplier):LongArray

    @Update
    @Throws(Exception::class)
    fun updateSupplier(vararg supplier: Supplier):Int

    @Delete
    fun deleteSupplier(vararg supplier: Supplier):Int

    @Query("SELECT * FROM supplier ORDER BY priority,name")
    fun getAllSupplier(): LiveData<List<Supplier>>

    @Query("SELECT * FROM supplier WHERE name LIKE :arg OR address LIKE :arg OR mainSupply LIKE :arg ORDER BY priority,name")
    fun getAllQueriedSupplier(arg:String): LiveData<List<Supplier>>

    @Query("SELECT name FROM supplier")
    fun getSupplierNameList():List<String>
}