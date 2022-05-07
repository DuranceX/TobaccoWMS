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
import com.cardy.design.adapter.CustomerListAdapter;
import com.cardy.design.entity.Customer;
import com.cardy.design.util.diff.CustomerDIffCallback;
import com.cardy.design.viewmodel.CustomerViewModel;
import com.cardy.design.widget.IconFontTextView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomerFragment extends Fragment {

    CustomerListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton,menuButton;
    CustomerViewModel viewModel;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;

    ImageView imageViewLogo;
    EditText editTextLogo;

    Boolean firstFlag = true;

    public CustomerFragment() {
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
        return inflater.inflate(R.layout.fragment_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        adapter = new CustomerListAdapter(R.layout.item_customer_information,viewModel,intentActivityResultLauncher);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.customerRecycleview);
        searchView = getView().findViewById(R.id.customerSearchView);
        addButton = getView().findViewById(R.id.customerAddButton);
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty_layout);

        adapter.setDiffCallback(new CustomerDIffCallback());

        viewModel.getAllCustomerLive().observe(getActivity(), new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                if(searchView.getQuery().equals("") || firstFlag){
                    if (adapter.getData().size() == 0)
                        adapter.setNewInstance(customers);
                    //通过setDiffNewData来通知adapter数据发生变化，并保留动画
                    adapter.setDiffNewData(customers);
                    //重写的setList方法更新adapter中的list数据
                    adapter.setMyList(customers);
                    firstFlag = false;
                }
            }
        });

        initClickListener();
        initSearch();

        menuButton.setOnClickListener(v->{
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });
    }

    public void initClickListener(){
        addButton.setOnClickListener(v->{
            final TextView[] textViewNameLabel = new TextView[1];
            final TextView[] textViewAddressLabel = new TextView[1];
            final TextView[] textViewMainLabel = new TextView[1];
            final TextView[] textViewPriorityLabel = new TextView[1];
            final TextView[] textViewMain = new TextView[1];
            final EditText[] editTextName = new EditText[1];
            final EditText[] editTextAddress = new EditText[1];
            final RadioButton[] radioButtonLow = new RadioButton[1];
            final RadioButton[] radioButtonMid = new RadioButton[1];
            final RadioButton[] radioButtonHigh = new RadioButton[1];
            BottomDialog.show("添加客户",new OnBindView<BottomDialog>(R.layout.dialog_add_customer_supplier) {
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
                    textViewMain[0] = v.findViewById(R.id.textViewMain);
                    radioButtonLow[0] = v.findViewById(R.id.radioButtonLow);
                    radioButtonMid[0] = v.findViewById(R.id.radioButtonMid);
                    radioButtonHigh[0] = v.findViewById(R.id.radioButtonHigh);

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
                    String main = textViewMain[0].getText().toString();
                    int priority = Customer.PRIORITY_LOW;
                    if (radioButtonLow[0].isChecked())
                        priority = Customer.PRIORITY_LOW;
                    else if (radioButtonMid[0].isChecked())
                        priority = Customer.PRIORITY_MIDDLE;
                    else if (radioButtonHigh[0].isChecked())
                        priority = Customer.PRIORITY_HIGH;

                    Customer customer = new Customer(name, address, priority, logo, main);
                    viewModel.insertCustomer(customer);
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
                viewModel.getAllQueriedCustomerLive(newText).observe(getActivity(), new Observer<List<Customer>>() {
                    @Override
                    public void onChanged(List<Customer> customers) {
                        adapter.setDiffNewData(customers);
                        adapter.setMyList(customers);
                    }
                });
                return false;
            }
        });
    }
}