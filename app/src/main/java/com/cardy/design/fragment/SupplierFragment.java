package com.cardy.design.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.cardy.design.R;
import com.cardy.design.adapter.SupplierListAdapter;
import com.cardy.design.entity.Supplier;
import com.cardy.design.util.diff.SupplierDiffCallback;
import com.cardy.design.viewmodel.SupplierViewModel;
import com.cardy.design.widget.IconFontTextView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SupplierFragment extends Fragment {

    SupplierListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton, menuButton;
    SupplierViewModel viewModel;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;
    ImageView imageViewLogo;
    EditText editTextLogo;

    Boolean flag = true;

    public SupplierFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),new ActivityResultCallback<ActivityResult>(){
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Uri uri = result.getData().getData();
                    if(imageViewLogo!=null && editTextLogo!= null){
                        Picasso.with(getContext()).load(uri).into(imageViewLogo);
                        editTextLogo.setText(uri.toString());
                    }
                    adapter.setImage(uri);
                }
            }
        });
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
        viewModel = new ViewModelProvider(this).get(SupplierViewModel.class);
        adapter = new SupplierListAdapter(R.layout.item_supplier_information, viewModel,intentActivityResultLauncher);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.supplierRecycleview);
        searchView = getView().findViewById(R.id.supplierSearchView);
        addButton = getView().findViewById(R.id.supplierAddButton);
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty_layout);

        //设置diffCallback
        adapter.setDiffCallback(new SupplierDiffCallback());

        viewModel.getAllSupplierLive().observe(getActivity(), new Observer<List<Supplier>>() {
            @Override
            public void onChanged(List<Supplier> suppliers) {
                if(searchView.getQuery().equals("")|| flag){
                    if (adapter.getData().size() == 0)
                        adapter.setNewInstance(suppliers);
                    //通过setDiffNewData来通知adapter数据发生变化，并保留动画
                    adapter.setDiffNewData(suppliers);
                    //重写的setList方法更新adapter中的list数据
                    adapter.setMyList(suppliers);
                }
            }
        });

        initAddMethod();
        initSearch();

        menuButton.setOnClickListener(v -> {
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

//        Supplier supplier = new Supplier("汤記贸易有限责任公司","成华区玉双路6号859号",Supplier.PRIORITY_HIGH,"content://com.android.providers.downloads.documents/document/raw%3A%2Fstorage%2Femulated%2F0%2FDownload%2Fwallhaven-eymzjk.jpg","卷纸");
//        Supplier supplier1 = new Supplier("蓝天种植园","蓝天市蓝天区蓝天街道20号",Supplier.PRIORITY_HIGH,"","鸦片");
//        Supplier supplier2 = new Supplier("绿地种植园","绿地市绿地区绿地街道20号",Supplier.PRIORITY_HIGH,"content://com.android.providers.downloads.documents/document/raw%3A%2Fstorage%2Femulated%2F0%2FDownload%2Fwallhaven-wyzd56.png","鸦片");
//        viewModel.insertSupplier(supplier,supplier1,supplier2);
    }

    public void initAddMethod() {
        addButton.setOnClickListener(v -> {
            final TextView[] textViewNameLabel = new TextView[1];
            final TextView[] textViewAddressLabel = new TextView[1];
            final TextView[] textViewMainLabel = new TextView[1];
            final TextView[] textViewPriorityLabel = new TextView[1];
            final TextView[] editTextMain = new TextView[1];
            final EditText[] editTextName = new EditText[1];
            final EditText[] editTextAddress = new EditText[1];
            final RadioButton[] radioButtonLow = new RadioButton[1];
            final RadioButton[] radioButtonMid = new RadioButton[1];
            final RadioButton[] radioButtonHigh = new RadioButton[1];
            BottomDialog.show("添加供货商", new OnBindView<BottomDialog>(R.layout.dialog_add_customer_supplier) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    imageViewLogo = v.findViewById(R.id.imageViewLogo);
                    textViewNameLabel[0] = v.findViewById(R.id.textViewNameLabel);
                    textViewAddressLabel[0] = v.findViewById(R.id.textViewModelLabel);
                    textViewMainLabel[0] = v.findViewById(R.id.textViewPriceLabel);
                    textViewPriorityLabel[0] = v.findViewById(R.id.textViewPriorityLabel);
                    editTextLogo = v.findViewById(R.id.editTextLogo);
                    editTextName[0] = v.findViewById(R.id.editTextName);
                    editTextAddress[0] = v.findViewById(R.id.editTextModel);
                    editTextMain[0] = v.findViewById(R.id.editTextMain);
                    radioButtonLow[0] = v.findViewById(R.id.radioButtonLow);
                    radioButtonMid[0] = v.findViewById(R.id.radioButtonMid);
                    radioButtonHigh[0] = v.findViewById(R.id.radioButtonHigh);

                    //修改界面为供货商
                    textViewNameLabel[0].setText("供货商名称：");
                    textViewAddressLabel[0].setText("供货商地址：");
                    textViewMainLabel[0].setText("主要供货原料：");
                    textViewPriorityLabel[0].setText("供货商优先级：");

                    //添加点击事件
                    imageViewLogo.setOnClickListener(imageView->{
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        intentActivityResultLauncher.launch(intent);
                    });
                }
            }).setOkButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    String name = editTextName[0].getText().toString();
                    String logo = editTextLogo.getText().toString();
                    String address = editTextAddress[0].getText().toString();
                    String main = editTextMain[0].getText().toString();
                    int priority = 0;
                    if (radioButtonLow[0].isChecked())
                        priority = Supplier.PRIORITY_LOW;
                    else if (radioButtonMid[0].isChecked())
                        priority = Supplier.PRIORITY_MIDDLE;
                    else if (radioButtonHigh[0].isChecked())
                        priority = Supplier.PRIORITY_HIGH;

                    Supplier supplier = new Supplier(name, address, priority, logo, main);
                    viewModel.insertSupplier(supplier);
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
                viewModel.getAllQueriedSupplierLive(newText).observe(getActivity(), new Observer<List<Supplier>>() {
                    @Override
                    public void onChanged(List<Supplier> suppliers) {
                        flag = false;
                        adapter.setDiffNewData(suppliers);
                        adapter.setMyList(suppliers);
                    }
                });
                return false;
            }
        });
    }


}