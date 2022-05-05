package com.cardy.design.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cardy.design.dao.UserDao;
import com.cardy.design.entity.User;
import com.cardy.design.util.TestDatabase;
import com.kongzue.dialogx.dialogs.PopTip;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserDao userDao;
    private LiveData<List<User>> listLive;
    private User user;
    private Handler handler;

    public UserViewModel(@NonNull Application application) {
        super(application);
        TestDatabase testDatabase = TestDatabase.Companion.getINSTANCE(application);
        userDao = testDatabase.userDao();
        listLive = userDao.getAllUser();
    }

    public void sendHandler(Handler handler){
        this.handler = handler;
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

    public void isLogin(String id, String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                user = userDao.getUserById(id);
                Message message = Message.obtain();
                message.what = 2;
                Bundle bundle = new Bundle();
                if(user!=null){
                    bundle.putInt("isLogin", password.equals(user.getPassword())?1:2);
                    bundle.putString("userId",user.getId());
                    bundle.putString("username", user.getUsername());
                    bundle.putBoolean("permission",user.getPermission());
                }else{
                    bundle.putInt("isLogin", 3);
                }
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }).start();
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
            try {
                dao.insertUser(users);
                PopTip.show("添加成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("添加出错");
            }
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
            try {
                dao.updateUser(users);
                PopTip.show("修改成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("修改出错");
            }
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
