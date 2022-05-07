package com.cardy.design.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cardy.design.dao.PurchaseOrderDao;
import com.cardy.design.dao.SaleOrderDao;
import com.cardy.design.entity.PurchaseOrder;
import com.cardy.design.entity.SaleOrder;
import com.cardy.design.util.TestDatabase;
import com.kongzue.dialogx.dialogs.PopTip;

import java.util.List;

public class SaleOrderViewModel extends AndroidViewModel {
    SaleOrderDao dao;
    LiveData<List<SaleOrder>> listLive;

    public SaleOrderViewModel(@NonNull Application application) {
        super(application);
        TestDatabase database = TestDatabase.Companion.getINSTANCE(application);
        dao = database.saleOrderDao();
        listLive = dao.getAllSaleOrder();
    }

    public void insertSaleOrder(SaleOrder... saleOrders) {
        new SaleOrderViewModel.InsertAsyncTask(dao).execute(saleOrders);
    }

    public void updateSaleOrder(SaleOrder... saleOrders) {
        new SaleOrderViewModel.UpdateAsyncTask(dao).execute(saleOrders);
    }

    public void deleteSaleOrder(SaleOrder... saleOrders) {
        new SaleOrderViewModel.DeleteAsyncTask(dao).execute(saleOrders);
    }

    public LiveData<List<SaleOrder>> getAllSaleOrderLive() {
        return listLive;
    }

    public LiveData<List<SaleOrder>> getAllQueriedSaleOrderLive(String arg){
        String newArg = "%"+arg+"%";
        return dao.getAllQueriedSaleOrder(newArg);
    }

    public List<SaleOrder> getSelectedStateOrder(String state){
        return dao.getSelectedStateOrder(state);
    }

    static class InsertAsyncTask extends AsyncTask<SaleOrder, Void, Void> {
        private SaleOrderDao dao;

        public InsertAsyncTask(SaleOrderDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(SaleOrder... saleOrders) {
            try {
                dao.insertSaleOrder(saleOrders);
                PopTip.show("添加成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("添加出错");
            }
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<SaleOrder, Void, Void> {
        private SaleOrderDao dao;

        public UpdateAsyncTask(SaleOrderDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(SaleOrder... saleOrders) {
            try {
                dao.updateSaleOrder(saleOrders);
                PopTip.show("修改成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("修改出错");
            }
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<SaleOrder, Void, Void> {
        private SaleOrderDao dao;

        public DeleteAsyncTask(SaleOrderDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(SaleOrder... saleOrders) {
            dao.deleteSaleOrder(saleOrders);
            return null;
        }
    }
}
