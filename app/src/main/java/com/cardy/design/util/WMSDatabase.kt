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
    views = [CustomerAmount::class, SupplierAmount::class, ProductSaleAmount::class, MaterialPurchaseAmount::class],
    version = 2,
    exportSchema = false
)
abstract class WMSDatabase:RoomDatabase(){

    companion object{
        private var INSTANCE:WMSDatabase?=null

        fun getINSTANCE(context: Context):WMSDatabase ? {
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context,WMSDatabase::class.java,"tobacco_database.db")
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return INSTANCE
        }

        fun getINSTANCE():WMSDatabase?= INSTANCE
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
            database.execSQL("ALTER TABLE inventory ADD COLUMN areaNumber text NOT NULL DEFAULT '100'");
        }
    }
}