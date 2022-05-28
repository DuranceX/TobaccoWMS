package com.cardy.design.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cardy.design.dao.InventoryDao;
import com.cardy.design.entity.Inventory;
import com.cardy.design.util.TestDatabase;
import com.cardy.design.util.WMSDatabase;
import com.kongzue.dialogx.dialogs.PopTip;

import java.util.List;

public class InventoryViewModel extends AndroidViewModel {
    InventoryDao dao;
    LiveData<List<Inventory>> listLive;
    
    public InventoryViewModel(@NonNull Application application) {
        super(application);
        dao = WMSDatabase.Companion.getINSTANCE(application).inventoryDao();
        listLive = dao.getAllInventory();
    }

    public void insertInventory(Inventory... inventories) {
        new InventoryViewModel.InsertAsyncTask(dao).execute(inventories);
    }

    public void updateInventory(Inventory... inventories) {
        new InventoryViewModel.UpdateAsyncTask(dao).execute(inventories);
    }

    public void deleteInventory(Inventory... inventories) {
        new InventoryViewModel.DeleteAsyncTask(dao).execute(inventories);
    }

    public LiveData<List<Inventory>> getAllProductInventory() {
        return dao.getAllSelectedInventory(Inventory.TYPE_PRODUCT);
    }

    public LiveData<List<Inventory>> getAllMaterialInventory() {
        return dao.getAllSelectedInventory(Inventory.TYPE_MATERIAL);
    }

    public LiveData<List<Inventory>> getAllQueriedProductInventory(String arg){
        String newArg = "%"+arg+"%";
        return dao.getAllSelectedQueriedInventory(Inventory.TYPE_PRODUCT,newArg);
    }

    public LiveData<List<Inventory>> getAllQueriedMaterialInventory(String arg){
        String newArg = "%"+arg+"%";
        return dao.getAllSelectedQueriedInventory(Inventory.TYPE_MATERIAL,newArg);
    }

    public Inventory getInventoryByModel(String model){
        return dao.getInventoryByModel(model);
    }

    static class InsertAsyncTask extends AsyncTask<Inventory, Void, Void> {
        private InventoryDao dao;

        public InsertAsyncTask(InventoryDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Inventory... inventories) {
            try {
                dao.insertInventory(inventories);
                PopTip.show("添加成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("添加出错");
            }
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Inventory, Void, Void> {
        private InventoryDao dao;

        public UpdateAsyncTask(InventoryDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Inventory... inventories) {
            try {
                dao.updateInventory(inventories);
                PopTip.show("修改成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("修改出错");
            }
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Inventory, Void, Void> {
        private InventoryDao dao;

        public DeleteAsyncTask(InventoryDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Inventory... inventories) {
            dao.deleteInventory(inventories);
            return null;
        }
    }
}
