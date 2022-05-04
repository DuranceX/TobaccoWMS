package com.cardy.design.adapter;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cardy.design.R;
import com.cardy.design.entity.Inventory;
import com.cardy.design.entity.InventoryTest;
import com.cardy.design.util.Util;
import com.cardy.design.viewmodel.InventoryViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InventoryListAdapter extends BaseQuickAdapter<Inventory, MyInventoryViewHolder> {
    InventoryViewModel viewModel;

    public InventoryListAdapter(int layoutResId,InventoryViewModel viewModel) {
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
        if (inventory.getType() == InventoryTest.TYPE_PRODUCT){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //设置图片路径
                    try{
                        String path = viewModel.getProductImage(inventory.getModel());
                        if(!path.equals("")){
                            if(!path.startsWith("http")){
                                Uri pathUri = Util.getImagePath(getContext(),path);
                                holder.image.setImageURI(pathUri);
                            }
                            else {
                                Picasso.with(getContext()).load(path).into(holder.image);
                            }
                        }
                        else
                            throw new Exception();
                    }catch (Exception exception){
                        holder.image.setImageDrawable(getContext().getDrawable(R.drawable.product_placeholder));
                    }
                }
            }).start();
        }
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