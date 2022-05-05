package com.cardy.design;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

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
    ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取SharePreference中保存的登录用户的信息
        SharedPreferences shp = getSharedPreferences("userData",MODE_PRIVATE);
        String userID = shp.getString("userId","");
        String username = shp.getString("username","");
        Boolean permission = shp.getBoolean("permission",false);

        TextView tvUserId,tvUsername;
        ImageView userImage;

        //初始化
        NavigationView navigationView = findViewById(R.id.navigationView);
        tvUserId = navigationView.getHeaderView(0).findViewById(R.id.userId);
        tvUsername = navigationView.getHeaderView(0).findViewById(R.id.username);
        userImage = navigationView.getHeaderView(0).findViewById(R.id.userImageView);
        tvUsername.setText(username);
        tvUserId.setText(userID);
        if (permission) {
            userImage.setImageDrawable(getDrawable(R.drawable.manager_green));
            navigationView.inflateMenu(R.menu.menu_admin);
        }
        else {
            userImage.setImageDrawable(getDrawable(R.drawable.normal_blue));
            navigationView.inflateMenu(R.menu.menu);
        }

        //设置navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController controller = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navigationView,controller);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    if(Environment.isExternalStorageManager()){}
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if(Environment.isExternalStorageManager()){

            }else{
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:"+getPackageName()));
                intentActivityResultLauncher.launch(intent);
            }
        }
    }
}