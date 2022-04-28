package com.cardy.design.util.diff;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.cardy.design.entity.PurchaseOrder;

public class PurchaseOrderDiffCallback extends DiffUtil.ItemCallback<PurchaseOrder> {
    @Override
    public boolean areItemsTheSame(@NonNull PurchaseOrder oldItem, @NonNull PurchaseOrder newItem) {
        return oldItem.getOrderId()==newItem.getOrderId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull PurchaseOrder oldItem, @NonNull PurchaseOrder newItem) {
        return false;
    }
}
