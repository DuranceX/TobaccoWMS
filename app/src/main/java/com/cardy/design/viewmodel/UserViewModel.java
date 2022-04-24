package com.cardy.design.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cardy.design.dao.UserDao;
import com.cardy.design.entity.User;
import com.cardy.design.util.TestDatabase;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserDao userDao;
    private LiveData<List<User>> listLive;

    public UserViewModel(@NonNull Application application) {
        super(application);
        TestDatabase testDatabase = TestDatabase.Companion.getINSTANCE(application);
        userDao = testDatabase.userDao();
        listLive = userDao.getAllUser();
    }

    public void insertUser(User... users){
        new InsertAsyncTask(userDao).execute(users);
    }

    public void updateUser(User... users){
        new UpdateAsyncTask(userDao).execute(users);
    }

    public void deleteUser(User... users){
        new DeleteAsyncTask(userDao).execute(users);
    }

    public LiveData<List<User>> getAllUserLive(){
        return listLive;
    }

    static class InsertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao dao;

        public InsertAsyncTask(UserDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            dao.insertUser(users);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<User,Void,Void>{
        private UserDao dao;

        public UpdateAsyncTask(UserDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            dao.updateUser(users);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<User,Void,Void>{
        private UserDao dao;

        public DeleteAsyncTask(UserDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            dao.deleteUser(users);
            return null;
        }
    }

}
