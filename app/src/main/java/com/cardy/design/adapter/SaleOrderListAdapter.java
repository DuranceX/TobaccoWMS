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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cardy.design.R;
import com.cardy.design.entity.Inventory;
import com.cardy.design.entity.PurchaseOrder;
import com.cardy.design.entity.SaleOrder;
import com.cardy.design.viewmodel.CustomerViewModel;
import com.cardy.design.viewmodel.InventoryViewModel;
import com.cardy.design.viewmodel.ProductViewModel;
import com.cardy.design.viewmodel.SaleOrderViewModel;
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
public class SaleOrderListAdapter extends BaseQuickAdapter<SaleOrder, MySaleOrderViewHolder> implements DraggableModule {
    List<SaleOrder> list;
    SaleOrderViewModel viewModel;
    ProductViewModel productViewModel;
    CustomerViewModel customerViewModel;
    InventoryViewModel inventoryViewModel;

    TextView tvUserId, tvUserName, tvSaleDate, tvDeliveryDate;
    Spinner spinnerName, spinnerModel, spinnerCustomer;
    EditText etPrice, etCount, etComment;
    List<String> customerList = new ArrayList<>();
    List<String> productNameList = new ArrayList<>();
    List<String> productModelList = new ArrayList<>();

    LocalDate date = LocalDate.now();

    public SaleOrderListAdapter(int layoutResId, SaleOrderViewModel viewModel, ProductViewModel productViewModel, CustomerViewModel customerViewModel,InventoryViewModel inventoryViewModel) {
        super(layoutResId);
        this.viewModel = viewModel;
        this.productViewModel = productViewModel;
        this.customerViewModel = customerViewModel;
        this.inventoryViewModel = inventoryViewModel;
        new Thread(new Runnable() {
            @Override
            public void run() {
                productNameList = productViewModel.getProductNameList();
                customerList = customerViewModel.getNameList();
            }
        }).start();

        initClickListener();
        initSwipeListener();
    }

    @Override
    protected void convert(@NonNull MySaleOrderViewHolder holder, SaleOrder order) {
        holder.orderId.setText(String.valueOf(order.getOrderId()));
        holder.userId.setText(order.getUserId());
        holder.userName.setText(order.getUserName());
        holder.productName.setText(order.getProductName());
        holder.productModel.setText(order.getProductModel());
        holder.count.setText("x" + String.valueOf(order.getCount()));
        holder.price.setText("￥" + String.valueOf(order.getPrice()));
        holder.saleDate.setText(order.getSaleDate());
        holder.deliveryDate.setText(order.getDeliveryDate());
        holder.comment.setText(order.getComment());
        holder.customer.setText(order.getCustomer());
        if (order.getState().equals(SaleOrder.STATE_REQUEST)) {
            holder.state.setBackgroundColor(getContext().getColor(R.color.orange_trans));
            holder.state.setTextColor(getContext().getColor(R.color.orange));
            holder.state.setText(SaleOrder.STATE_REQUEST);
        } else if(order.getState().equals(SaleOrder.STATE_WAIT)){
            holder.state.setBackgroundColor(getContext().getColor(R.color.purple_trans));
            holder.state.setTextColor(getContext().getColor(R.color.purple));
            holder.state.setText(SaleOrder.STATE_WAIT);
        } else if (order.getState().equals(SaleOrder.STATE_DELIVERY)) {
            holder.state.setBackgroundColor(getContext().getColor(R.color.blue_trans));
            holder.state.setTextColor(getContext().getColor(R.color.blue_light));
            holder.state.setText(SaleOrder.STATE_DELIVERY);
        } else if (order.getState().equals(SaleOrder.STATE_REFUSED)) {
            holder.state.setBackgroundColor(getContext().getColor(R.color.red_trans));
            holder.state.setTextColor(getContext().getColor(R.color.red));
            holder.state.setText(SaleOrder.STATE_REFUSED);
        } else {
            holder.state.setBackgroundColor(getContext().getColor(R.color.green_trans));
            holder.state.setTextColor(getContext().getColor(R.color.green_soft));
            holder.state.setText(SaleOrder.STATE_COMPLETE);
        }
    }

    @NonNull
    @Override
    public BaseDraggableModule addDraggableModule(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new BaseDraggableModule(baseQuickAdapter);
    }

    @Override
    public void setNewInstance(@Nullable List<SaleOrder> list) {
        super.setNewInstance(list);
        this.list = list;
    }

    public void setMyList(List<SaleOrder> list) {
        this.list = list;
    }

