package com.cardy.design.util.diff;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.cardy.design.entity.SaleOrder;

public class SaleOrderDiffCallback extends DiffUtil.ItemCallback<SaleOrder> {
    @Override
    public boolean areItemsTheSame(@NonNull SaleOrder oldItem, @NonNull SaleOrder newItem) {
        return oldItem.getOrderId()==newItem.getOrderId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull SaleOrder oldItem, @NonNull SaleOrder newItem) {
        return false;
    }
}
