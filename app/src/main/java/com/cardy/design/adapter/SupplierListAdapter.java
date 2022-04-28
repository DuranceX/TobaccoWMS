package com.cardy.design.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cardy.design.R;
import com.cardy.design.entity.Supplier;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class SupplierListAdapter extends BaseQuickAdapter<Supplier, MySupplierViewHolder> implements DraggableModule {
    List<Supplier> list;
    SupplierViewModel viewModel;
    ActivityResultLauncher<Intent> launcher;
    ImageView imageViewLogo;
    EditText editTextLogo;

    public SupplierListAdapter(int layoutResId, SupplierViewModel viewModel,ActivityResultLauncher<Intent> intentActivityResultLauncher) {
        super(layoutResId);
        this.viewModel = viewModel;
        this.launcher = intentActivityResultLauncher;
        initClickListener();
        initSwipeListener();
    }

    @Override
    protected void convert(@NonNull MySupplierViewHolder holder, Supplier supplier) {
        try{
            if(!supplier.getLogo().equals("")){
                Picasso.with(getContext()).load(supplier.getLogo()).into(holder.logo);
            }
            else
                throw new Exception();
        }catch (Exception exception){
            holder.logo.setImageDrawable(getContext().getDrawable(R.drawable.avatar_placeholder));
        }
        holder.name.setText(supplier.getName());
        holder.address.setText(supplier.getAddress());
        holder.supply.setText(supplier.getMainSupply());
    }

    /**
     * 重写setList方法，更新列表数据
     *
     * @param list
     */
    public void setMyList(List<Supplier> list) {
        this.list = list;
    }

    /**
     * 重写setNewInstance方法，初始化数据时同时更新adapter内部list
     *
     * @param list
     */
    @Override
    public void setNewInstance(@Nullable List<Supplier> list) {
        super.setNewInstance(list);
        this.list = list;
    }

    @NonNull
    @Override
    public BaseDraggableModule addDraggableModule(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new BaseDraggableModule(baseQuickAdapter);
    }

    /**
     * 初始化Item的点击事件
     */
    public void initClickListener() {
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                final TextView[] textViewNameLabel = new TextView[1];
                final TextView[] textViewAddressLabel = new TextView[1];
                final TextView[] textViewMainLabel = new TextView[1];
                final TextView[] textViewPriorityLabel = new TextView[1];
                final TextView[] textViewMain = new TextView[1];
                final EditText[] editTextName = new EditText[1];
                final EditText[] editTextAddress = new EditText[1];
                final RadioButton[] radioButtonLow = new RadioButton[1];
                final RadioButton[] radioButtonMid = new RadioButton[1];
                final RadioButton[] radioButtonHigh = new RadioButton[1];
                BottomDialog.show("修改供货商信息", new OnBindView<BottomDialog>(R.layout.dialog_add_customer_supplier) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        imageViewLogo = v.findViewById(R.id.imageViewLogo);
                        editTextLogo = v.findViewById(R.id.editTextLogo);
                        textViewNameLabel[0] = v.findViewById(R.id.textViewNameLabel);
                        textViewAddressLabel[0] = v.findViewById(R.id.textViewModelLabel);
                        textViewMainLabel[0] = v.findViewById(R.id.textViewPriceLabel);
                        textViewPriorityLabel[0] = v.findViewById(R.id.textViewPriorityLabel);
                        editTextName[0] = v.findViewById(R.id.editTextName);
                        editTextAddress[0] = v.findViewById(R.id.editTextModel);
                        textViewMain[0] = v.findViewById(R.id.textViewMain);
                        radioButtonLow[0] = v.findViewById(R.id.radioButtonLow);
                        radioButtonMid[0] = v.findViewById(R.id.radioButtonMid);
                        radioButtonHigh[0] = v.findViewById(R.id.radioButtonHigh);

                        //修改界面为供货商
                        textViewNameLabel[0].setText("供货商名称：");
                        textViewAddressLabel[0].setText("供货商地址：");
                        textViewMainLabel[0].setText("主要供货原料：");
                        textViewPriorityLabel[0].setText("供货商优先级：");

                        //填充数值
                        if (!list.get(position).getLogo().equals("")) {
                            Picasso.with(getContext()).load(list.get(position).getLogo()).into(imageViewLogo);
                        }
                        editTextLogo.setText(list.get(position).getLogo());
                        editTextName[0].setText(list.get(position).getName());
                        editTextAddress[0].setText(list.get(position).getAddress());
                        textViewMain[0].setText(list.get(position).getMainSupply());
                        switch (list.get(position).getPriority()) {
                            case Supplier.PRIORITY_HIGH:
                                radioButtonHigh[0].setChecked(true);
                                break;
                            case Supplier.PRIORITY_MIDDLE:
                                radioButtonMid[0].setChecked(true);
                                break;
                            case Supplier.PRIORITY_LOW:
                                radioButtonLow[0].setChecked(true);
                                break;
                        }

                        //添加点击事件
                        imageViewLogo.setOnClickListener(imageView->{
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            launcher.launch(intent);
                        });
                    }
                }).setOkButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
                    @Override
                    public boolean onClick(BottomDialog baseDialog, View v) {
                        String name = editTextName[0].getText().toString();
                        String logo = editTextLogo.getText().toString();
                        String address = editTextAddress[0].getText().toString();
                        String main = textViewMain[0].getText().toString();
                        int priority = 0;
                        if (radioButtonLow[0].isChecked())
                            priority = Supplier.PRIORITY_LOW;
                        else if (radioButtonMid[0].isChecked())
                            priority = Supplier.PRIORITY_MIDDLE;
                        else if (radioButtonHigh[0].isChecked())
                            priority = Supplier.PRIORITY_HIGH;

                        Supplier supplier = new Supplier(list.get(position).getId(),name, address, priority, logo, main);
                        viewModel.updateSupplier(supplier);
                        return false;
                    }
                }).setCancelButton("取消");
            }
        });
    }

    public void setImage(Uri uri){
        if(imageViewLogo!=null && editTextLogo!=null){
            Picasso.with(getContext()).load(uri).into(imageViewLogo);
            editTextLogo.setText(uri.toString());
        }
    }

    /**
     * 初始化Item的侧滑事件
     */
    public void initSwipeListener() {
        addDraggableModule(this);
        // 侧滑监听
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            Supplier supplier;

            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "view swiped start: " + pos);
                supplier = list.get(pos);
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "view swiped reset: " + pos);
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "View Swiped: " + pos);
                viewModel.deleteSupplier(supplier);
                PopTip.show("供货商信息已删除", "撤回").setOnButtonClickListener(new OnDialogButtonClickListener<PopTip>() {
                    @Override
                    public boolean onClick(PopTip baseDialog, View v) {
                        PopTip.show("已撤销删除操作");
                        viewModel.insertSupplier(supplier);
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
}

class MySupplierViewHolder extends BaseViewHolder {
    TextView name, address, supply;
    ImageView logo;

    public MySupplierViewHolder(@NonNull View view) {
        super(view);
        logo = view.findViewById(R.id.imageViewLogo);
        name = view.findViewById(R.id.supplierName);
        address = view.findViewById(R.id.supplierAddress);
        supply = view.findViewById(R.id.supplierMainSupply);
    }
}