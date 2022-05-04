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
import com.cardy.design.adapter.InventoryMaterialListAdapter;
import com.cardy.design.entity.Inventory;
import com.cardy.design.entity.Material;
import com.cardy.design.entity.PurchaseOrder;
import com.cardy.design.entity.SaleOrder;
import com.cardy.design.util.diff.InventoryDiffCallback;
import com.cardy.design.viewmodel.InventoryViewModel;
import com.cardy.design.viewmodel.MaterialViewModel;
import com.cardy.design.viewmodel.PurchaseOrderViewModel;
import com.cardy.design.widget.IconFontTextView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

import java.time.LocalDate;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class InventoryMaterialFragment extends Fragment {

    InventoryMaterialListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView menuButton;
    Button inButton,outButton;
    InventoryViewModel viewModel;
    PurchaseOrderViewModel purchaseOrderViewModel;
    MaterialViewModel materialViewModel;

    List<PurchaseOrder> orders;
    List<Material> materials;
    LocalDate date = LocalDate.now();

    Spinner spinner;
    TextView tvName, tvModel, tvSupplier, tvCount, tvPrice, tvPurchaseDate,calendar;
    EditText etDeliveryDate,etOutCount,etArea;

    public InventoryMaterialFragment() {
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
        return inflater.inflate(R.layout.fragment_inventory_material, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        purchaseOrderViewModel = new ViewModelProvider(this).get(PurchaseOrderViewModel.class);
        materialViewModel = new ViewModelProvider(this).get(MaterialViewModel.class);
        adapter = new InventoryMaterialListAdapter(R.layout.item_inventory_material,viewModel);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.inventoryMaterialRecycleview);
        searchView = getView().findViewById(R.id.inventoryMaterialSearchView);
        inButton = getView().findViewById(R.id.inventoryMaterialInButton);
        outButton = getView().findViewById(R.id.inventoryMaterialOutButton);
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty_layout);

        adapter.setDiffCallback(new InventoryDiffCallback());

        viewModel.getAllMaterialInventory().observe(getActivity(), new Observer<List<Inventory>>() {
            @Override
            public void onChanged(List<Inventory> inventories) {
                if (adapter.getData().size() == 0)
                    adapter.setNewInstance(inventories);
                //通过setDiffNewData来通知adapter数据发生变化，并保留动画
                adapter.setDiffNewData(inventories);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                orders = purchaseOrderViewModel.getSelectedStateOrder(SaleOrder.STATE_DELIVERY);
                materials = materialViewModel.getAllMaterialsNoLive();
            }
        }).start();

        outButton.setOnClickListener(v->{
            final Inventory[] inventory = new Inventory[1];
            BottomDialog.show("入库", new OnBindView<BottomDialog>(R.layout.dialog_inventory_material_check_out) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    spinner = v.findViewById(R.id.orderSpinner);
                    etOutCount = v.findViewById(R.id.editTextOutCount);

                    ArrayAdapter<Material> materialAdapter = new ArrayAdapter<>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item, materials);
                    spinner.setAdapter(materialAdapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Material material = materials.get(spinner.getSelectedItemPosition());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    inventory[0] = viewModel.getInventoryByModel(material.getModel());
                                }
                            }).start();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }).setOkButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    int outCount = Integer.parseInt(etOutCount.getText().toString());

                    inventory[0].setHostCount(inventory[0].getHostCount()-outCount);
                    viewModel.updateInventory(inventory[0]);
                    return false;
                }
            }).setCancelButton("取消");
        });

        inButton.setOnClickListener(v->{
            final Inventory[] inventory = new Inventory[1];
            BottomDialog.show("出库",new OnBindView<BottomDialog>(R.layout.dialog_inventory_material_check_in) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    spinner = v.findViewById(R.id.orderSpinner);
                    tvName = v.findViewById(R.id.textViewName);
                    tvModel = v.findViewById(R.id.textViewModel);
                    tvSupplier = v.findViewById(R.id.textViewSupplier);
                    tvCount = v.findViewById(R.id.textViewCount);
                    tvPrice = v.findViewById(R.id.textViewPrice);
                    tvPurchaseDate = v.findViewById(R.id.textViewPurchaseDate);
                    calendar = v.findViewById(R.id.textViewCalendar);
                    etDeliveryDate = v.findViewById(R.id.editTextDeliveryDate);
                    etArea = v.findViewById(R.id.editTextArea);

                    ArrayAdapter<PurchaseOrder> orderAdapter = new ArrayAdapter<>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item, orders);

                    spinner.setAdapter(orderAdapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            PurchaseOrder order = orders.get(i);
                            tvName.setText(order.getMaterialName());
                            tvModel.setText(order.getMaterialModel());
                            tvSupplier.setText(order.getSupplier());
                            tvCount.setText(String.valueOf(order.getCount()));
                            tvPrice.setText(String.valueOf(order.getPrice()));
                            tvPurchaseDate.setText(order.getPurchaseDate());

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    inventory[0] = viewModel.getInventoryByModel(order.getMaterialModel());
                                }
                            }).start();
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
                    String supplier = tvSupplier.getText().toString();
                    int count = Integer.parseInt(tvCount.getText().toString());
                    double price = Double.parseDouble(tvPrice.getText().toString());
                    String purchaseDate = tvPurchaseDate.getText().toString();
                    String deliveryDate = etDeliveryDate.getText().toString();
                    String area = etArea.getText().toString();

                    PurchaseOrder order = new PurchaseOrder(name,model,count,price,supplier,purchaseDate,deliveryDate,SaleOrder.STATE_COMPLETE,"");
                    purchaseOrderViewModel.updatePurchaseOrder(order);

                    if(inventory[0]==null){
                        Inventory temp = new Inventory(name,model,"",count,0,area,Inventory.TYPE_MATERIAL);
                        viewModel.insertInventory(temp);
                    }else{
                        inventory[0].setHostCount(inventory[0].getHostCount()+count);
                        inventory[0].setDeliveryCount(inventory[0].getDeliveryCount()-count);
                        viewModel.updateInventory(inventory[0]);
                    }
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