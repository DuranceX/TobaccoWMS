package com.cardy.design.adapter;

import android.app.DatePickerDialog;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cardy.design.R;
import com.cardy.design.entity.Inventory;
import com.cardy.design.entity.PurchaseOrder;
import com.cardy.design.viewmodel.InventoryViewModel;
import com.cardy.design.viewmodel.MaterialViewModel;
import com.cardy.design.viewmodel.PurchaseOrderViewModel;
import com.cardy.design.viewmodel.SupplierViewModel;
import com.cardy.design.widget.IconFontTextView;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PurchaseOrderListAdapter extends BaseQuickAdapter<PurchaseOrder, MyPurchaseOrderViewHolder> implements DraggableModule {
    List<PurchaseOrder> list;
    PurchaseOrderViewModel viewModel;
    MaterialViewModel materialViewModel;
    SupplierViewModel supplierViewModel;
    InventoryViewModel inventoryViewModel;

    TextView tvUserId, tvUserName, tvPurchaseDate,tvDeliveryDate;
    Spinner spinnerName, spinnerModel, spinnerSupplier;
    EditText etPrice, etCount,etComment;
    List<String> supplierList = new ArrayList<>();
    List<String> materialNameList = new ArrayList<>();
    List<String> materialModelList = new ArrayList<>();

    LocalDate date = LocalDate.now();

    public PurchaseOrderListAdapter(int layoutResId,PurchaseOrderViewModel viewModel,MaterialViewModel materialViewModel, SupplierViewModel supplierViewModel,InventoryViewModel inventoryViewModel) {
        super(layoutResId);
        this.viewModel = viewModel;
        this.materialViewModel = materialViewModel;
        this.supplierViewModel = supplierViewModel;
        this.inventoryViewModel = inventoryViewModel;
        new Thread(new Runnable() {
            @Override
            public void run() {
                materialNameList = materialViewModel.getMaterialNameList();
                supplierList = supplierViewModel.getNameList();
            }
        }).start();

        initClickListener();
        initSwipeListener();
    }

    @Override
    protected void convert(@NonNull MyPurchaseOrderViewHolder holder, PurchaseOrder order) {
        holder.orderId.setText(String.valueOf(order.getOrderId()));
        holder.userId.setText(order.getUserId());
        holder.userName.setText(order.getUserName());
        holder.materialName.setText(order.getMaterialName());
        holder.materialModel.setText(order.getMaterialModel());
        holder.count.setText("x" + String.valueOf(order.getCount()));
        holder.price.setText("￥" + String.valueOf(order.getPrice()));
        holder.purchaseDate.setText(order.getPurchaseDate());
        holder.deliveryDate.setText(order.getDeliveryDate());
        holder.comment.setText(order.getComment());
        holder.supplier.setText(order.getSupplier());
        if(order.getState().equals(PurchaseOrder.STATE_REQUEST)){
            holder.state.setBackgroundColor(getContext().getColor(R.color.orange_trans));
            holder.state.setTextColor(getContext().getColor(R.color.orange));
            holder.state.setText(PurchaseOrder.STATE_REQUEST);
        }else if(order.getState().equals(PurchaseOrder.STATE_DELIVERY)){
            holder.state.setBackgroundColor(getContext().getColor(R.color.blue_trans));
            holder.state.setTextColor(getContext().getColor(R.color.blue_light));
            holder.state.setText(PurchaseOrder.STATE_DELIVERY);
        }else if(order.getState().equals(PurchaseOrder.STATE_REFUSED)){
            holder.state.setBackgroundColor(getContext().getColor(R.color.red_trans));
            holder.state.setTextColor(getContext().getColor(R.color.red));
            holder.state.setText(PurchaseOrder.STATE_REFUSED);
        }else{
            holder.state.setBackgroundColor(getContext().getColor(R.color.green_trans));
            holder.state.setTextColor(getContext().getColor(R.color.green_soft));
            holder.state.setText(PurchaseOrder.STATE_COMPLETE);
        }
    }

    @NonNull
    @Override
    public BaseDraggableModule addDraggableModule(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new BaseDraggableModule(baseQuickAdapter);
    }

    public void initClickListener(){
        final String[] buttonStr = {"提交申请"};
        final PurchaseOrder[] order = new PurchaseOrder[1];
        final Inventory[] inventory = new Inventory[1];
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BottomDialog.show("修改采购订单",new OnBindView<BottomDialog>(R.layout.dialog_update_purchase_order) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        tvUserId = v.findViewById(R.id.textViewUserId);
                        tvUserName = v.findViewById(R.id.textViewUserName);
                        tvPurchaseDate = v.findViewById(R.id.textViewPurchaseDate);
                        tvDeliveryDate = v.findViewById(R.id.textViewDeliveryDate);
                        spinnerName = v.findViewById(R.id.spinnerName);
                        spinnerModel = v.findViewById(R.id.spinnerModel);
                        spinnerSupplier = v.findViewById(R.id.spinnerSupplier);
                        etPrice = v.findViewById(R.id.editTextPrice);
                        etCount = v.findViewById(R.id.editTextCount);
                        etComment = v.findViewById(R.id.editTextComment);

                        order[0] = list.get(position);
                        tvUserId.setText(order[0].getUserId());
                        tvUserName.setText(order[0].getUserName());
                        tvPurchaseDate.setText(order[0].getPurchaseDate());
                        etPrice.setText(String.valueOf(order[0].getPrice()));
                        etCount.setText(String.valueOf(order[0].getCount()));
                        tvDeliveryDate.setText(order[0].getDeliveryDate());
                        etComment.setText(order[0].getComment());
                        tvDeliveryDate.setEnabled(false);
                        etComment.setEnabled(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                inventory[0] = inventoryViewModel.getInventoryByModel(order[0].getMaterialModel());
                            }
                        }).start();

                        //底部菜单初始化
                        switch (order[0].getState()){
                            case PurchaseOrder.STATE_REQUEST:
                            case PurchaseOrder.STATE_REFUSED:
                                buttonStr[0] = "重新提交";
                                break;
                            case PurchaseOrder.STATE_DELIVERY:
                            case PurchaseOrder.STATE_COMPLETE:
                                buttonStr[0] = "确定";
                                etPrice.setEnabled(false);
                                etCount.setEnabled(false);
                                etComment.setEnabled(false);
                                spinnerName.setEnabled(false);
                                spinnerModel.setEnabled(false);
                                spinnerSupplier.setEnabled(false);
                                break;
                        }

                        //下拉框相关初始化
                        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item,materialNameList);
                        ArrayAdapter<String> supplierAdapter = new ArrayAdapter<String>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item,supplierList);
                        spinnerName.setAdapter(nameAdapter);
                        spinnerSupplier.setAdapter(supplierAdapter);
                        String name = order[0].getMaterialName();
                        String model = order[0].getMaterialModel();
                        String supplier = order[0].getSupplier();
                        spinnerName.setSelection(materialNameList.indexOf(name));
                        spinnerSupplier.setSelection(supplierList.indexOf(supplier));
                        Handler mHandler = new Handler(Looper.myLooper()){
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if(msg.what == 1){
                                    List<String> modelList = msg.getData().getStringArrayList("modelList");
                                    ArrayAdapter<String> modelAdapter = new ArrayAdapter<String>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item,modelList);
                                    spinnerModel.setAdapter(modelAdapter);
                                }
                            }
                        };
                        spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                materialModelList.clear();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String name = spinnerName.getSelectedItem().toString();
                                        materialModelList =  materialViewModel.getMaterialModelListByName(name);
                                        Message message = Message.obtain();
                                        message.what = 1;;
                                        Bundle bundle = new Bundle();
                                        bundle.putStringArrayList("modelList", (ArrayList<String>) materialModelList);
                                        message.setData(bundle);
                                        mHandler.sendMessage(message);
                                    }
                                }).start();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) { }
                        });

                        //日期相关初始化
                        tvPurchaseDate.setText(order[0].getPurchaseDate());
                        tvDeliveryDate.setText(order[0].getDeliveryDate());
