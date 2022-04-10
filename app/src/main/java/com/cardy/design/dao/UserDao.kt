package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.User

@Dao
interface UserDao {
    @Insert
    fun insertUser(vararg users: User):Long

    @Update
    fun updateUser(vararg users: User):Long

    @Delete
    fun deleteUser(vararg users: User):Long

    @Query("SELECT * FROM user")
    fun getAllUser():LiveData<List<User>>
}