package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.Inventory

@Dao
interface InventoryDao {
    @Insert
    fun insertInventory(vararg inventory: Inventory):LongArray

    @Update
    fun updateInventory(vararg inventory: Inventory):Int

    @Delete
    fun deleteInventory(vararg inventory: Inventory):Int

    @Query("SELECT * FROM inventory Where type = :type")
    fun getAllSelectedInventory(type:Int): LiveData<List<Inventory>>
}