package com.cardy.design.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cardy.design.R;
import com.cardy.design.adapter.CustomerListAdapter;
import com.cardy.design.adapter.SupplierListAdapter;
import com.cardy.design.entity.CustomerTest;
import com.cardy.design.widget.IconFontTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;

import java.util.Arrays;
import java.util.List;

public class SupplierFragment extends Fragment {
    SupplierListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton;

    public SupplierFragment() {
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
        return inflater.inflate(R.layout.fragment_supplier, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SupplierListAdapter(R.layout.item_supplier_information);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.supplierRecycleview);
        searchView = getView().findViewById(R.id.supplierSearchView);
        addButton = getView().findViewById(R.id.supplierAddButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        CustomerTest[] list = new CustomerTest[5];
        for (int i = 0; i < 5; i++) {
            list[i] = new CustomerTest();
            list[i].setName("汤記贸易有限责任公司");
            list[i].setAddress("成华区玉双路6号859号");
            list[i].setMainPurchase(new String[]{"鸦片"});
        }

        List<CustomerTest> newList = Arrays.asList(list);
        adapter.setNewInstance(newList);

        addButton.setOnClickListener(v->{
            BottomDialog.show("新增客户", "这里是对话框内容", new OnBindView<BottomDialog>(R.layout.dialog_add_customer) {
                @Override
                public void onBind(BottomDialog dialog, View v) {

                }
            });
        });
    }
}