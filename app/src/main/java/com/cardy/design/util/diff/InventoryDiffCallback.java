package com.cardy.design.util.diff;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.cardy.design.entity.Inventory;

public class InventoryDiffCallback extends DiffUtil.ItemCallback<Inventory> {
    @Override
    public boolean areItemsTheSame(@NonNull Inventory oldItem, @NonNull Inventory newItem) {
        return oldItem.getModel().equals(newItem.getModel());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Inventory oldItem, @NonNull Inventory newItem) {
        return false;
    }
}
