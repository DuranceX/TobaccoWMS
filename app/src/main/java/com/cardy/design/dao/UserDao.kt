package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.User

@Dao
interface UserDao {
    @Insert
    fun insertUser(vararg users: User):LongArray

    @Update
    fun updateUser(vararg users: User):Int

    @Delete
    fun deleteUser(vararg users: User):Int

    @Query("SELECT * FROM user ORDER BY id")
    fun getAllUser():LiveData<List<User>>

    @Query("SELECT * FROM user")
    fun getAllUserNotLive():List<User>
}