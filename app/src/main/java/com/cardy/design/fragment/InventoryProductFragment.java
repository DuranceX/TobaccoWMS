package com.cardy.design.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cardy.design.R;
import com.cardy.design.adapter.InventoryListAdapter;
import com.cardy.design.entity.InventoryTest;
import com.cardy.design.widget.IconFontTextView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InventoryProductFragment extends Fragment {

    InventoryListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton,menuButton;

    public InventoryProductFragment() {
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
        return inflater.inflate(R.layout.fragment_inventory_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new InventoryListAdapter(R.layout.item_inventory_product);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.inventoryProductRecycleview);
        searchView = getView().findViewById(R.id.inventoryProductSearchView);
        addButton = getView().findViewById(R.id.inventoryProductAddButton);
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        //TODO: 从数据库中获取数据
        List<InventoryTest> list = new ArrayList<InventoryTest>(5);
        for (int i = 0; i < 5; i++) {
            InventoryTest inventoryTest = new InventoryTest("“云烟”香烟","SC20210312","",300,200,"B区22号",InventoryTest.TYPE_PRODUCT);
            list.add(inventoryTest);
        }

        adapter.setList(list);

        addButton.setOnClickListener(v->{
            BottomDialog.show("确认送达",new OnBindView<BottomDialog>(R.layout.dialog_inventory_check_product) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    //TODO: 添加“添加”事件
                    Spinner spinner;
                    TextView name,model,customer,count,price,saleDate,calendar;
                    EditText deliveryDate;
                    final int[] mYear = new int[1];
                    final int[] mMonth = new int[1];
                    final int[] mDay = new int[1];
                    mYear[0] = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
                    mMonth[0] = Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH);
                    mDay[0] = Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_MONTH);

                    spinner = v.findViewById(R.id.orderSpinner);
                    name = v.findViewById(R.id.textViewName);
                    model = v.findViewById(R.id.textViewModel);
                    customer = v.findViewById(R.id.textViewCustomer);
                    count = v.findViewById(R.id.textViewCount);
                    price = v.findViewById(R.id.textViewPrice);
                    saleDate = v.findViewById(R.id.textViewSaleDate);
                    calendar = v.findViewById(R.id.textViewCalendar);
                    deliveryDate = v.findViewById(R.id.editTextDeliveryDate);

                    calendar.setOnClickListener(view1->{
                        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mYear[0] = year;
                                mMonth[0] = monthOfYear;
                                mDay[0] = dayOfMonth;
                                deliveryDate.setText(new StringBuilder().append(mYear[0]).append("-")
                                        .append(mMonth[0] + 1).append("-").append(mDay[0]));
                            }
                        };
                        new DatePickerDialog(getActivity(), mDateSetListener, mYear[0], mMonth[0], mDay[0]).show();
                    });
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