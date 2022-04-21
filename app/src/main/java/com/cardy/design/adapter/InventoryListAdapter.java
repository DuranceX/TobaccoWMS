package com.cardy.design.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cardy.design.R;
import com.cardy.design.entity.InventoryTest;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.dialogs.TipDialog;

import java.util.List;

public class InventoryListAdapter extends BaseQuickAdapter<InventoryTest, MyInventoryViewHolder> {
    List<InventoryTest> list;

    public InventoryListAdapter(int layoutResId) {
        super(layoutResId);
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BottomDialog.show("实现跳转页面", "跳转到该库存的具体内容页面");
            }
        });
    }

    @Override
    protected void convert(@NonNull MyInventoryViewHolder holder, InventoryTest inventory) {
        holder.name.setText(inventory.getName());
        holder.model.setText(inventory.getModel());
        holder.hostCount.setText(String.valueOf(inventory.getHostCount()));
        holder.deliveryCount.setText(String.valueOf(inventory.getDeliveryCount()));
        //if (inventory.getType() == InventoryTest.TYPE_PRODUCT)
            //holder.image.setImageURI();
    }
}


class MyInventoryViewHolder extends BaseViewHolder {
    TextView name,model,hostCount,deliveryCount;
    ImageView image;
    public MyInventoryViewHolder(@NonNull View view) {
        super(view);
        name = view.findViewById(R.id.textViewName);
        model = view.findViewById(R.id.textViewModel);
        hostCount = view.findViewById(R.id.textViewHostCount);
        deliveryCount = view.findViewById(R.id.textViewDeliveryCount);
        image = view.findViewById(R.id.productImageView);
    }
}