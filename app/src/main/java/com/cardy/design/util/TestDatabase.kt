package com.cardy.design.util

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cardy.design.dao.*
import com.cardy.design.entity.*

@Database(
    entities = [User::class, Customer::class, Supplier::class, Product::class, Material::class, Inventory::class, PurchaseOrder::class, SaleOrder::class],
    views = [PurchaseAmount::class, PurchaseCount::class],
    version = 3,
    exportSchema = false,
)
abstract class TestDatabase:RoomDatabase(){

    companion object{
        private var INSTANCE:TestDatabase?=null

        fun getINSTANCE(context: Context):TestDatabase ? {
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context,TestDatabase::class.java,"test.db")
                    .fallbackToDestructiveMigration()
                    .build()
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

    object MIGRATION_1_2 : Migration(1,2){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE purchase_order ADD COLUMN userId text NOT NULL DEFAULT '3180608067'");
            database.execSQL("ALTER TABLE purchase_order ADD COLUMN userName text DEFAULT ''");
//            database.execSQL("ALTER TABLE sale_order ADD COLUMN userId text NOT NULL DEFAULT '3180608067'");
//            database.execSQL("ALTER TABLE sale_order ADD COLUMN userName text");
        }
    }
}