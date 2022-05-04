package com.cardy.design.adapter;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cardy.design.R;
import com.cardy.design.entity.Inventory;
import com.cardy.design.util.Util;
import com.cardy.design.viewmodel.InventoryViewModel;
import com.cardy.design.viewmodel.ProductViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.squareup.picasso.Picasso;

public class InventoryMaterialListAdapter extends BaseQuickAdapter<Inventory, MyInventoryViewHolder> {
    InventoryViewModel viewModel;

    public InventoryMaterialListAdapter(int layoutResId, InventoryViewModel viewModel) {
        super(layoutResId);
        this.viewModel = viewModel;
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BottomDialog.show("实现跳转页面", "跳转到该库存的具体内容页面");
            }
        });
    }

    @Override
    protected void convert(@NonNull MyInventoryViewHolder holder, Inventory inventory) {
        holder.name.setText(inventory.getName());
        holder.model.setText(inventory.getModel());
        holder.hostCount.setText(String.valueOf(inventory.getHostCount()));
        holder.deliveryCount.setText(String.valueOf(inventory.getDeliveryCount()));
    }
}