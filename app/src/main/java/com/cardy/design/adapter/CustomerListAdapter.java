package com.cardy.design.adapter;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cardy.design.R;
import com.cardy.design.entity.Customer;
import com.cardy.design.util.Util;
import com.cardy.design.viewmodel.CustomerViewModel;
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

import java.util.List;

public class CustomerListAdapter extends BaseQuickAdapter<Customer, MyCustomerViewHolder> implements DraggableModule {
    List<Customer> list;
    CustomerViewModel viewModel;
    ActivityResultLauncher<Intent> launcher;
    ImageView imageViewLogo;
    EditText editTextLogo;

    public CustomerListAdapter(int layoutResId,CustomerViewModel viewModel,ActivityResultLauncher<Intent> launcher) {
        super(layoutResId);
        this.viewModel = viewModel;
        this.launcher = launcher;
        initClickListener();
        initSwipeListener();
    }

    @Override
    protected void convert(@NonNull MyCustomerViewHolder holder, Customer customer) {
        try{
            if(!customer.getLogo().equals("")){
                if(customer.getLogo().startsWith("content")){
                    Uri pathUri = Util.getImagePath(getContext(),customer.getLogo());
                    holder.logo.setImageURI(pathUri);
                }
                else if(customer.getLogo().startsWith("http")){
                    Picasso.with(getContext()).load(customer.getLogo()).into(holder.logo);
                }
                else
                    throw new Exception();
            }
            else
                throw new Exception();
        }catch (Exception exception){
            holder.logo.setImageDrawable(getContext().getDrawable(R.drawable.avatar_placeholder));
        }
        holder.name.setText(customer.getName());
        holder.address.setText(customer.getAddress());
        holder.purchase.setText(customer.getMainPurchase());
    }

    @Override
    public void setNewInstance(@Nullable List<Customer> list) {
        super.setNewInstance(list);
        this.list = list;
    }

    public void setMyList(List<Customer> list){
        this.list = list;
    }

    public void setImage(Uri uri){
        if(imageViewLogo!=null && editTextLogo!=null){
            Picasso.with(getContext()).load(uri).into(imageViewLogo);
            editTextLogo.setText(uri.toString());
        }
    }

    public void initClickListener(){
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                final TextView[] editTextMain = new TextView[1];
                final EditText[] editTextName = new EditText[1];
                final EditText[] editTextAddress = new EditText[1];
                final RadioButton[] radioButtonLow = new RadioButton[1];
                final RadioButton[] radioButtonMid = new RadioButton[1];
                final RadioButton[] radioButtonHigh = new RadioButton[1];
                BottomDialog.show("修改客户信息",new OnBindView<BottomDialog>(R.layout.dialog_add_customer_supplier) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        imageViewLogo = v.findViewById(R.id.imageViewLogo);
                        editTextLogo = v.findViewById(R.id.editTextLogo);
                        editTextName[0] = v.findViewById(R.id.editTextName);
                        editTextAddress[0] = v.findViewById(R.id.editTextModel);
                        editTextMain[0] = v.findViewById(R.id.editTextMain);
                        radioButtonLow[0] = v.findViewById(R.id.radioButtonLow);
                        radioButtonMid[0] = v.findViewById(R.id.radioButtonMid);
                        radioButtonHigh[0] = v.findViewById(R.id.radioButtonHigh);

                        //填充数值
                        if(!list.get(position).getLogo().equals("")){
                            if(list.get(position).getLogo().startsWith("content")){
                                Uri pathUri = Util.getImagePath(getContext(),list.get(position).getLogo());
                                imageViewLogo.setImageURI(pathUri);
                            }
                            else if(list.get(position).getLogo().startsWith("http")){
                                Picasso.with(getContext()).load(list.get(position).getLogo()).into(imageViewLogo);
                            }
                            else
                                imageViewLogo.setImageDrawable(getContext().getDrawable(R.drawable.product_placeholder));
                        }
                        editTextLogo.setText(list.get(position).getLogo());
                        editTextName[0].setText(list.get(position).getName());
                        editTextAddress[0].setText(list.get(position).getAddress());
                        editTextMain[0].setText(list.get(position).getMainPurchase());
                        switch (list.get(position).getPriority()) {
                            case Customer.PRIORITY_HIGH:
                                radioButtonHigh[0].setChecked(true);
                                break;
                            case Customer.PRIORITY_MIDDLE:
                                radioButtonMid[0].setChecked(true);
                                break;
                            case Customer.PRIORITY_LOW:
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
                        String main = editTextMain[0].getText().toString();
                        int priority = 0;
                        if (radioButtonLow[0].isChecked())
                            priority = Customer.PRIORITY_LOW;
                        else if (radioButtonMid[0].isChecked())
                            priority = Customer.PRIORITY_MIDDLE;
                        else if (radioButtonHigh[0].isChecked())
                            priority = Customer.PRIORITY_HIGH;

                        Customer customer = new Customer(list.get(position).getId(),name, address, priority, logo, main);
                        viewModel.updateCustomer(customer);
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
            Customer customer;

            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "view swiped start: " + pos);
                customer = list.get(pos);
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "view swiped reset: " + pos);
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "View Swiped: " + pos);
                viewModel.deleteCustomer(customer);
                PopTip.show("客户信息已删除","撤回").setOnButtonClickListener(new OnDialogButtonClickListener<PopTip>() {
                    @Override
                    public boolean onClick(PopTip baseDialog, View v) {
                        PopTip.show("已撤销删除操作");
                        viewModel.insertCustomer(customer);
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

class MyCustomerViewHolder extends BaseViewHolder{
    TextView name,address,purchase;
    ImageView logo;
    public MyCustomerViewHolder(@NonNull View view) {
        super(view);
        logo = view.findViewById(R.id.imageViewLogo);
        name = view.findViewById(R.id.customerName);
        address = view.findViewById(R.id.customerAddress);
        purchase = view.findViewById(R.id.customerMainPurchase);
    }
}