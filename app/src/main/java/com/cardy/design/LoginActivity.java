package com.cardy.design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cardy.design.viewmodel.UserViewModel;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;

public class LoginActivity extends AppCompatActivity {
    EditText etUserId,etPassword;
    TextView loginButton;
    UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        etUserId = findViewById(R.id.editTextUserId);
        etPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginButton);

        Handler mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 2){
                    int isLogin = msg.getData().getInt("isLogin");
                    if(isLogin==1){
                        SharedPreferences shp = getSharedPreferences("userData",MODE_PRIVATE);
                        SharedPreferences.Editor editor = shp.edit();
                        editor.putString("userId",msg.getData().getString("userId"));
                        editor.putString("username",msg.getData().getString("username"));
                        editor.putBoolean("permission",msg.getData().getBoolean("permission"));
                        editor.apply();
                        TipDialog.show("登录成功", WaitDialog.TYPE.SUCCESS).setDialogLifecycleCallback(new DialogLifecycleCallback<WaitDialog>() {
                            @Override
                            public void onDismiss(WaitDialog dialog) {
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        });

                    }
                    else if(isLogin==2){
                        TipDialog.show("用户名或密码错误", WaitDialog.TYPE.ERROR);
                    }
                    else {
                        TipDialog.show("用户不存在", WaitDialog.TYPE.ERROR);
                    }
                }
            }
        };
        viewModel.sendHandler(mHandler);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = etUserId.getText().toString();
                String password = etPassword.getText().toString();
                viewModel.isLogin(id,password);
                WaitDialog.show("登录中....");
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}