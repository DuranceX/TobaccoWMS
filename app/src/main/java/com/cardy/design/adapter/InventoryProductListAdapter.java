package com.cardy.design.adapter;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.cardy.design.R;
import com.cardy.design.entity.Inventory;
import com.cardy.design.entity.InventoryTest;
import com.cardy.design.entity.Product;
import com.cardy.design.util.Util;
import com.cardy.design.viewmodel.InventoryViewModel;
import com.cardy.design.viewmodel.ProductViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;

public class InventoryProductListAdapter extends BaseQuickAdapter<Inventory, MyInventoryViewHolder> {
    InventoryViewModel viewModel;
    ProductViewModel productViewModel;
    int SET_IMAGE = 1;

    public InventoryProductListAdapter(int layoutResId, InventoryViewModel viewModel, ProductViewModel productViewModel) {
        super(layoutResId);
        this.viewModel = viewModel;
        this.productViewModel = productViewModel;
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
        holder.image.setImageDrawable(getContext().getDrawable(R.drawable.product_placeholder));
        Handler mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what == SET_IMAGE){
                    String imgPath = msg.getData().getString("imgPath");
                    try{
                        if(!imgPath.equals("")){
                            if(!imgPath.startsWith("http")){
                                Uri imgPathUri = Util.getImagePath(getContext(), imgPath);
                                holder.image.setImageURI(imgPathUri);
                            }
                            else {
                                Picasso.with(getContext()).load(imgPath).into(holder.image);
                            }
                        }
                        else
                            throw new Exception();
                    }catch (Exception exception){
                        Log.e("Test: inventory", exception.toString() );
                    }
                }
            }
        };
        if (inventory.getType() == Inventory.TYPE_PRODUCT){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("Test: inventory", inventory.getModel());
                    String imgPath = productViewModel.getProductByModel(inventory.getModel()).getImage();
                    Message message = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("imgPath",imgPath);
                    Log.d("Test: inventory", imgPath);
                    message.what = SET_IMAGE;
                    message.setData(bundle);
                    mHandler.sendMessage(message);
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