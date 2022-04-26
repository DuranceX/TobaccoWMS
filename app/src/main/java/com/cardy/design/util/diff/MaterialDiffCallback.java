package com.cardy.design.util.diff;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.cardy.design.entity.Material;

public class MaterialDiffCallback extends DiffUtil.ItemCallback<Material> {
    @Override
    public boolean areItemsTheSame(@NonNull Material oldItem, @NonNull Material newItem) {
        return oldItem.getModel().equals(newItem.getModel());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Material oldItem, @NonNull Material newItem) {
        return false;
    }
}
