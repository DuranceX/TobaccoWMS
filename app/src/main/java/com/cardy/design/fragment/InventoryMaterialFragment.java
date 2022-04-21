package com.cardy.design.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cardy.design.R;
import com.cardy.design.adapter.InventoryListAdapter;
import com.cardy.design.entity.InventoryTest;
import com.cardy.design.widget.IconFontTextView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;

import java.util.ArrayList;
import java.util.List;

public class InventoryMaterialFragment extends Fragment {

    InventoryListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton,menuButton;

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
        adapter = new InventoryListAdapter(R.layout.item_inventory_material);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.inventoryMaterialRecycleview);
        searchView = getView().findViewById(R.id.inventoryMaterialSearchView);
        addButton = getView().findViewById(R.id.inventoryMaterialAddButton);
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        //TODO: 从数据库中获取数据
        List<InventoryTest> list = new ArrayList<InventoryTest>(5);
        for (int i = 0; i < 5; i++) {
            InventoryTest inventoryTest = new InventoryTest("鸦片","SICS202312","",300,200,"B区22号",InventoryTest.TYPE_MATERIAL);
            list.add(inventoryTest);
        }

        adapter.setList(list);

        addButton.setOnClickListener(v->{
            BottomDialog.show("确认送达",new OnBindView<BottomDialog>(R.layout.dialog_add_customer_supplier) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    //TODO: 添加“添加”事件

                }
            }).setOkButton("确定").setCancelButton("取消");
        });

        //呼出抽屉菜单
        menuButton.setOnClickListener(v->{
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });
    }
}