package com.cardy.design.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cardy.design.R;
import com.cardy.design.entity.CustomerTest;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.Arrays;

public class CustomerListAdapter extends BaseQuickAdapter<CustomerTest, MyCustomerViewHolder> {
    public CustomerListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull MyCustomerViewHolder holder, CustomerTest customer) {
        holder.name.setText(customer.getName());
        holder.address.setText(customer.getAddress());
        holder.purchase.setText(Arrays.toString(customer.getMainPurchase()).replace('[',' ').replace(']',' '));
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
