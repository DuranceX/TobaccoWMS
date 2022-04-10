package com.cardy.design.util

import androidx.room.Database
import com.cardy.design.dao.*
import com.cardy.design.entity.*

@Database(
    entities = [User::class, Customer::class, Supplier::class, Product::class, Material::class, Inventory::class, PurchaseOrder::class, SaleOrder::class],
    views = [PurchaseAmount::class, PurchaseCount::class],
    version = 1,
    exportSchema = false
)
abstract class WMSDatabase {
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