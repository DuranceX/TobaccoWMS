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

    @Query("SELECT * from inventory GROUP BY model ORDER BY name,model")
    fun getAllInventory():LiveData<List<Inventory>>

    @Query("SELECT * FROM inventory Where type = :type GROUP BY model ORDER BY name,model")
    fun getAllSelectedInventory(type:Int): LiveData<List<Inventory>>

    @Query("SELECT image FROM inventory,product WHERE inventory.model == :model")
    fun getProductImage(model:String):String
}