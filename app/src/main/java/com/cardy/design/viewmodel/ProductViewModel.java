package com.cardy.design.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cardy.design.dao.ProductDao;
import com.cardy.design.dao.SupplierDao;
import com.cardy.design.entity.Product;
import com.cardy.design.entity.Supplier;
import com.cardy.design.util.TestDatabase;
import com.kongzue.dialogx.dialogs.PopTip;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private ProductDao dao;
    private LiveData<List<Product>> listLive;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        dao = TestDatabase.Companion.getINSTANCE(application).productDao();
        listLive = dao.getAllProduct();
    }

    public void insertProduct(Product... products) {
        new ProductViewModel.InsertAsyncTask(dao).execute(products);
    }

    public void updateProduct(Product... products) {
        new ProductViewModel.UpdateAsyncTask(dao).execute(products);
    }

    public void deleteProduct(Product... products) {
        new ProductViewModel.DeleteAsyncTask(dao).execute(products);
    }

    public List<String> getProductNameList(){
        return dao.getProductNameList();
    }

    public List<String> getProductModelListByName(String name){
        return dao.getProductModelListByName(name);
    }

    public Double getPriceByModel(String model){
        return dao.getProductByModel(model);
    }

    public LiveData<List<Product>> getAllProductLive() {
        return listLive;
    }

    static class InsertAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao dao;

        public InsertAsyncTask(ProductDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            try {
                dao.insertProduct(products);
                PopTip.show("添加成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("添加出错");
            }
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao dao;

        public UpdateAsyncTask(ProductDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            try {
                dao.updateProduct(products);
                PopTip.show("修改成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("修改出错");
            }
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao dao;

        public DeleteAsyncTask(ProductDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            dao.deleteProduct(products);
            return null;
        }
    }
}
