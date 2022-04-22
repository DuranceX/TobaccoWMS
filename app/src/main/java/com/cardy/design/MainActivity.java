package com.cardy.design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.cardy.design.dao.PurchaseOrderDao;
import com.cardy.design.dao.UserDao;
import com.cardy.design.entity.PurchaseOrder;
import com.cardy.design.entity.User;
import com.cardy.design.util.TestDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.sql.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.navigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController controller = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navigationView,controller);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TestDatabase database = TestDatabase.Companion.getINSTANCE(this);
        UserDao dao = database.userDao();
        PurchaseOrderDao orderDao = database.purchaseOrderDao();
//        User user = new User("3180608067","谢嘉迪","3180608067",true);
//        String nowTime = new Date(System.currentTimeMillis()).toString();
//        String endTime = new Date(System.currentTimeMillis()).toString();
//        PurchaseOrder order = new PurchaseOrder("鸦片","SICS210325",200,13250.0,"蓝天种植园",nowTime,
//                endTime,"申请中","");
//        dao.insertUser(user);
//        orderDao.insertPurchaseOrder(order);
        Log.d("TestDatabase", String.valueOf(dao.getAllUserNotLive()));
        Log.d("TestDatabase_order", String.valueOf(orderDao.getAllPurchaseOrder().getValue()));
    }
}