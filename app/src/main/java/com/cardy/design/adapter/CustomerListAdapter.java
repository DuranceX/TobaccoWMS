package com.cardy.design.adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class CustomerListAdapter extends BaseQuickAdapter<CustomerTest, MyCustomerViewHolder> implements DraggableModule {
    List<CustomerTest> newList;

    public CustomerListAdapter(int layoutResId) {
        super(layoutResId);
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BottomDialog.show("修改客户信息",new OnBindView<BottomDialog>(R.layout.dialog_add_customer_supplier) {
                    //TODO: 添加”修改“功能
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        TextView textViewMain;
                        EditText editTextName,editTextAddress,editTextAvatar;
                        RadioButton radioButtonLow,radioButtonMid,radioButtonHigh;

                        editTextAvatar = v.findViewById(R.id.editTextLogo);
                        editTextName = v.findViewById(R.id.editTextName);
                        editTextAddress = v.findViewById(R.id.editTextModel);
                        textViewMain = v.findViewById(R.id.textViewMain);
                        radioButtonLow = v.findViewById(R.id.radioButtonLow);
                        radioButtonMid = v.findViewById(R.id.radioButtonMid);
                        radioButtonHigh = v.findViewById(R.id.radioButtonHigh);

                        editTextAvatar.setText("https://ssss/");
                        editTextName.setText(newList.get(position).getName());
                        editTextAddress.setText(newList.get(position).getAddress());
                        textViewMain.setText(Arrays.toString(newList.get(position).getMainPurchase()).replace('[',' ').replace(']',' '));
                        radioButtonLow.setChecked(true);
                    }
                }).setOkButton("确定").setCancelButton("取消");
            }
        });
        addDraggableModule(this);
    }

    @Override
    protected void convert(@NonNull MyCustomerViewHolder holder, CustomerTest customer) {
        holder.name.setText(customer.getName());
        holder.address.setText(customer.getAddress());
        holder.purchase.setText(Arrays.toString(customer.getMainPurchase()).replace('[',' ').replace(']',' '));
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

class MyCustomerViewHolder extends BaseViewHolder{
    TextView name,address,purchase;
    public MyCustomerViewHolder(@NonNull View view) {
        super(view);
        name = view.findViewById(R.id.customerName);
        address = view.findViewById(R.id.customerAddress);
        purchase = view.findViewById(R.id.customerMainPurchase);
    }
}