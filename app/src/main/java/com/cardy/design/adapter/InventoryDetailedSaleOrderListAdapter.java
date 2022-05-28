package com.cardy.design.adapter;

import androidx.annotation.NonNull;

import com.cardy.design.R;
import com.cardy.design.entity.PurchaseOrder;
import com.cardy.design.entity.SaleOrder;
import com.chad.library.adapter.base.BaseQuickAdapter;

public class InventoryDetailedSaleOrderListAdapter extends BaseQuickAdapter<SaleOrder,MySaleOrderViewHolder> {


    public InventoryDetailedSaleOrderListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull MySaleOrderViewHolder holder, SaleOrder order) {
        holder.orderId.setText(String.valueOf(order.getOrderId()));
        holder.userId.setText(order.getUserId());
        holder.userName.setText(order.getUserName());
        holder.productName.setText(order.getProductName());
        holder.productModel.setText(order.getProductModel());
        holder.count.setText("x" + String.valueOf(order.getCount()));
        holder.price.setText("￥" + String.valueOf(order.getPrice()));
        holder.saleDate.setText(order.getSaleDate());
        holder.deliveryDate.setText(order.getDeliveryDate());
        holder.comment.setText(order.getComment());
        holder.customer.setText(order.getCustomer());

        holder.state.setBackgroundColor(getContext().getColor(R.color.blue_trans));
        holder.state.setTextColor(getContext().getColor(R.color.blue_light));
        holder.state.setText(SaleOrder.STATE_DELIVERY);
    }
}
