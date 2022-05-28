package com.cardy.design.adapter;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cardy.design.R;
import com.cardy.design.entity.Inventory;
import com.cardy.design.entity.InventoryDetail;
import com.cardy.design.entity.PurchaseOrder;
import com.cardy.design.viewmodel.InventoryViewModel;
import com.cardy.design.viewmodel.PurchaseOrderViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class InventoryMaterialListAdapter extends BaseQuickAdapter<Inventory, MyInventoryViewHolder> {
    List<Inventory> list;
    InventoryViewModel viewModel;
    PurchaseOrderViewModel purchaseOrderViewModel;

    public InventoryMaterialListAdapter(int layoutResId, InventoryViewModel viewModel,PurchaseOrderViewModel purchaseOrderViewModel) {
        super(layoutResId);
        this.viewModel = viewModel;
        this.purchaseOrderViewModel = purchaseOrderViewModel;
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                FullScreenDialog.show(new OnBindView<FullScreenDialog>(R.layout.dialog_inventory_detail) {
                    @Override
                    public void onBind(FullScreenDialog dialog, View v) {
                        TextView objectName = v.findViewById(R.id.textViewObjectName);
                        TextView objectModel = v.findViewById(R.id.textViewObjectModel);
                        RecyclerView rvInventory = v.findViewById(R.id.recycleViewInventory);
                        RecyclerView rvOrder = v.findViewById(R.id.recyclerViewDeliveryOrder);
                        List<InventoryDetail> detailList = new ArrayList<>();
                        final List<PurchaseOrder>[] orderList = new List[]{new ArrayList<>()};
                        String[] areaList;
                        String[] areaNumberList;

                        objectName.setText(list.get(position).getName());
                        objectModel.setText(list.get(position).getModel());

                        areaList = list.get(position).getArea().split(",");
                        areaNumberList = list.get(position).getAreaNumber().split(",");
                        for(int i=0;i<Math.min(areaList.length,areaNumberList.length);i++){
                            if(!areaNumberList[i].equals("")) {
                                InventoryDetail temp = new InventoryDetail(areaList[i], areaNumberList[i]);
                                detailList.add(temp);
                            }
                        }

                        InventoryDetailedListAdapter inventoryDetailedListAdapter = new InventoryDetailedListAdapter(R.layout.item_inventory_detailed,list.get(position).getHostCount());
                        inventoryDetailedListAdapter.setAnimationEnable(true);
                        rvInventory.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvInventory.setAdapter(inventoryDetailedListAdapter);
                        inventoryDetailedListAdapter.setList(detailList);

                        InventoryDetailedPurchaseOrderListAdapter inventoryDetailedPurchaseOrderListAdapter  = new InventoryDetailedPurchaseOrderListAdapter(R.layout.item_purchase_order);
                        inventoryDetailedPurchaseOrderListAdapter.setAnimationEnable(true);
                        rvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvOrder.setAdapter(inventoryDetailedPurchaseOrderListAdapter);

                        Handler mHandler = new Handler(Looper.myLooper()){
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if(msg.what == 999){
                                    inventoryDetailedPurchaseOrderListAdapter.setList(orderList[0]);
                                }
                            }
                        };

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                orderList[0] = purchaseOrderViewModel.getSelectedStateOrder("运输中");
                                Message message = Message.obtain();
                                message.what = 999;
                                mHandler.sendMessage(message);
                            }
                        }).start();
                    }
                });
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

    /**
     * 重写setList方法，更新列表数据
     *
     * @param list
     */
    public void setMyList(List<Inventory> list) {
        this.list = list;
    }

    /**
     * 重写setNewInstance方法，初始化数据时同时更新adapter内部list
     *
     * @param list
     */
    @Override
    public void setNewInstance(@Nullable List<Inventory> list) {
        super.setNewInstance(list);
        this.list = list;
    }
}