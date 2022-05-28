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
    version = 6,
    exportSchema = false,
)
abstract class TestDatabase:RoomDatabase(){

    companion object{
        private var INSTANCE:TestDatabase?=null

        fun getINSTANCE(context: Context):TestDatabase ? {
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context,TestDatabase::class.java,"test.db")
                    .addMigrations(MIGRATION_5_6)
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

    object MIGRATION_3_4 : Migration(3,4){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE VIEW `CustomerAmount` AS SELECT customer, SUM(price) AS price, count(customer) AS times FROM sale_order GROUP BY customer")
            database.execSQL("CREATE VIEW `SupplierAmount` AS SELECT supplier, SUM(price) AS price, count(supplier) AS times FROM purchase_order GROUP BY supplier")
            database.execSQL("CREATE VIEW `ProductSaleAmount` AS SELECT productName,productModel,count(price) AS price FROM sale_order GROUP BY productModel")
            database.execSQL("CREATE VIEW `MaterialPurchaseAmount` AS SELECT materialName,materialModel,count(price) AS price FROM purchase_order GROUP BY materialModel")
        }
    }

    object MIGRATION_4_5 : Migration(4,5){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP VIEW ProductSaleAmount")
            database.execSQL("DROP VIEW MaterialPurchaseAmount")
            database.execSQL("CREATE VIEW `ProductSaleAmount` AS SELECT productName,productModel,sum(price) AS price FROM sale_order GROUP BY productModel")
            database.execSQL("CREATE VIEW `MaterialPurchaseAmount` AS SELECT materialName,materialModel,sum(price) AS price FROM purchase_order GROUP BY materialModel")
        }
    }

    object MIGRATION_5_6 : Migration(5,6){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE inventory ADD COLUMN areaNumber text NOT NULL DEFAULT '100'");
        }
    }
}