    public void initClickListener() {
        final String[] buttonStr = {"提交申请"};
        final SaleOrder[] order = new SaleOrder[1];
        final Inventory[] inventory = new Inventory[1];
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BottomDialog.show("修改销售订单", new OnBindView<BottomDialog>(R.layout.dialog_update_sale_order) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        tvUserId = v.findViewById(R.id.textViewUserId);
                        tvUserName = v.findViewById(R.id.textViewUserName);
                        tvSaleDate = v.findViewById(R.id.textViewSaleDate);
                        tvDeliveryDate = v.findViewById(R.id.textViewDeliveryDate);
                        spinnerName = v.findViewById(R.id.spinnerName);
                        spinnerModel = v.findViewById(R.id.spinnerModel);
                        spinnerCustomer = v.findViewById(R.id.spinnerCustomer);
                        etPrice = v.findViewById(R.id.editTextPrice);
                        etCount = v.findViewById(R.id.editTextCount);
                        etComment = v.findViewById(R.id.editTextComment);

                        order[0] = list.get(position);
                        tvUserId.setText(order[0].getUserId());
                        tvUserName.setText(order[0].getUserName());
                        tvSaleDate.setText(order[0].getSaleDate());
                        etPrice.setText(String.valueOf(order[0].getPrice()));
                        etCount.setText(String.valueOf(order[0].getCount()));
                        tvDeliveryDate.setText(order[0].getDeliveryDate());
                        etComment.setText(order[0].getComment());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                inventory[0] = inventoryViewModel.getInventoryByModel(order[0].getProductModel());
                            }
                        }).start();

                        //底部菜单初始化
                        switch (order[0].getState()){
                            case SaleOrder.STATE_DELIVERY:
                                buttonStr[0] = "确认送达";
                                etPrice.setEnabled(false);
                                etCount.setEnabled(false);
                                spinnerName.setEnabled(false);
                                spinnerModel.setEnabled(false);
                                spinnerCustomer.setEnabled(false);
                                break;
                            case SaleOrder.STATE_REQUEST:
                            case SaleOrder.STATE_REFUSED:
                                buttonStr[0] = "重新提交";
                                break;
                            case SaleOrder.STATE_WAIT:
                            case SaleOrder.STATE_COMPLETE:
                                buttonStr[0] = "确定";
                                etPrice.setEnabled(false);
                                etCount.setEnabled(false);
                                etComment.setEnabled(false);
                                spinnerName.setEnabled(false);
                                spinnerModel.setEnabled(false);
                                spinnerCustomer.setEnabled(false);
                                break;
                        }

                        //下拉框相关初始化
                        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item, productNameList);
                        ArrayAdapter<String> supplierAdapter = new ArrayAdapter<String>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item, customerList);
                        spinnerName.setAdapter(nameAdapter);
                        spinnerCustomer.setAdapter(supplierAdapter);
                        String name = order[0].getProductName();
                        String model = order[0].getProductModel();
                        String supplier = order[0].getCustomer();
                        spinnerName.setSelection(productNameList.indexOf(name));
                        spinnerCustomer.setSelection(customerList.indexOf(supplier));
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
                                productModelList.clear();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String name = spinnerName.getSelectedItem().toString();
                                        productModelList =  productViewModel.getProductModelListByName(name);
                                        Message message = Message.obtain();
                                        message.what = 1;;
                                        Bundle bundle = new Bundle();
                                        bundle.putStringArrayList("modelList", (ArrayList<String>) productModelList);
                                        message.setData(bundle);
                                        mHandler.sendMessage(message);
                                    }
                                }).start();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) { }
                        });

                        //日期相关初始化
                        tvSaleDate.setText(order[0].getSaleDate());
                        tvDeliveryDate.setText(order[0].getDeliveryDate());
                    }
                }).setOkButton(buttonStr[0], new OnDialogButtonClickListener<BottomDialog>() {
                    @Override
                    public boolean onClick(BottomDialog baseDialog, View v) {
                        String userId = tvUserId.getText().toString();
                        String userName = tvUserName.getText().toString();
                        String saleDate = tvSaleDate.getText().toString();
                        String name = spinnerName.getSelectedItem().toString();
                        String model = spinnerModel.getSelectedItem().toString();
                        String customer = spinnerCustomer.getSelectedItem().toString();
                        Double price = Double.valueOf(etPrice.getText().toString());
                        int count = Integer.parseInt(etCount.getText().toString());
                        String deliveryDate = tvDeliveryDate.getText().toString();

                        switch (order[0].getState()){
                            case SaleOrder.STATE_REQUEST:
                            case SaleOrder.STATE_REFUSED:
                                order[0] = new SaleOrder(list.get(position).getOrderId(),userId,userName,name,model,count,price,customer,saleDate,deliveryDate,PurchaseOrder.STATE_REQUEST,"");
                                viewModel.updateSaleOrder(order[0]);
                                break;
                            case SaleOrder.STATE_DELIVERY:
                                order[0] = new SaleOrder(list.get(position).getOrderId(),userId,userName,name,model,count,price,customer,saleDate,deliveryDate,PurchaseOrder.STATE_COMPLETE,"");
                                viewModel.updateSaleOrder(order[0]);
                                inventory[0].setDeliveryCount(inventory[0].getDeliveryCount()-count);
                                inventoryViewModel.updateInventory(inventory[0]);
                                break;
                            case SaleOrder.STATE_COMPLETE:
                            case SaleOrder.STATE_WAIT:
                                baseDialog.dismiss();
                                break;
                        }
                        return false;
                    }
                }).setCancelButton("取消");
            }
        });
    }

    public void initSwipeListener() {
        addDraggableModule(this);
        // 侧滑监听
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            SaleOrder order;

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
                viewModel.deleteSaleOrder(order);
                PopTip.show("销售订单已删除", "撤回").setOnButtonClickListener(new OnDialogButtonClickListener<PopTip>() {
                    @Override
                    public boolean onClick(PopTip baseDialog, View v) {
                        viewModel.insertSaleOrder(order);
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
}

class MySaleOrderViewHolder extends BaseViewHolder {
    TextView orderId, userId, userName, productName, productModel, price, count, customer, saleDate, deliveryDate, state, comment;

    public MySaleOrderViewHolder(@NonNull View view) {
        super(view);
        orderId = view.findViewById(R.id.textViewOrderId);
        userId = view.findViewById(R.id.textViewUserId);
        userName = view.findViewById(R.id.textViewUserName);
        productName = view.findViewById(R.id.textViewProductName);
        productModel = view.findViewById(R.id.textViewProductModel);
        price = view.findViewById(R.id.textViewPrice);
        count = view.findViewById(R.id.textViewCount);
        customer = view.findViewById(R.id.textViewCustomer);
        saleDate = view.findViewById(R.id.textViewSaleDate);
        deliveryDate = view.findViewById(R.id.textViewDeliveryDate);
        state = view.findViewById(R.id.textViewState);
        comment = view.findViewById(R.id.textViewComment);
    }
}