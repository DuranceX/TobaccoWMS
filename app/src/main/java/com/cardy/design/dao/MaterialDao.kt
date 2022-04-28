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

    @Query("SELECT * FROM material ORDER BY name,model")
    fun getAllMaterialNoLive(): List<Material>

    @Query("SELECT name FROM material GROUP BY name")
    fun getMaterialNameList():List<String>

    @Query("SELECT model FROM material WHERE name=:name")
    fun getMaterialModelListByName(name:String):List<String>

    @Query("SELECT price FROM material WHERE model=:model")
    fun getPriceByModel(model:String):Double
}