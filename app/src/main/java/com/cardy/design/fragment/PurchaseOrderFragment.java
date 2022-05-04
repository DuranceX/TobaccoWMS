package com.cardy.design.fragment;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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
import com.cardy.design.adapter.PurchaseOrderListAdapter;
import com.cardy.design.entity.PurchaseOrder;
import com.cardy.design.util.diff.PurchaseOrderDiffCallback;
import com.cardy.design.viewmodel.InventoryViewModel;
import com.cardy.design.viewmodel.MaterialViewModel;
import com.cardy.design.viewmodel.PurchaseOrderViewModel;
import com.cardy.design.viewmodel.SupplierViewModel;
import com.cardy.design.widget.IconFontTextView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PurchaseOrderFragment extends Fragment {
    PurchaseOrderListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton,menuButton;
    PurchaseOrderViewModel viewModel;
    MaterialViewModel materialViewModel;
    SupplierViewModel supplierViewModel;
    InventoryViewModel inventoryViewModel;
    List<String> supplierList = new ArrayList<>();
    List<String> materialNameList = new ArrayList<>();
    List<String> materialModelList = new ArrayList<>();

    TextView tvUserId, tvUserName, tvPurchaseDate;
    Spinner spinnerName, spinnerModel, spinnerSupplier;
    EditText etPrice, etCount;

    LocalDate date = LocalDate.now();
    int SET_SECOND_SPINNER = 1;

    public PurchaseOrderFragment() {
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
        return inflater.inflate(R.layout.fragment_purchase_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PurchaseOrderViewModel.class);
        materialViewModel = new ViewModelProvider(this).get(MaterialViewModel.class);
        supplierViewModel = new ViewModelProvider(this).get(SupplierViewModel.class);
        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        adapter = new PurchaseOrderListAdapter(R.layout.item_purchase_order,viewModel,materialViewModel,supplierViewModel,inventoryViewModel);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.purchaseOrderRecycleview);
        searchView = getView().findViewById(R.id.purchaseOrderSearchView);
        addButton = getView().findViewById(R.id.purchaseOrderAddButton);
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty_layout);

        adapter.setDiffCallback(new PurchaseOrderDiffCallback());

        viewModel.getAllPurchaseOrderLive().observe(getActivity(), new Observer<List<PurchaseOrder>>() {
            @Override
            public void onChanged(List<PurchaseOrder> purchaseOrders) {
                if (adapter.getData().size() == 0)
                    adapter.setNewInstance(purchaseOrders);
                adapter.setDiffNewData(purchaseOrders);
                adapter.setMyList(purchaseOrders);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                materialNameList = materialViewModel.getMaterialNameList();
                supplierList = supplierViewModel.getNameList();
            }
        }).start();


        initAddMethod();

        menuButton.setOnClickListener(v->{
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });
    }

    public void initAddMethod(){
        addButton.setOnClickListener(v->{
            BottomDialog.show("添加原料",new OnBindView<BottomDialog>(R.layout.dialog_add_purchase_order) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    tvUserId = v.findViewById(R.id.textViewUserId);
                    tvUserName = v.findViewById(R.id.textViewUserName);
                    tvPurchaseDate = v.findViewById(R.id.textViewPurchaseDate);
                    spinnerName = v.findViewById(R.id.spinnerName);
                    spinnerModel = v.findViewById(R.id.spinnerModel);
                    spinnerSupplier = v.findViewById(R.id.spinnerSupplier);
                    etPrice = v.findViewById(R.id.editTextPrice);
                    etCount = v.findViewById(R.id.editTextCount);

                    ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item,materialNameList);
                    ArrayAdapter<String> supplierAdapter = new ArrayAdapter<String>(getContext(), com.lihang.R.layout.support_simple_spinner_dropdown_item,supplierList);

                    spinnerName.setAdapter(nameAdapter);
                    spinnerSupplier.setAdapter(supplierAdapter);

                    Handler mHandler = new Handler(Looper.myLooper()){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            if(msg.what == SET_SECOND_SPINNER){
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
                                    message.what = SET_SECOND_SPINNER;;
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

                    tvPurchaseDate.setText(LocalDate.now().toString());
                }
            }).setOkButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
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
                    PurchaseOrder order = new PurchaseOrder(0,userId,userName,name,model,count,price,supplier,purchaseDate,"",PurchaseOrder.STATE_REQUEST,"");
                    viewModel.insertPurchaseOrder(order);
                    return false;
                }
            }).setCancelButton("取消");
        });
    }
}