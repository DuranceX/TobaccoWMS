package com.cardy.design.util

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cardy.design.dao.*
import com.cardy.design.entity.*

@Database(
    entities = [User::class, Customer::class, Supplier::class, Product::class, Material::class, Inventory::class, PurchaseOrder::class, SaleOrder::class],
    views = [PurchaseAmount::class, PurchaseCount::class],
    version = 1,
    exportSchema = false
)
abstract class TestDatabase:RoomDatabase(){

    companion object{
        private var INSTANCE:TestDatabase?=null

        fun getINSTANCE(context: Context):TestDatabase ? {
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context,TestDatabase::class.java,"tobacco_database.db").allowMainThreadQueries().build()
            }
            return INSTANCE
        }

        fun getINSTANCE():TestDatabase?= INSTANCE
    }

    abstract fun userDao():UserDao
    abstract fun customerDao():CustomerDao
    abstract fun supplierDao():SupplierDao
    abstract fun productDao():ProductDao
    abstract fun materialDao():MaterialDao
    abstract fun inventoryDao():InventoryDao
    abstract fun purchaseOrderDao():PurchaseOrderDao
    abstract fun saleOrderDao():SaleOrderDao
    abstract fun reportDao():ReportDao
}