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

public class SupplierListAdapter extends BaseQuickAdapter<CustomerTest, MySupplierViewHolder> {
    public SupplierListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull MySupplierViewHolder holder, CustomerTest customer) {
        holder.name.setText(customer.getName());
        holder.address.setText(customer.getAddress());
        holder.supply.setText(Arrays.toString(customer.getMainPurchase()).replace('[',' ').replace(']',' '));
    }
}

class MySupplierViewHolder extends BaseViewHolder{
    TextView name,address,supply;
    public MySupplierViewHolder(@NonNull View view) {
        super(view);
        name = view.findViewById(R.id.supplierName);
        address = view.findViewById(R.id.supplierAddress);
        supply = view.findViewById(R.id.supplierMainSupply);
    }
}
