package com.cardy.design.adapter;

import androidx.annotation.NonNull;

import com.cardy.design.R;
import com.cardy.design.entity.PurchaseOrder;
import com.cardy.design.entity.SaleOrder;
import com.chad.library.adapter.base.BaseQuickAdapter;

public class InventoryDetailedPurchaseOrderListAdapter extends BaseQuickAdapter<PurchaseOrder,MyPurchaseOrderViewHolder> {


    public InventoryDetailedPurchaseOrderListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull MyPurchaseOrderViewHolder holder, PurchaseOrder order) {
        holder.orderId.setText(String.valueOf(order.getOrderId()));
        holder.userId.setText(order.getUserId());
        holder.userName.setText(order.getUserName());
        holder.materialName.setText(order.getMaterialName());
        holder.materialModel.setText(order.getMaterialModel());
        holder.count.setText("x" + String.valueOf(order.getCount()));
        holder.price.setText("￥" + String.valueOf(order.getPrice()));
        holder.purchaseDate.setText(order.getPurchaseDate());
        holder.deliveryDate.setText(order.getDeliveryDate());
        holder.comment.setText(order.getComment());
        holder.supplier.setText(order.getSupplier());

        holder.state.setBackgroundColor(getContext().getColor(R.color.blue_trans));
        holder.state.setTextColor(getContext().getColor(R.color.blue_light));
        holder.state.setText(SaleOrder.STATE_DELIVERY);
    }
}
