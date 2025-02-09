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

import android.util.Log;
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
import com.cardy.design.adapter.InventoryProductListAdapter;
import com.cardy.design.entity.Inventory;
import com.cardy.design.entity.Product;
import com.cardy.design.entity.SaleOrder;
import com.cardy.design.util.diff.InventoryDiffCallback;
import com.cardy.design.viewmodel.InventoryViewModel;
import com.cardy.design.viewmodel.ProductViewModel;
import com.cardy.design.viewmodel.SaleOrderViewModel;
import com.cardy.design.widget.IconFontTextView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

import java.time.LocalDate;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class InventoryProductFragment extends Fragment {

    InventoryProductListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView menuButton;
    Button inButton,outButton;
    InventoryViewModel viewModel;
    SaleOrderViewModel saleOrderViewModel;
    ProductViewModel productViewModel;

    List<SaleOrder> orders;
    List<Product> products;
    LocalDate date = LocalDate.now();
    Boolean flag = true;

    Spinner spinner;
    TextView tvName, tvModel, tvCustomer, tvCount, tvPrice, tvSaleDate,calendar;
    EditText etDeliveryDate,etInCount,etArea;

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
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        adapter = new InventoryProductListAdapter(R.layout.item_inventory_product,viewModel,productViewModel,saleOrderViewModel);
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
                if(searchView.getQuery().equals("") || flag){
                    if (adapter.getData().size() == 0)
                        adapter.setNewInstance(inventories);
                    //通过setDiffNewData来通知adapter数据发生变化，并保留动画
                    adapter.setDiffNewData(inventories);
                    adapter.setMyList(inventories);
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                orders = saleOrderViewModel.getSelectedStateOrder(SaleOrder.STATE_WAIT);
                products = productViewModel.getAllProductNoLive();
            }
        }).start();

        initInButton();
        initOutButton();
        initSearch();

        //呼出抽屉菜单
        menuButton.setOnClickListener(v->{
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

    }

    public void initInButton(){
        inButton.setOnClickListener(v->{
            final Inventory[] inventory = new Inventory[1];
            final Product[] product = new Product[1];
            BottomDialog.show("入库", new OnBindView<BottomDialog>(R.layout.dialog_inventory_product_check_in) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    spinner = v.findViewById(R.id.orderSpinner);
                    etInCount = v.findViewById(R.id.editTextOutCount);
                    etArea = v.findViewById(R.id.editTextArea);

                    ArrayAdapter<Product> productAdapter = new ArrayAdapter<Product>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item, products);
                    spinner.setAdapter(productAdapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            product[0] = products.get(spinner.getSelectedItemPosition());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    inventory[0] = viewModel.getInventoryByModel(product[0].getModel());
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
                    String name = product[0].getName();
                    String model = product[0].getModel();
                    int inCount = Integer.parseInt(etInCount.getText().toString());
                    String area = etArea.getText().toString();
                    String areaNumber = tvCount.getText().toString();
                    if(inventory[0]==null){
                        Inventory temp = new Inventory(name,model,"",inCount,0,area,areaNumber,Inventory.TYPE_PRODUCT);
                        viewModel.insertInventory(temp);
                    }else{
                        int hostCount = inventory[0].getHostCount() + inCount;
                        Inventory temp = new Inventory(name,model,"",hostCount,inventory[0].getDeliveryCount(),inventory[0].getArea()+","+area,inventory[0].getAreaNumber()+","+areaNumber,Inventory.TYPE_PRODUCT);
                        viewModel.updateInventory(temp);
                    }
                    return false;
                }
            }).setCancelButton("取消");
        });
    }

    public void initOutButton(){
        outButton.setOnClickListener(v->{
            final Inventory[] inventory = new Inventory[1];
            final SaleOrder[] order = new SaleOrder[1];
            BottomDialog.show("出库",new OnBindView<BottomDialog>(R.layout.dialog_inventory_product_check_out) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
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
                            order[0] = orders.get(i);
                            tvName.setText(order[0].getProductName());
                            tvModel.setText(order[0].getProductModel());
                            tvCustomer.setText(order[0].getCustomer());
                            tvCount.setText(String.valueOf(order[0].getCount()));
                            tvPrice.setText(String.valueOf(order[0].getPrice()));
                            tvSaleDate.setText(order[0].getSaleDate());

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    inventory[0] = viewModel.getInventoryByModel(order[0].getProductModel());
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
                    int count = Integer.parseInt(tvCount.getText().toString());
                    if(inventory[0].getHostCount()>count){
                        String deliveryDate = etDeliveryDate.getText().toString();

                        order[0].setState(SaleOrder.STATE_DELIVERY);
                        order[0].setDeliveryDate(deliveryDate);
                        saleOrderViewModel.updateSaleOrder(order);

                        inventory[0].setHostCount(inventory[0].getHostCount()-count);
                        inventory[0].setDeliveryCount(inventory[0].getDeliveryCount()+count);
                        viewModel.updateInventory(inventory[0]);
                    }
                    else{
                        TipDialog.show("库存不足", WaitDialog.TYPE.ERROR);
                    }
                    return false;
                }
            }).setCancelButton("取消");
        });
    }

    public void initSearch(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.getAllQueriedProductInventory(newText).observe(getActivity(), new Observer<List<Inventory>>() {
                    @Override
                    public void onChanged(List<Inventory> inventories) {
                        flag = false;
                        adapter.setDiffNewData(inventories);
                        adapter.setMyList(inventories);
                    }
                });
                return false;
            }
        });
    }
}