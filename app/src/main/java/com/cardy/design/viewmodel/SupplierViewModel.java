package com.cardy.design.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cardy.design.dao.SupplierDao;
import com.cardy.design.entity.Supplier;
import com.cardy.design.util.TestDatabase;

import java.util.List;

public class SupplierViewModel extends AndroidViewModel {
    private SupplierDao supplierDao;
    private LiveData<List<Supplier>> listLive;

    public SupplierViewModel(@NonNull Application application) {
        super(application);
        TestDatabase database = TestDatabase.Companion.getINSTANCE(application);
        supplierDao = database.supplierDao();
        listLive = supplierDao.getAllSupplier();
    }

    public void insertSupplier(Supplier... suppliers) {
        new InsertAsyncTask(supplierDao).execute(suppliers);
    }

    public void updateSupplier(Supplier... suppliers) {
        new UpdateAsyncTask(supplierDao).execute(suppliers);
    }

    public void deleteSupplier(Supplier... suppliers) {
        new DeleteAsyncTask(supplierDao).execute(suppliers);
    }

    public LiveData<List<Supplier>> getAllSupplierLive() {
        return listLive;
    }

    static class InsertAsyncTask extends AsyncTask<Supplier, Void, Void> {
        private SupplierDao dao;

        public InsertAsyncTask(SupplierDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Supplier... suppliers) {
            dao.insertSupplier(suppliers);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Supplier, Void, Void> {
        private SupplierDao dao;

        public UpdateAsyncTask(SupplierDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Supplier... suppliers) {
            dao.updateSupplier(suppliers);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Supplier, Void, Void> {
        private SupplierDao dao;

        public DeleteAsyncTask(SupplierDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Supplier... suppliers) {
            dao.deleteSupplier(suppliers);
            return null;
        }
    }
}
