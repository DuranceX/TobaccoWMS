package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.Material
import com.cardy.design.entity.Product
import java.lang.Exception
import kotlin.jvm.Throws

@Dao
interface ProductDao {
    @Insert
    @Throws(Exception::class)
    fun insertProduct(vararg product: Product):LongArray

    @Update
    @Throws(Exception::class)
    fun updateProduct(vararg product: Product):Int

    @Delete
    fun deleteProduct(vararg product: Product):Int

    @Query("SELECT * FROM product ORDER BY name,model")
    fun getAllProduct(): LiveData<List<Product>>

    @Query("SELECT * FROM product ORDER BY name,model")
    fun getAllProductNoLive(): List<Product>

    @Query("SELECT name FROM product GROUP BY name")
    fun getProductNameList():List<String>

    @Query("SELECT model FROM product WHERE name=:name")
    fun getProductModelListByName(name:String):List<String>

    @Query("SELECT * FROM product WHERE model=:model")
    fun getProductByModel(model:String):Product
}