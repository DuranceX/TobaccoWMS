package com.cardy.design.fragment;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cardy.design.R;
import com.cardy.design.adapter.InventoryListAdapter;
import com.cardy.design.entity.Inventory;
import com.cardy.design.entity.SaleOrder;
import com.cardy.design.util.diff.InventoryDiffCallback;
import com.cardy.design.viewmodel.InventoryViewModel;
import com.cardy.design.viewmodel.SaleOrderViewModel;
import com.cardy.design.widget.IconFontTextView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

import java.time.LocalDate;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class InventoryProductFragment extends Fragment {

    InventoryListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView menuButton;
    Button inButton,outButton;
    InventoryViewModel viewModel;
    SaleOrderViewModel saleOrderViewModel;

    List<SaleOrder> orders;
    List<Inventory> inventoryList;
    LocalDate date = LocalDate.now();

    Spinner spinner;
    TextView tvName, tvModel, tvCustomer, tvCount, tvPrice, tvSaleDate,calendar;
    EditText etDeliveryDate;

    public InventoryProductFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inventory_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        saleOrderViewModel = new ViewModelProvider(this).get(SaleOrderViewModel.class);
        adapter = new InventoryListAdapter(R.layout.item_inventory_product,viewModel);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.inventoryProductRecycleview);
        searchView = getView().findViewById(R.id.inventoryProductSearchView);
        inButton = getView().findViewById(R.id.inventoryProductInButton);
        outButton = getView().findViewById(R.id.inventoryProductOutButton);
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty_layout);

        adapter.setDiffCallback(new InventoryDiffCallback());

        viewModel.getAllProductInventory().observe(getActivity(), new Observer<List<Inventory>>() {
            @Override
            public void onChanged(List<Inventory> inventories) {
                if (adapter.getData().size() == 0)
                    adapter.setNewInstance(inventories);
                //通过setDiffNewData来通知adapter数据发生变化，并保留动画
                adapter.setDiffNewData(inventories);
                inventoryList = inventories;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                orders = saleOrderViewModel.getSelectedStateOrder(SaleOrder.STATE_DELIVERY);
            }
        }).start();

        //TODO: 完成入库功能
        inButton.setOnClickListener(v->{
            BottomDialog.show("入库", new OnBindView<BottomDialog>(R.layout.dialog_inventory_product_check_in) {
                @Override
                public void onBind(BottomDialog dialog, View v) {

                }
            }).setOkButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    return false;
                }
            }).setCancelButton("取消");
        });

        //TODO: 完成出库功能
        outButton.setOnClickListener(v->{
            BottomDialog.show("确认送达",new OnBindView<BottomDialog>(R.layout.dialog_inventory_product_check_out) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    //TODO: 添加“确认”事件
                    spinner = v.findViewById(R.id.orderSpinner);
                    tvName = v.findViewById(R.id.textViewName);
                    tvModel = v.findViewById(R.id.textViewModel);
                    tvCustomer = v.findViewById(R.id.textViewCustomer);
                    tvCount = v.findViewById(R.id.textViewCount);
                    tvPrice = v.findViewById(R.id.textViewPrice);
                    tvSaleDate = v.findViewById(R.id.textViewSaleDate);
                    calendar = v.findViewById(R.id.textViewCalendar);
                    etDeliveryDate = v.findViewById(R.id.editTextDeliveryDate);

                    ArrayAdapter<SaleOrder> orderAdapter = new ArrayAdapter<>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item, orders);

                    spinner.setAdapter(orderAdapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            SaleOrder order = orders.get(i);
                            tvName.setText(order.getProductName());
                            tvModel.setText(order.getProductModel());
                            tvCustomer.setText(order.getCustomer());
                            tvCount.setText(String.valueOf(order.getCount()));
                            tvPrice.setText(String.valueOf(order.getPrice()));
                            tvSaleDate.setText(order.getSaleDate());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) { }
                    });

//                    final int[] mYear = new int[1];
//                    final int[] mMonth = new int[1];
//                    final int[] mDay = new int[1];
//                    mYear[0] = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
//                    mMonth[0] = Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH);
//                    mDay[0] = Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_MONTH);
//                    calendar.setOnClickListener(view1->{
//                        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                mYear[0] = year;
//                                mMonth[0] = monthOfYear;
//                                mDay[0] = dayOfMonth;
//                                deliveryDate.setText(new StringBuilder().append(mYear[0]).append("-")
//                                        .append(mMonth[0] + 1).append("-").append(mDay[0]));
//                            }
//                        };
//                        new DatePickerDialog(getActivity(), mDateSetListener, mYear[0], mMonth[0], mDay[0]).show();
//                    });
                    calendar.setOnClickListener(v1->{
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                date = LocalDate.of(year,month+1,day);
                                int monthValue = date.getMonthValue();
                                String dateString = "";
                                if(monthValue<10)
                                    dateString = date.getYear() + "-0" + date.getMonthValue() + "-" + date.getDayOfMonth();
                                else
                                    dateString = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
                                etDeliveryDate.setText(dateString);
                            }
                        },date.getYear(),date.getMonthValue()-1,date.getDayOfMonth());
                        datePickerDialog.show();
                    });
                }
            }).setOkButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    String name = tvName.getText().toString();
                    String model = tvModel.getText().toString();
                    String customer = tvCustomer.getText().toString();
                    int count = Integer.parseInt(tvCount.getText().toString());
                    double price = Double.parseDouble(tvPrice.getText().toString());
                    String saleDate = tvSaleDate.getText().toString();
                    String deliveryDate = etDeliveryDate.getText().toString();

                    SaleOrder order = new SaleOrder(name,model,count,price,customer,saleDate,deliveryDate,SaleOrder.STATE_COMPLETE,"");
                    saleOrderViewModel.updateSaleOrder(order);

                    Inventory inventory = null;
                    for (Inventory temp:inventoryList) {
                        if(temp.getModel().equals(model)){
                            inventory = temp;
                            break;
                        }
                    }
                    inventory.setDeliveryCount(inventory.getDeliveryCount()-count);
                    viewModel.updateInventory(inventory);
                    return false;
                }
            }).setCancelButton("取消");
        });

        //呼出抽屉菜单
        menuButton.setOnClickListener(v->{
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });
    }
}