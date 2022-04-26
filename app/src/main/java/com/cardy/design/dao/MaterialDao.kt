package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.Material
import java.lang.Exception
import kotlin.jvm.Throws

@Dao
interface MaterialDao {
    @Insert
    @Throws(Exception::class)
    fun insertMaterial(vararg material: Material):LongArray

    @Update
    @Throws(Exception::class)
    fun updateMaterial(vararg material: Material):Int

    @Delete
    fun deleteMaterial(vararg material: Material):Int

    @Query("SELECT * FROM material ORDER BY name,model")
    fun getAllMaterial(): LiveData<List<Material>>
}