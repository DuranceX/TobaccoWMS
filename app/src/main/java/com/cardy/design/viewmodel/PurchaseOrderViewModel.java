package com.cardy.design.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cardy.design.dao.MaterialDao;
import com.cardy.design.dao.ProductDao;
import com.cardy.design.dao.PurchaseOrderDao;
import com.cardy.design.dao.SupplierDao;
import com.cardy.design.entity.Material;
import com.cardy.design.entity.Product;
import com.cardy.design.entity.PurchaseOrder;
import com.cardy.design.entity.Supplier;
import com.cardy.design.util.TestDatabase;
import com.cardy.design.util.WMSDatabase;
import com.kongzue.dialogx.dialogs.PopTip;

import java.util.List;

public class PurchaseOrderViewModel extends AndroidViewModel {
    PurchaseOrderDao dao;
    LiveData<List<PurchaseOrder>> listLive;

    public PurchaseOrderViewModel(@NonNull Application application) {
        super(application);
        WMSDatabase database = WMSDatabase.Companion.getINSTANCE(application);
        dao = database.purchaseOrderDao();
        listLive = dao.getAllPurchaseOrder();
    }

    public void insertPurchaseOrder(PurchaseOrder... purchaseOrders) {
        new PurchaseOrderViewModel.InsertAsyncTask(dao).execute(purchaseOrders);
    }

    public void updatePurchaseOrder(PurchaseOrder... purchaseOrders) {
        new PurchaseOrderViewModel.UpdateAsyncTask(dao).execute(purchaseOrders);
    }

    public void deletePurchaseOrder(PurchaseOrder... purchaseOrders) {
        new PurchaseOrderViewModel.DeleteAsyncTask(dao).execute(purchaseOrders);
    }

    public LiveData<List<PurchaseOrder>> getAllPurchaseOrderLive() {
        return listLive;
    }

    public LiveData<List<PurchaseOrder>> getAllQueriedPurchaseOrderLive(String arg){
        String newArg = "%"+arg+"%";
        return dao.getAllQueriedPurchaseOrder(newArg);
    }

    public List<PurchaseOrder> getSelectedStateOrder(String state){
        return dao.getSelectedStateOrder(state);
    }

    static class InsertAsyncTask extends AsyncTask<PurchaseOrder, Void, Void> {
        private PurchaseOrderDao dao;

        public InsertAsyncTask(PurchaseOrderDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(PurchaseOrder... purchaseOrders) {
            try {
                dao.insertPurchaseOrder(purchaseOrders);
                PopTip.show("添加成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("添加出错");
            }
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<PurchaseOrder, Void, Void> {
        private PurchaseOrderDao dao;

        public UpdateAsyncTask(PurchaseOrderDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(PurchaseOrder... purchaseOrders) {
            try {
                dao.updatePurchaseOrder(purchaseOrders);
                PopTip.show("修改成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("修改出错");
            }
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<PurchaseOrder, Void, Void> {
        private PurchaseOrderDao dao;

        public DeleteAsyncTask(PurchaseOrderDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(PurchaseOrder... purchaseOrders) {
            dao.deletePurchaseOrder(purchaseOrders);
            return null;
        }
    }
}