//                        if(!order.getDeliveryDate().equals("")) {
//                            date = LocalDate.parse(order.getDeliveryDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//                        }
//                        tvCalendarButton.setOnClickListener(v1->{
//                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),new DatePickerDialog.OnDateSetListener() {
//                                @Override
//                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                                    date = LocalDate.of(year,month+1,day);
//                                    int monthValue = date.getMonthValue();
//                                    String dateString="";
//                                    if(monthValue<10)
//                                        dateString = date.getYear() + "-0" + date.getMonthValue() + "-" + date.getDayOfMonth();
//                                    else
//                                        dateString = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
//                                    etDeliveryDate.setText(dateString);
//                                }
//                            },date.getYear(),date.getMonthValue()-1,date.getDayOfMonth());
//                            datePickerDialog.show();
//                        });
                    }
                }).setOkButton(buttonStr[0], new OnDialogButtonClickListener<BottomDialog>() {
                    @Override
                    public boolean onClick(BottomDialog baseDialog, View v) {
                        String userId = tvUserId.getText().toString();
                        String userName = tvUserName.getText().toString();
                        String purchaseDate = tvPurchaseDate.getText().toString();
                        String name = spinnerName.getSelectedItem().toString();
                        String model = spinnerModel.getSelectedItem().toString();
                        String supplier = spinnerSupplier.getSelectedItem().toString();
                        Double price = Double.valueOf(etPrice.getText().toString());
                        int count = Integer.parseInt(etCount.getText().toString());
                        String deliveryDate = tvDeliveryDate.getText().toString();
                        switch (order[0].getState()){
                            case PurchaseOrder.STATE_REQUEST:
                                //TODO: 添加管理员判断
                                order[0].setState(PurchaseOrder.STATE_DELIVERY);
                                viewModel.updatePurchaseOrder(order[0]);
                                if(inventory[0]==null){
                                    Inventory temp = new Inventory(name,model,"",0,count,"",Inventory.TYPE_MATERIAL);
                                    inventoryViewModel.insertInventory(inventory[0]);
                                }else{
                                    inventory[0].setDeliveryCount(inventory[0].getDeliveryCount()+count);
                                    inventoryViewModel.updateInventory(inventory[0]);
                                }
                                break;
                            case PurchaseOrder.STATE_REFUSED:
                                order[0] = new PurchaseOrder(list.get(position).getOrderId(),userId,userName,name,model,count,price,supplier,purchaseDate,deliveryDate,PurchaseOrder.STATE_REQUEST,"");
                                viewModel.updatePurchaseOrder(order[0]);
                                break;
                            case PurchaseOrder.STATE_DELIVERY:
                            case PurchaseOrder.STATE_COMPLETE:
                                baseDialog.dismiss();
                                break;
                        }
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
            PurchaseOrder order;

            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Test: swipe pos", "view swiped start: " + pos);
                order = list.get(pos);
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "view swiped reset: " + pos);
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "View Swiped: " + pos);
                viewModel.deletePurchaseOrder(order);
                PopTip.show("采购订单已删除", "撤回").setOnButtonClickListener(new OnDialogButtonClickListener<PopTip>() {
                    @Override
                    public boolean onClick(PopTip baseDialog, View v) {
                        viewModel.insertPurchaseOrder(order);
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

    @Override
    public void setNewInstance(@Nullable List<PurchaseOrder> list) {
        super.setNewInstance(list);
        this.list = list;
    }

    public void setMyList(List<PurchaseOrder> list){
        this.list = list;
    }
}

class MyPurchaseOrderViewHolder extends BaseViewHolder{
    TextView orderId,userId,userName,materialName,materialModel,price,count,supplier,purchaseDate,deliveryDate,state,comment;
    public MyPurchaseOrderViewHolder(@NonNull View view) {
        super(view);
        orderId = view.findViewById(R.id.textViewOrderId);
        userId = view.findViewById(R.id.textViewUserId);
        userName = view.findViewById(R.id.textViewUserName);
        materialName = view.findViewById(R.id.textViewMaterialName);
        materialModel = view.findViewById(R.id.textViewMaterialModel);
        price = view.findViewById(R.id.textViewPrice);
        count = view.findViewById(R.id.textViewCount);
        supplier = view.findViewById(R.id.textViewSupplier);
        purchaseDate = view.findViewById(R.id.textViewPurchaseDate);
        deliveryDate = view.findViewById(R.id.textViewDeliveryDate);
        state = view.findViewById(R.id.textViewState);
        comment = view.findViewById(R.id.textViewComment);
    }
}

