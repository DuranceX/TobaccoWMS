package com.cardy.design.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cardy.design.dao.CustomerDao;
import com.cardy.design.dao.SupplierDao;
import com.cardy.design.entity.Customer;
import com.cardy.design.entity.Supplier;
import com.cardy.design.util.TestDatabase;
import com.kongzue.dialogx.dialogs.PopTip;

import java.util.List;

public class CustomerViewModel extends AndroidViewModel {
    CustomerDao dao;
    LiveData<List<Customer>> listLive;

    public CustomerViewModel(@NonNull Application application) {
        super(application);
        TestDatabase database = TestDatabase.Companion.getINSTANCE(application);
        dao = database.customerDao();
        listLive = dao.getAllCustomer();
    }

    public void insertCustomer(Customer... customers) {
        new CustomerViewModel.InsertAsyncTask(dao).execute(customers);
    }

    public void updateCustomer(Customer... customers) {
        new CustomerViewModel.UpdateAsyncTask(dao).execute(customers);
    }

    public void deleteCustomer(Customer... customers) {
        new CustomerViewModel.DeleteAsyncTask(dao).execute(customers);
    }

    public List<String> getNameList(){
        return dao.getCustomerNameList();
    }

    public LiveData<List<Customer>> getAllCustomerLive() {
        return listLive;
    }

    static class InsertAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDao dao;

        public InsertAsyncTask(CustomerDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Customer... customers) {
            try {
                dao.insertCustomer(customers);
                PopTip.show("添加成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("添加出错");
            }
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDao dao;

        public UpdateAsyncTask(CustomerDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Customer... customers) {
            try {
                dao.updateCustomer(customers);
                PopTip.show("修改成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("修改出错");
            }
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDao dao;

        public DeleteAsyncTask(CustomerDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Customer... customers) {
            dao.deleteCustomer(customers);
            return null;
        }
    }
}
