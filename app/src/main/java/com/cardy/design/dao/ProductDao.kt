package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.Product

@Dao
interface ProductDao {
    @Insert
    fun insertProduct(vararg product: Product):Long

    @Update
    fun updateProduct(vararg product: Product):Long

    @Delete
    fun deleteProduct(vararg product: Product):Long

    @Query("SELECT * FROM product")
    fun getAllProduct(): LiveData<List<Product>>
}