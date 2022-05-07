package com.cardy.design.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cardy.design.entity.User
import java.lang.Exception
import kotlin.jvm.Throws

@Dao
interface UserDao {
    @Insert
    @Throws(Exception::class)
    fun insertUser(vararg users: User):LongArray

    @Update
    @Throws(Exception::class)
    fun updateUser(vararg users: User):Int

    @Delete
    fun deleteUser(vararg users: User):Int

    @Query("SELECT * FROM user ORDER BY id")
    fun getAllUser():LiveData<List<User>>

    @Query("SELECT * FROM user WHERE username LIKE :arg OR id LIKE :arg ORDER BY id")
    fun getAllQueriedUserLive(arg:String):LiveData<List<User>>

    @Query("SELECT * from user WHERE id==:id")
    fun getUserById(id:String):User
}