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
import com.cardy.design.entity.Product;
import com.cardy.design.entity.PurchaseOrder;
import com.cardy.design.entity.SaleOrder;
import com.cardy.design.util.Util;
import com.cardy.design.viewmodel.InventoryViewModel;
import com.cardy.design.viewmodel.ProductViewModel;
import com.cardy.design.viewmodel.SaleOrderViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class InventoryProductListAdapter extends BaseQuickAdapter<Inventory, MyInventoryViewHolder> {
    InventoryViewModel viewModel;
    ProductViewModel productViewModel;
    SaleOrderViewModel saleOrderViewModel;
    List<Inventory> list;
    int SET_IMAGE = 1;

    public InventoryProductListAdapter(int layoutResId, InventoryViewModel viewModel, ProductViewModel productViewModel,SaleOrderViewModel saleOrderViewModel) {
        super(layoutResId);
        this.viewModel = viewModel;
        this.productViewModel = productViewModel;
        this.saleOrderViewModel = saleOrderViewModel;
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
                        final List<SaleOrder>[] orderList = new List[]{new ArrayList<>()};
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
                        rvInventory.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvInventory.setAdapter(inventoryDetailedListAdapter);
                        inventoryDetailedListAdapter.setList(detailList);

                        InventoryDetailedSaleOrderListAdapter inventoryDetailedSaleOrderListAdapter = new InventoryDetailedSaleOrderListAdapter(R.layout.item_sale_order);
                        rvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvOrder.setAdapter(inventoryDetailedSaleOrderListAdapter);

                        Handler mHandler = new Handler(Looper.myLooper()){
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if(msg.what == 999){
                                    inventoryDetailedSaleOrderListAdapter.setList(orderList[0]);
                                }
                            }
                        };

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                orderList[0] = saleOrderViewModel.getSelectedStateOrder("运输中");
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
                    message.what = SET_IMAGE;
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }
            }).start();
        }
    }

    /**
     * 重写setList方法，更新列表数据
     *
     * @param list
     */
    public void setMyList(List<Inventory> list) {
        this.list = list;
    }

    @Override
    public void setNewInstance(@Nullable List<Inventory> list) {
        super.setNewInstance(list);
        this.list = list;
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