package com.cardy.design.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cardy.design.dao.ReportDao;
import com.cardy.design.entity.CustomerAmount;
import com.cardy.design.entity.MaterialPurchaseAmount;
import com.cardy.design.entity.ProductSaleAmount;
import com.cardy.design.entity.SupplierAmount;
import com.cardy.design.util.TestDatabase;

import java.util.List;

public class ReportViewModel extends AndroidViewModel {
    ReportDao dao;

    public ReportViewModel(@NonNull Application application) {
        super(application);
        dao = TestDatabase.Companion.getINSTANCE().reportDao();
    }

    public List<CustomerAmount> getCustomerAmount(){
        return dao.getCustomerAmount();
    }

    public List<SupplierAmount> getSupplierAmount(){
        return dao.getSupplierAmount();
    }

    public List<ProductSaleAmount> getProductSaleAmount(){
        return dao.getProductSaleAmount();
    }

    public List<MaterialPurchaseAmount> getMaterialPurchaseAmount(){
        return dao.getMaterialPurchaseAmount();
    }

    public Double getLastMonthSalePrice(String date){
        return dao.getLastMonthSalePrice(date);
    }

    public Double getLastMonthPurchasePrice(String date){
        return dao.getLastMonthPurchasePrice(date);
    }
}
