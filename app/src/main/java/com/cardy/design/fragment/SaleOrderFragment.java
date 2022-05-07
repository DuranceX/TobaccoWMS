package com.cardy.design.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

import com.cardy.design.R;
import com.cardy.design.adapter.SaleOrderListAdapter;
import com.cardy.design.entity.SaleOrder;
import com.cardy.design.util.diff.SaleOrderDiffCallback;
import com.cardy.design.viewmodel.CustomerViewModel;
import com.cardy.design.viewmodel.InventoryViewModel;
import com.cardy.design.viewmodel.ProductViewModel;
import com.cardy.design.viewmodel.SaleOrderViewModel;
import com.cardy.design.widget.IconFontTextView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SaleOrderFragment extends Fragment {
    Boolean permission;
    SaleOrderListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton, menuButton;
    SaleOrderViewModel viewModel;
    ProductViewModel productViewModel;
    CustomerViewModel customerViewModel;
    InventoryViewModel inventoryViewModel;
    List<String> customerList = new ArrayList<>();
    List<String> productNameList = new ArrayList<>();
    List<String> productModelList = new ArrayList<>();

    TextView tvUserId, tvUserName, tvSaleDate;
    Spinner spinnerName, spinnerModel, spinnerCustomer;
    EditText etPrice, etCount;

    Boolean firstFlag = true;

    public SaleOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences shp = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
        permission = shp.getBoolean("permission",false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sale_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SaleOrderViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        adapter = new SaleOrderListAdapter(R.layout.item_sale_order, viewModel, productViewModel, customerViewModel,inventoryViewModel);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.saleOrderRecycleview);
        searchView = getView().findViewById(R.id.saleOrderSearchView);
        addButton = getView().findViewById(R.id.saleOrderAddButton);
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty_layout);

        adapter.setDiffCallback(new SaleOrderDiffCallback());

        viewModel.getAllSaleOrderLive().observe(getActivity(), new Observer<List<SaleOrder>>() {
            @Override
            public void onChanged(List<SaleOrder> saleOrders) {
                if(searchView.getQuery().equals("") || firstFlag){
                    if (adapter.getData().size() == 0)
                        adapter.setNewInstance(saleOrders);
                    adapter.setDiffNewData(saleOrders);
                    adapter.setMyList(saleOrders);
                    firstFlag = false;
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                productNameList = productViewModel.getProductNameList();
                customerList = customerViewModel.getNameList();
            }
        }).start();

        initAddMethod();
        initSearch();

        menuButton.setOnClickListener(v -> {
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });
    }

    public void initAddMethod() {
        addButton.setOnClickListener(v -> {
            BottomDialog.show("添加原料", new OnBindView<BottomDialog>(R.layout.dialog_add_sale_order) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    tvUserId = v.findViewById(R.id.textViewUserId);
                    tvUserName = v.findViewById(R.id.textViewUserName);
                    tvSaleDate = v.findViewById(R.id.textViewSaleDate);
                    spinnerName = v.findViewById(R.id.spinnerName);
                    spinnerModel = v.findViewById(R.id.spinnerModel);
                    spinnerCustomer = v.findViewById(R.id.spinnerCustomer);
                    etPrice = v.findViewById(R.id.editTextPrice);
                    etCount = v.findViewById(R.id.editTextCount);

                    ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item, productNameList);
                    ArrayAdapter<String> supplierAdapter = new ArrayAdapter<String>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item, customerList);

                    spinnerName.setAdapter(nameAdapter);
                    spinnerCustomer.setAdapter(supplierAdapter);

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

                    tvSaleDate.setText(LocalDate.now().toString());
                }
            }).setOkButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
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
                    SaleOrder order = new SaleOrder(0, userId, userName, name, model, count, price, customer, saleDate, "", SaleOrder.STATE_WAIT, "");
                    if(permission){
                        order.setState(SaleOrder.STATE_WAIT);
                    }
                    viewModel.insertSaleOrder(order);
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
                viewModel.getAllQueriedSaleOrderLive(newText).observe(getActivity(), new Observer<List<SaleOrder>>() {
                    @Override
                    public void onChanged(List<SaleOrder> saleOrders) {
                        adapter.setDiffNewData(saleOrders);
                        adapter.setMyList(saleOrders);
                    }
                });
                return false;
            }
        });
    }
}