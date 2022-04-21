package com.cardy.design.fragment;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cardy.design.R;
import com.cardy.design.adapter.ProductListAdapter;
import com.cardy.design.entity.CustomerTest;
import com.cardy.design.widget.IconFontTextView;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    ProductListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton,menuButton;

    public ProductFragment() {
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
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ProductListAdapter(R.layout.item_product_information);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.productRecycleview);
        searchView = getView().findViewById(R.id.productSearchView);
        addButton = getView().findViewById(R.id.productAddButton);
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        //TODO: 从数据库中获取数据
        List<CustomerTest> list = new ArrayList<CustomerTest>(5);
        for (int i = 0; i < 5; i++) {
            CustomerTest customerTest = new CustomerTest();
            customerTest.setName("“中华”香烟");
            customerTest.setAddress("SC20210512");
            customerTest.setMainPurchase(new String[]{"烟草","香烟"});
            list.add(customerTest);
        }
        list.get(1).setName("”云烟“香烟");
        list.get(1).setAddress("SC20200123");
        list.get(1).setMainPurchase(new String[]{"烟草","香烟","测试产品"});

        adapter.setNewInstance(list);
        adapter.setList(list);

        addButton.setOnClickListener(v->{
            BottomDialog.show("添加产品",new OnBindView<BottomDialog>(R.layout.dialog_add_product) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    //TODO: 添加“添加”事件
                    EditText editTextName,editTextModel,editTextUsedMaterial,editTextPrice;

                    editTextName = v.findViewById(R.id.editTextName);
                    editTextModel = v.findViewById(R.id.editTextModel);
                    editTextUsedMaterial = v.findViewById(R.id.editTextUsedMaterial);
                    editTextPrice = v.findViewById(R.id.editTextPrice);
                }
            }).setOkButton("确定").setCancelButton("取消");
        });

        menuButton.setOnClickListener(v->{
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });


        // 侧滑监听
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            CustomerTest customer;

            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "view swiped start: " + pos);
                customer = list.get(pos);
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "view swiped reset: " + pos);
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "View Swiped: " + pos);
                // TODO: 调用Customer的删除方法
                PopTip.show("产品信息已删除","撤回").setOnButtonClickListener(new OnDialogButtonClickListener<PopTip>() {
                    @Override
                    public boolean onClick(PopTip baseDialog, View v) {
                        // TODO: 调用Customer的添加方法重新添加
                        PopTip.show("已撤销删除操作");
                        adapter.addData(pos,customer);
                        return false;
                    }
                });
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(ContextCompat.getColor(getContext(), R.color.background_gray));
            }
        };

        adapter.getDraggableModule().setSwipeEnabled(true);
        adapter.getDraggableModule().setOnItemSwipeListener(onItemSwipeListener);
        //END即只允许向右滑动
        adapter.getDraggableModule().getItemTouchHelperCallback().setSwipeMoveFlags(ItemTouchHelper.END);
    }
}