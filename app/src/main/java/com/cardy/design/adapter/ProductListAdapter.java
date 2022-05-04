package com.cardy.design.adapter;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cardy.design.R;
import com.cardy.design.entity.CustomerTest;
import com.cardy.design.entity.Product;
import com.cardy.design.util.Util;
import com.cardy.design.viewmodel.ProductViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chad.library.adapter.base.module.BaseDraggableModule;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;


import java.util.Arrays;
import java.util.List;

public class ProductListAdapter extends BaseQuickAdapter<Product, MyProductViewHolder> implements DraggableModule {
    List<Product> list;
    ProductViewModel viewModel;
    ActivityResultLauncher<Intent> launcher;

    EditText editTextName,editTextModel,editTextUsedMaterial,editTextPrice,editTextImagePath;
    ImageView imageProduct;

    public ProductListAdapter(int layoutResId,ProductViewModel viewModel,ActivityResultLauncher<Intent> intentActivityResultLauncher) {
        super(layoutResId);
        this.viewModel = viewModel;
        this.launcher = intentActivityResultLauncher;
        initClickListener();
        initSwipeListener();
    }

    @Override
    protected void convert(@NonNull MyProductViewHolder holder, Product product) {
        //填充数值
        try{
            if(!product.getImage().equals("")){
                if(!product.getImage().startsWith("http")){
                    Uri pathUri = Util.getImagePath(getContext(),product.getImage());
                    holder.imageView.setImageURI(pathUri);
                }
                else {
                    Picasso.with(getContext()).load(product.getImage()).into(holder.imageView);
                }
            }
            else
                throw new Exception();
        }catch (Exception exception){
            holder.imageView.setImageDrawable(getContext().getDrawable(R.drawable.product_placeholder));
        }
        holder.name.setText(product.getName());
        holder.model.setText(product.getModel());
        holder.material.setText(product.getUsedMaterial());
        holder.price.setText("￥"+ String.valueOf(product.getPrice()));
    }


    public void initClickListener(){
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BottomDialog.show("修改产品信息",new OnBindView<BottomDialog>(R.layout.dialog_add_product) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {

                        imageProduct = v.findViewById(R.id.imageViewProduct);
                        editTextImagePath = v.findViewById(R.id.editTextImagePath);
                        editTextName = v.findViewById(R.id.editTextName);
                        editTextModel = v.findViewById(R.id.editTextModel);
                        editTextUsedMaterial = v.findViewById(R.id.editTextUsedMaterial);
                        editTextPrice = v.findViewById(R.id.editTextPrice);
                        editTextModel.setFocusable(false);

                        //填充数值
                        if (!list.get(position).getImage().equals("")) {
                            Picasso.with(getContext()).load(list.get(position).getImage()).into(imageProduct);
                        }
                        editTextImagePath.setText(list.get(position).getImage());
                        editTextName.setText(list.get(position).getName());
                        editTextModel.setText(list.get(position).getModel());
                        editTextUsedMaterial.setText(list.get(position).getUsedMaterial());
                        editTextPrice.setText(String.valueOf(list.get(position).getPrice()));

                        //添加点击事件
                        imageProduct.setOnClickListener(imageView->{
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            launcher.launch(intent);
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
                        try {
                            viewModel.updateProduct(product);
                            PopTip.show("添加成功");
                        } catch (Exception exception) {
                            PopTip.show("添加出错");
                        }
                        return false;
                    }
                }).setCancelButton("取消");
            }
        });
    }
    
    public void initSwipeListener(){
        addDraggableModule(this);
        // 侧滑监听
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            Product product;

            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "view swiped start: " + pos);
                product = list.get(pos);
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "view swiped reset: " + pos);
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "View Swiped: " + pos);
                viewModel.deleteProduct(product);
                PopTip.show("产品信息已删除","撤回").setOnButtonClickListener(new OnDialogButtonClickListener<PopTip>() {
                    @Override
                    public boolean onClick(PopTip baseDialog, View v) {
                        PopTip.show("已撤销删除操作");
                        viewModel.insertProduct(product);
                        return false;
                    }
                });
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(ContextCompat.getColor(getContext(), R.color.background_gray));
            }
        };
        getDraggableModule().setSwipeEnabled(true);
        getDraggableModule().setOnItemSwipeListener(onItemSwipeListener);
        //END即只允许向右滑动
        getDraggableModule().getItemTouchHelperCallback().setSwipeMoveFlags(ItemTouchHelper.END);
    }
    
    /**
     * 重写setList方法，更新列表数据
     *
     * @param list
     */
    public void setMyList(List<Product> list) {
        this.list = list;
    }

    /**
     * 重写setNewInstance方法，初始化数据时同时更新adapter内部list
     *
     * @param list
     */
    @Override
    public void setNewInstance(@Nullable List<Product> list) {
        super.setNewInstance(list);
        this.list = list;
    }

    public void setImage(Uri uri){
        if(imageProduct != null && editTextImagePath != null) {
            Picasso.with(getContext()).load(uri).into(imageProduct);
            editTextImagePath.setText(uri.toString());
        }
    }

    @NonNull
    @Override
    public BaseDraggableModule addDraggableModule(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new BaseDraggableModule(baseQuickAdapter);
    }
}

class MyProductViewHolder extends BaseViewHolder{
    TextView name,model,price,material;
    ImageView imageView;
    public MyProductViewHolder(@NonNull View view) {
        super(view);
        imageView = view.findViewById(R.id.productImageView);
        name = view.findViewById(R.id.textViewName);
        model = view.findViewById(R.id.textViewModel);
        material = view.findViewById(R.id.productUsedMaterial);
        price = view.findViewById(R.id.productPrice);
    }
}
