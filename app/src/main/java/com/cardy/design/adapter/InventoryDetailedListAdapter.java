package com.cardy.design.adapter;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cardy.design.R;
import com.cardy.design.entity.InventoryDetail;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

public class InventoryDetailedListAdapter extends BaseQuickAdapter<InventoryDetail, MyInventoryDetailViewHolder> {
    int sum;

    public InventoryDetailedListAdapter(int layoutResId,int sum) {
        super(layoutResId);
        this.sum = sum;
    }

    @Override
    protected void convert(@NonNull MyInventoryDetailViewHolder holder, InventoryDetail inventoryDetail) {
        holder.areaName.setText(inventoryDetail.getAreaName());
        holder.areaNumber.setText(inventoryDetail.getAreaNumber());
        holder.progressBar.setMax((int) ((int)sum*0.7));
        holder.progressBar.setProgress(Integer.parseInt(inventoryDetail.getAreaNumber()));
    }
}

class MyInventoryDetailViewHolder extends BaseViewHolder{
    TextView areaName,areaNumber;
    ProgressBar progressBar;

    public MyInventoryDetailViewHolder(@NonNull View view) {
        super(view);
        areaName = view.findViewById(R.id.textViewAreaName);
        areaNumber = view.findViewById(R.id.textViewAreaNumber);
        progressBar = view.findViewById(R.id.progressBar);
    }
}
