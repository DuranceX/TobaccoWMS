package com.cardy.design.util.diff;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.cardy.design.entity.Customer;

public class CustomerDIffCallback extends DiffUtil.ItemCallback<Customer> {
    @Override
    public boolean areItemsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
        return false;
    }
}
