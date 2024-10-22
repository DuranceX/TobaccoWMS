package com.cardy.design.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.cardy.design.R;
import com.cardy.design.adapter.ProductListAdapter;
import com.cardy.design.entity.Product;
import com.cardy.design.util.diff.ProductDiffCallback;
import com.cardy.design.viewmodel.ProductViewModel;
import com.cardy.design.widget.IconFontTextView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductFragment extends Fragment {

    ProductListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton,menuButton;
    ProductViewModel viewModel;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;

    EditText editTextName,editTextModel,editTextUsedMaterial,editTextPrice,editTextImagePath;
    ImageView imageProduct;

    Boolean flag = true;

    public ProductFragment() {
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
                    if(imageProduct!=null && editTextImagePath!= null){
                        Picasso.with(getContext()).load(uri).into(imageProduct);
                        editTextImagePath.setText(uri.toString());
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
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        adapter = new ProductListAdapter(R.layout.item_product_information,viewModel,intentActivityResultLauncher);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.productRecycleview);
        searchView = getView().findViewById(R.id.productSearchView);
        addButton = getView().findViewById(R.id.productAddButton);
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty_layout);

        //设置diffCallback
        adapter.setDiffCallback(new ProductDiffCallback());

        viewModel.getAllProductLive().observe(getActivity(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                if(searchView.getQuery().equals("") || flag){
                    if (adapter.getData().size() == 0)
                        adapter.setNewInstance(products);
                    //通过setDiffNewData来通知adapter数据发生变化，并保留动画
                    adapter.setDiffNewData(products);
                    adapter.setMyList(products);
                }
            }
        });

        initClickListener();
        initSearch();

        menuButton.setOnClickListener(v->{
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

//        Product product = new Product("中华香烟","SC00120211715","https://www.opposingviews.com/.image/t_share/MTUzODg1MTA0MDE4Njk1OTM0/image-placeholder-title.jpg",100.0,"鸦片,卷纸");
//        Product product1 = new Product("白沙香烟","SC00220211523","http://img1.cnxiangyan.com/2021/1124/619e011add22e.png",10.0,"鸦片,卷纸");
//        Product product2 = new Product("云烟","SC00320213524","http://img1.cnxiangyan.com/2021/1208/61b06137495d8.jpg",22.0,"鸦片,卷纸");
//        viewModel.insertProduct(product,product1,product2);
    }

    public void initClickListener(){
        addButton.setOnClickListener(v->{
            BottomDialog.show("添加产品",new OnBindView<BottomDialog>(R.layout.dialog_add_product) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    imageProduct = v.findViewById(R.id.imageViewProduct);
                    editTextImagePath = v.findViewById(R.id.editTextImagePath);
                    editTextName = v.findViewById(R.id.editTextName);
                    editTextModel = v.findViewById(R.id.editTextModel);
                    editTextUsedMaterial = v.findViewById(R.id.editTextUsedMaterial);
                    editTextPrice = v.findViewById(R.id.editTextPrice);
                    editTextModel.setFocusable(true);

                    //添加点击事件
                    imageProduct.setOnClickListener(imageView->{
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        intentActivityResultLauncher.launch(intent);
                    });
                }
            }).setOkButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    String name = editTextName.getText().toString();
                    String image = editTextImagePath.getText().toString();
                    String model = editTextModel.getText().toString();
                    String usedMaterial = editTextUsedMaterial.getText().toString();
                    Double price = Double.parseDouble(editTextPrice.getText().toString());

                    Product product = new Product(name,model,image,price,usedMaterial);
                    viewModel.insertProduct(product);
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
                viewModel.getAllQueriedProductLive(newText).observe(getActivity(), new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        flag = false;
                        adapter.setDiffNewData(products);
                        adapter.setMyList(products);
                    }
                });
                return false;
            }
        });
    }
}