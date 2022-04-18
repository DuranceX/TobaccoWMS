package com.cardy.design.fragment;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import com.cardy.design.R;
import com.cardy.design.adapter.SupplierListAdapter;
import com.cardy.design.entity.CustomerTest;
import com.cardy.design.widget.IconFontTextView;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

import java.util.ArrayList;
import java.util.List;

//TODO: 将CustomerTest改成Supplier
public class SupplierFragment extends Fragment {

    SupplierListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton,menuButton;

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
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        //TODO: 从数据库中获取数据
        List<CustomerTest> list = new ArrayList<CustomerTest>(5);
        for (int i = 0; i < 5; i++) {
            CustomerTest customerTest = new CustomerTest();
            customerTest.setName("陆玩具有限责任公司");
            customerTest.setAddress("天河区大信商圈大信南路32号");
            customerTest.setMainPurchase(new String[]{"烟草","香烟"});
            list.add(customerTest);
        }
        list.get(1).setName("测试有限责任公司");
        list.get(1).setAddress("测试区测试路32号");
        list.get(1).setMainPurchase(new String[]{"烟草","香烟","测试产品"});

        adapter.setNewInstance(list);
        adapter.setList(list);

        addButton.setOnClickListener(v->{
            BottomDialog.show("添加供货商",new OnBindView<BottomDialog>(R.layout.dialog_add_customer_supplier) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    //TODO: 添加“添加”事件
                    TextView textViewNameLabel,textViewAddressLabel,textViewMainLabel,textViewPriorityLabel;
                    textViewNameLabel = v.findViewById(R.id.textViewNameLabel);
                    textViewAddressLabel = v.findViewById(R.id.textViewModelLabel);
                    textViewMainLabel = v.findViewById(R.id.textViewPriceLabel);
                    textViewPriorityLabel = v.findViewById(R.id.textViewPriorityLabel);

                    //修改界面为供货商
                    textViewNameLabel.setText("供货商名称：");
                    textViewAddressLabel.setText("供货商地址：");
                    textViewMainLabel.setText("主要供货原料：");
                    textViewPriorityLabel.setText("供货商优先级：");
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
                // TODO: 调用Supplier的删除方法
                PopTip.show("供货商信息已删除","撤回").setOnButtonClickListener(new OnDialogButtonClickListener<PopTip>() {
                    @Override
                    public boolean onClick(PopTip baseDialog, View v) {
                        // TODO: 调用Supplier的添加方法重新添加
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