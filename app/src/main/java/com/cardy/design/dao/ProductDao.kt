package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.Product

@Dao
interface ProductDao {
    @Insert
    fun insertProduct(vararg product: Product):LongArray

    @Update
    fun updateProduct(vararg product: Product):Int

    @Delete
    fun deleteProduct(vararg product: Product):Int

    @Query("SELECT * FROM product ORDER BY model")
    fun getAllProduct(): LiveData<List<Product>>
}