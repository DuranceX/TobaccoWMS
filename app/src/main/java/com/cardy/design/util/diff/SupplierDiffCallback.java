package com.cardy.design.util.diff;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.cardy.design.entity.Supplier;

public class SupplierDiffCallback extends DiffUtil.ItemCallback<Supplier> {
    @Override
    public boolean areItemsTheSame(@NonNull Supplier oldItem, @NonNull Supplier newItem) {
        return oldItem.getId()==newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Supplier oldItem, @NonNull Supplier newItem) {
//        return oldItem.getName().equals(newItem.getName())
//                && oldItem.getAddress().equals(newItem.getAddress())
//                && oldItem.getMainSupply().equals(newItem.getMainSupply())
//                && oldItem.getLogo().equals(newItem.getLogo())
//                && oldItem.getPriority() == newItem.getPriority();
        return false;
    }
}
