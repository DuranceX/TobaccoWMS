package com.cardy.design.util

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cardy.design.dao.*
import com.cardy.design.entity.*

@Database(
    entities = [User::class,PurchaseOrder::class],
    views = [],
    version = 1,
    exportSchema = false
)
abstract class TestDatabase:RoomDatabase(){

    companion object{
        private var INSTANCE:TestDatabase?=null

        fun getINSTANCE(context: Context):TestDatabase ? {
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context,TestDatabase::class.java,"test.db").build()
            }
            return INSTANCE
        }

        fun getINSTANCE():TestDatabase?= INSTANCE
    }

    abstract fun userDao():UserDao
    abstract fun purchaseOrderDao():PurchaseOrderDao
}