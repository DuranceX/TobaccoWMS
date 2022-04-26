package com.cardy.design.adapter;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cardy.design.R;
import com.cardy.design.entity.CustomerTest;
import com.cardy.design.entity.Material;
import com.cardy.design.entity.Supplier;
import com.cardy.design.viewmodel.MaterialViewModel;
import com.cardy.design.viewmodel.SupplierViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chad.library.adapter.base.module.BaseDraggableModule;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

import java.util.Arrays;
import java.util.List;

public class MaterialListAdapter extends BaseQuickAdapter<Material, MyMaterialViewHolder> implements DraggableModule {
    List<Material> list;
    MaterialViewModel viewModel;

    EditText editTextName,editTextModel,editTextPrice;

    public MaterialListAdapter(int layoutResId, MaterialViewModel viewModel) {
        super(layoutResId);
        this.viewModel = viewModel;
        initClickListener();
        initSwipeListener();
    }

    @Override
    protected void convert(@NonNull MyMaterialViewHolder holder, Material material) {
        holder.name.setText(material.getName());
        holder.model.setText(material.getModel());
        holder.price.setText("￥"+ material.getPrice());
    }

    @Override
    public void setNewInstance(@Nullable List<Material> list) {
        super.setNewInstance(list);
        this.list = list;
    }

    public void setMyList(List<Material> list){
        this.list = list;
    }

    public void initClickListener(){
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BottomDialog.show("修改原料信息",new OnBindView<BottomDialog>(R.layout.dialog_add_material) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        editTextName = v.findViewById(R.id.editTextName);
                        editTextModel = v.findViewById(R.id.editTextModel);
                        editTextPrice = v.findViewById(R.id.editTextPrice);

                        editTextName.setText(list.get(position).getName());
                        editTextModel.setText(list.get(position).getModel());
                        editTextModel.setFocusable(false);
                        editTextPrice.setText(String.valueOf(list.get(position).getPrice()));
                    }
                }).setOkButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
                    @Override
                    public boolean onClick(BottomDialog baseDialog, View v) {
                        String name = editTextName.getText().toString();
                        String model = editTextModel.getText().toString();
                        Double price = Double.parseDouble(editTextPrice.getText().toString());

                        Material material = new Material(name,model,price);
                        viewModel.updateMaterial(material);
                        return false;
                    }
                }).setCancelButton("取消");
            }
        });
    }

    public void initSwipeListener(){
        addDraggableModule(this);
        // 侧滑监听
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            Material material;

            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "view swiped start: " + pos);
                material = list.get(pos);
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "view swiped reset: " + pos);
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "View Swiped: " + pos);
                viewModel.deleteMaterial(material);
                PopTip.show("原料信息已删除","撤回").setOnButtonClickListener(new OnDialogButtonClickListener<PopTip>() {
                    @Override
                    public boolean onClick(PopTip baseDialog, View v) {
                        viewModel.insertMaterial(material);
                        PopTip.show("已撤销删除操作");
                        return false;
                    }
                });
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(ContextCompat.getColor(getContext(), R.color.background_gray));
            }
        };

        getDraggableModule().setSwipeEnabled(true);
        getDraggableModule().setOnItemSwipeListener(onItemSwipeListener);
        //END即只允许向右滑动
        getDraggableModule().getItemTouchHelperCallback().setSwipeMoveFlags(ItemTouchHelper.END);
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
