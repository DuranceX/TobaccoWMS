package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.Material

@Dao
interface MaterialDao {
    @Insert
    fun insertMaterial(vararg material: Material):LongArray

    @Update
    fun updateMaterial(vararg material: Material):Int

    @Delete
    fun deleteMaterial(vararg material: Material):Int

    @Query("SELECT * FROM material")
    fun getAllMaterial(): LiveData<List<Material>>
}