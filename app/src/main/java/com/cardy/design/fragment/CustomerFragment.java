package com.cardy.design.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import com.cardy.design.R;
import com.cardy.design.adapter.CustomerListAdapter;
import com.cardy.design.entity.CustomerTest;
import com.cardy.design.widget.IconFontTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.interfaces.OnBindView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class CustomerFragment extends Fragment {

    CustomerListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton;


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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        CustomerTest[] list = new CustomerTest[5];
        for (int i = 0; i < 5; i++) {
            list[i] = new CustomerTest();
            list[i].setName("陆玩具有限责任公司");
            list[i].setAddress("天河区大信商圈大信南路32号");
            list[i].setMainPurchase(new String[]{"烟草","香烟"});
        }

        List<CustomerTest> newList = Arrays.asList(list);
        adapter.setNewInstance(newList);

        addButton.setOnClickListener(v->{
            FullScreenDialog.show(new OnBindView<FullScreenDialog>(R.layout.dialog_add_customer) {
                @Override
                public void onBind(FullScreenDialog dialog, View v) {

                }
            });
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BottomDialog.show("修改客户信息", "这里是对话框内容", new OnBindView<BottomDialog>(R.layout.dialog_add_customer) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {

                    }
                });
            }
        });
    }
}