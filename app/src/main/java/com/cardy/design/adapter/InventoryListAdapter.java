package com.cardy.design.adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cardy.design.R;
import com.cardy.design.entity.CustomerTest;
import com.cardy.design.entity.InventoryTest;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;

import java.util.List;

public class InventoryListAdapter extends BaseQuickAdapter<InventoryTest, MyInventoryViewHolder> {
    List<InventoryTest> list;

    public InventoryListAdapter(int layoutResId) {
        super(layoutResId);
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BottomDialog.show("实现跳转页面", "跳转到该库存的具体内容页面");
            }
        });
    }

    @Override
    protected void convert(@NonNull MyInventoryViewHolder myInventoryViewHolder, InventoryTest inventoryTest) {

    }
}


class MyInventoryViewHolder extends BaseViewHolder {
    TextView name,model,price;
    public MyInventoryViewHolder(@NonNull View view) {
        super(view);
    }
}