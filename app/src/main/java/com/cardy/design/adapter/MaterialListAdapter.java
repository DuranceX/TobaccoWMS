package com.cardy.design.adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cardy.design.R;
import com.cardy.design.entity.CustomerTest;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.module.BaseDraggableModule;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;

import java.util.Arrays;
import java.util.List;

public class MaterialListAdapter extends BaseQuickAdapter<CustomerTest, MyMaterialViewHolder> implements DraggableModule {
    List<CustomerTest> newList;

    public MaterialListAdapter(int layoutResId) {
        super(layoutResId);
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BottomDialog.show("修改原料信息",new OnBindView<BottomDialog>(R.layout.dialog_add_material) {
                    //TODO: 添加”修改“功能
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        EditText editTextName,editTextModel,editTextPrice;

                        editTextName = v.findViewById(R.id.editTextName);
                        editTextModel = v.findViewById(R.id.editTextModel);
                        editTextPrice = v.findViewById(R.id.editTextPrice);

                        editTextName.setText(newList.get(position).getName());
                        editTextModel.setText(newList.get(position).getAddress());
                        editTextPrice.setText("100");
                    }
                }).setOkButton("确定").setCancelButton("取消");
            }
        });
        addDraggableModule(this);
    }

    @Override
    protected void convert(@NonNull MyMaterialViewHolder holder, CustomerTest customer) {
        holder.name.setText(customer.getName());
        holder.model.setText(customer.getAddress());
        holder.price.setText("100");
    }

    public void setList(List<CustomerTest> list){
        this.newList = list;
    }

    @NonNull
    @Override
    public BaseDraggableModule addDraggableModule(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new BaseDraggableModule(baseQuickAdapter);
    }
}

class MyMaterialViewHolder extends BaseViewHolder{
    TextView name,model,price;
    public MyMaterialViewHolder(@NonNull View view) {
        super(view);
        name = view.findViewById(R.id.materialName);
        model = view.findViewById(R.id.materialModel);
        price = view.findViewById(R.id.materialPrice);
    }
}
