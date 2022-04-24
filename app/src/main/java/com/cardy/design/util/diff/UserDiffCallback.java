package com.cardy.design.util.diff;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.cardy.design.entity.User;

public class UserDiffCallback extends DiffUtil.ItemCallback<User> {

    /**
     * Determine if it is the same item
     * <p>
     * 判断是否是同一个item
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return
     */
    @Override
    public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    /**
     * When it is the same item, judge whether the content has changed.
     * <p>
     * 当是同一个item时，再判断内容是否发生改变
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return
     */
    @Override
    public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
        return oldItem.getUsername().equals(newItem.getUsername())
                && oldItem.getPassword().equals(newItem.getPassword())
                && oldItem.getPermission() == newItem.getPermission();
    }
}
