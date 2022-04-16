package com.cardy.design.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.widget.SearchView;

import com.cardy.design.R;
import com.cardy.design.adapter.CustomerListAdapter;
import com.cardy.design.entity.CustomerTest;
import com.cardy.design.widget.IconFontTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;

import java.util.Arrays;
import java.util.List;

public class CustomerFragment extends Fragment {

    CustomerListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton,menuButton;


    public CustomerFragment() {
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
        return inflater.inflate(R.layout.fragment_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new CustomerListAdapter(R.layout.item_customer_information);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.customerRecycleview);
        searchView = getView().findViewById(R.id.customerSearchView);
        addButton = getView().findViewById(R.id.customerAddButton);
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        CustomerTest[] list = new CustomerTest[5];
        for (int i = 0; i < 5; i++) {
            list[i] = new CustomerTest();
            list[i].setName("陆玩具有限责任公司");
            list[i].setAddress("天河区大信商圈大信南路32号");
            list[i].setMainPurchase(new String[]{"烟草","香烟"});
        }

        list[1].setName("测试有限责任公司");
        list[1].setAddress("测试区测试路32号");
        list[1].setMainPurchase(new String[]{"烟草","香烟","测试产品"});

        List<CustomerTest> newList = Arrays.asList(list);
        adapter.setNewInstance(newList);
        adapter.setList(newList);

        addButton.setOnClickListener(v->{
            BottomDialog.show("添加客户",new OnBindView<BottomDialog>(R.layout.dialog_add_customer) {
                @Override
                public void onBind(BottomDialog dialog, View v) {

                }
            }).setOkButton("确定").setCancelButton("取消");
        });

        menuButton.setOnClickListener(v->{
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });


    }
}