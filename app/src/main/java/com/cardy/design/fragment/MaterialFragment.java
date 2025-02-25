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
import android.widget.EditText;

import com.cardy.design.R;
import com.cardy.design.adapter.MaterialListAdapter;
import com.cardy.design.entity.Material;
import com.cardy.design.util.diff.MaterialDiffCallback;
import com.cardy.design.viewmodel.MaterialViewModel;
import com.cardy.design.widget.IconFontTextView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

import java.util.List;

public class MaterialFragment extends Fragment {
    MaterialListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton,menuButton;
    MaterialViewModel viewModel;

    EditText editTextName,editTextModel,editTextPrice;

    Boolean flag = true;

    public MaterialFragment() {
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
        return inflater.inflate(R.layout.fragment_material, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MaterialViewModel.class);
        adapter = new MaterialListAdapter(R.layout.item_material_information,viewModel);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.materialRecycleview);
        searchView = getView().findViewById(R.id.materialSearchView);
        addButton = getView().findViewById(R.id.materialAddButton);
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty_layout);

        //设置diffCallback
        adapter.setDiffCallback(new MaterialDiffCallback());

        viewModel.getAllMaterialsLive().observe(getActivity(), new Observer<List<Material>>() {
            @Override
            public void onChanged(List<Material> materials) {
                if(searchView.getQuery().equals("") || flag){
                    if (adapter.getData().size() == 0)
                        adapter.setNewInstance(materials);
                    //通过setDiffNewData来通知adapter数据发生变化，并保留动画
                    adapter.setDiffNewData(materials);
                    adapter.setMyList(materials);
                }
            }
        });

        initClickListener();
        initSearch();

        menuButton.setOnClickListener(v->{
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

//        Material material = new Material("鸦片","SISC1023546",12.5);
//        Material material1 = new Material("鸦片","SISC1035864",14.5);
//        Material material2 = new Material("卷纸","SISC2034982",2.5);
//        viewModel.insertMaterial(material,material1,material2);
    }

    public void initClickListener(){
        addButton.setOnClickListener(v->{
            BottomDialog.show("添加原料",new OnBindView<BottomDialog>(R.layout.dialog_add_material) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    editTextName = v.findViewById(R.id.editTextName);
                    editTextModel = v.findViewById(R.id.editTextModel);
                    editTextPrice = v.findViewById(R.id.editTextPrice);
                    editTextModel.setFocusable(true);
                }
            }).setOkButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    String name = editTextName.getText().toString();
                    String model = editTextModel.getText().toString();
                    Double price = Double.parseDouble(editTextPrice.getText().toString());

                    Material material = new Material(name,model,price);
                    viewModel.insertMaterial(material);
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
                viewModel.getAllQueriedMaterialsLive(newText).observe(getActivity(), new Observer<List<Material>>() {
                    @Override
                    public void onChanged(List<Material> materials) {
                        flag = false;
                        adapter.setDiffNewData(materials);
                        adapter.setMyList(materials);
                    }
                });
                return false;
            }
        });
    }
}