package com.cardy.design.adapter;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cardy.design.R;
import com.cardy.design.entity.CustomerTest;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.module.BaseDraggableModule;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;

import java.util.Arrays;
import java.util.List;

public class UserListAdapter extends BaseQuickAdapter<CustomerTest, MyUserViewHolder> implements DraggableModule {
    List<CustomerTest> newList;

    public UserListAdapter(int layoutResId) {
        super(layoutResId);
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BottomDialog.show("修改用户信息",new OnBindView<BottomDialog>(R.layout.dialog_add_user) {
                    //TODO: 添加”修改“功能
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        EditText editTextId,editTextName,editTextPassword;
                        Switch adminSwitch;

                        editTextId = v.findViewById(R.id.editTextId);
                        editTextName = v.findViewById(R.id.editTextName);
                        editTextPassword = v.findViewById(R.id.editTextPassword);
                        adminSwitch = v.findViewById(R.id.adminSwitch);

                        editTextId.setText("31806080"+position);
                        editTextName.setText(newList.get(position).getName());
                        editTextPassword.setText(newList.get(position).getAddress());
                        adminSwitch.setChecked(false);
                    }
                }).setOkButton("确定").setCancelButton("取消");
            }
        });
        addDraggableModule(this);
    }

    @Override
    protected void convert(@NonNull MyUserViewHolder holder, CustomerTest customer) {
        holder.id.setText(customer.getName());
        holder.name.setText(customer.getAddress());
        holder.password.setText(Arrays.toString(customer.getMainPurchase()).replace('[',' ').replace(']',' '));
    }

    public void setList(List<CustomerTest> list){
        this.newList = list;
    }

    @NonNull
    @Override
    public BaseDraggableModule addDraggableModule(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new BaseDraggableModule(baseQuickAdapter);
    }
}

class MyUserViewHolder extends BaseViewHolder{
    TextView id,name,password;
    public MyUserViewHolder(@NonNull View view) {
        super(view);
        id = view.findViewById(R.id.userId);
        name = view.findViewById(R.id.userName);
        password = view.findViewById(R.id.userPassword);
    }
}