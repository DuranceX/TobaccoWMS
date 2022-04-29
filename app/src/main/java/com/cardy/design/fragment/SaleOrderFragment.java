package com.cardy.design.fragment;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import com.cardy.design.entity.PurchaseOrder;
import com.cardy.design.entity.SaleOrder;
import com.cardy.design.util.diff.SaleOrderDiffCallback;
import com.cardy.design.viewmodel.CustomerViewModel;
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
    SaleOrderListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton, menuButton;
    SaleOrderViewModel viewModel;
    ProductViewModel productViewModel;
    CustomerViewModel customerViewModel;
    List<String> customerList = new ArrayList<>();
    List<String> productNameList = new ArrayList<>();
    List<String> productModelList = new ArrayList<>();

    TextView tvUserId, tvUserName, tvSaleDate;
    IconFontTextView tvCalendarButton;
    Spinner spinnerName, spinnerModel, spinnerCustomer;
    EditText etPrice, etCount, etDeliveryDate;

    LocalDate date = LocalDate.now();

    public SaleOrderFragment() {
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
        return inflater.inflate(R.layout.fragment_sale_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SaleOrderViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        adapter = new SaleOrderListAdapter(R.layout.item_sale_order, viewModel, productViewModel, customerViewModel);
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
                if (adapter.getData().size() == 0)
                    adapter.setNewInstance(saleOrders);
                adapter.setDiffNewData(saleOrders);
                adapter.setMyList(saleOrders);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                productNameList = productViewModel.getProductNameList();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                customerList = customerViewModel.getNameList();
            }
        }).start();

        initAddMethod();

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
                    tvCalendarButton = v.findViewById(R.id.calendarButton);
                    spinnerName = v.findViewById(R.id.spinnerName);
                    spinnerModel = v.findViewById(R.id.spinnerModel);
                    spinnerCustomer = v.findViewById(R.id.spinnerCustomer);
                    etPrice = v.findViewById(R.id.editTextPrice);
                    etCount = v.findViewById(R.id.editTextCount);
                    etDeliveryDate = v.findViewById(R.id.editTextDeliveryDate);

                    ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item, productNameList);
                    ArrayAdapter<String> supplierAdapter = new ArrayAdapter<String>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item, customerList);

                    spinnerName.setAdapter(nameAdapter);
                    spinnerCustomer.setAdapter(supplierAdapter);

                    spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            productModelList.clear();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String name = spinnerName.getSelectedItem().toString();
                                    productModelList = productViewModel.getProductModelListByName(name);
                                    ArrayAdapter<String> modelAdapter = new ArrayAdapter<String>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item, productModelList);
                                    spinnerModel.setAdapter(modelAdapter);
                                }
                            }).start();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    tvSaleDate.setText(LocalDate.now().toString());
                    tvCalendarButton.setOnClickListener(v1 -> {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                date = LocalDate.of(year, month + 1, day);
                                int monthValue = date.getMonthValue();
                                String dateString = "";
                                if (monthValue < 10)
                                    dateString = date.getYear() + "-0" + date.getMonthValue() + "-" + date.getDayOfMonth();
                                else
                                    dateString = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
                                etDeliveryDate.setText(dateString);
                            }
                        }, date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
                        datePickerDialog.show();
                    });
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
                    String deliveryDate = etDeliveryDate.getText().toString();
                    SaleOrder order = new SaleOrder(0, userId, userName, name, model, count, price, customer, saleDate, deliveryDate, PurchaseOrder.STATE_REQUEST, "");
                    viewModel.insertSaleOrder(order);
                    return false;
                }
            }).setCancelButton("取消");
        });
    }
}