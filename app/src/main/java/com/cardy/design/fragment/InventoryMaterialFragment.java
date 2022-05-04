package com.cardy.design.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.cardy.design.R;
import com.cardy.design.adapter.InventoryMaterialListAdapter;
import com.cardy.design.entity.Inventory;
import com.cardy.design.util.diff.InventoryDiffCallback;
import com.cardy.design.viewmodel.InventoryViewModel;
import com.cardy.design.widget.IconFontTextView;

import java.util.List;

public class InventoryMaterialFragment extends Fragment {

    InventoryMaterialListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton,menuButton;
    InventoryViewModel viewModel;

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
        adapter = new InventoryMaterialListAdapter(R.layout.item_inventory_material,viewModel);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.inventoryMaterialRecycleview);
        searchView = getView().findViewById(R.id.inventoryMaterialSearchView);
        addButton = getView().findViewById(R.id.inventoryMaterialAddButton);
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

        //呼出抽屉菜单
        menuButton.setOnClickListener(v->{
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });
    }
}