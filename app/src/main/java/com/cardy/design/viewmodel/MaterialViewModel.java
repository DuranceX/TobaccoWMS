package com.cardy.design.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cardy.design.dao.MaterialDao;
import com.cardy.design.entity.Material;
import com.cardy.design.util.TestDatabase;
import com.kongzue.dialogx.dialogs.PopTip;

import java.util.List;

public class MaterialViewModel extends AndroidViewModel{
    MaterialDao dao;
    LiveData<List<Material>> listLive;

    public MaterialViewModel(@NonNull Application application) {
        super(application);
        TestDatabase database = TestDatabase.Companion.getINSTANCE(application);
        dao = database.materialDao();
        listLive = dao.getAllMaterial();
    }

    public void insertMaterial(Material... materials){
        new MaterialViewModel.InsertAsyncTask(dao).execute(materials);
    }

    public void updateMaterial(Material... materials){
        new MaterialViewModel.UpdateAsyncTask(dao).execute(materials);
    }

    public void deleteMaterial(Material... materials){
        new MaterialViewModel.DeleteAsyncTask(dao).execute(materials);
    }

    public List<String> getMaterialNameList(){
        return dao.getMaterialNameList();
    }

    public List<String> getMaterialModelListByName(String name){
        return dao.getMaterialModelListByName(name);
    }

    public Double getPriceByModel(String model){
        return dao.getPriceByModel(model);
    }

    public LiveData<List<Material>> getAllMaterialsLive(){
        return listLive;
    }

    public List<Material> getAllMaterialsNoLive(){
        return dao.getAllMaterialNoLive();
    }

    static class InsertAsyncTask extends AsyncTask<Material, Void, Void> {
        private MaterialDao dao;

        public InsertAsyncTask(MaterialDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Material... materials){
            try {
                dao.insertMaterial(materials);
                PopTip.show("添加成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("添加出错");
            }
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Material, Void, Void> {
        private MaterialDao dao;

        public UpdateAsyncTask(MaterialDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Material... materials) {
            try {
                dao.updateMaterial(materials);
                PopTip.show("修改成功");
            } catch (Exception exception) {
                exception.printStackTrace();
                PopTip.show("修改出错");
            }
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Material, Void, Void> {
        private MaterialDao dao;

        public DeleteAsyncTask(MaterialDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Material... materials) {
            dao.deleteMaterial(materials);
            return null;
        }
    }
}
