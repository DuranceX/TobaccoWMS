package com.cardy.design.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.Permission

@Entity(tableName = "user")
data class User(
    //用户ID
    @PrimaryKey
    var id:String,
    //用户名
    @ColumnInfo(name = "username")
    var username:String,
    //用户密码
    @ColumnInfo(name = "password")
    var password:String,
    //用户权限，true为管理员，false为普通用户
    @ColumnInfo(name = "permission")
    var permission:Boolean
){
    companion object{
        const val PERMISSION_ADMINISTRATOR:Boolean=true
        const val Permission_NORMAL:Boolean=false
    }
}
