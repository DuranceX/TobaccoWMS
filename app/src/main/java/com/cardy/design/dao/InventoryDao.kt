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

    @Query("SELECT * FROM inventory Where type = :type AND name LIKE :arg OR model LIKE :arg GROUP BY model ORDER BY name,model")
    fun getAllSelectedQueriedInventory(type:Int,arg:String): LiveData<List<Inventory>>

    @Query("SELECT * FROM inventory WHERE model == :model")
    fun getInventoryByModel(model: String):Inventory
}