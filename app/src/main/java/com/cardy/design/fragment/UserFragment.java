package com.cardy.design.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

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

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;
import com.cardy.design.R;
import com.cardy.design.adapter.UserListAdapter;
import com.cardy.design.entity.User;
import com.cardy.design.util.ExcelUtil;
import com.cardy.design.util.diff.UserDiffCallback;
import com.cardy.design.viewmodel.UserViewModel;
import com.cardy.design.widget.IconFontTextView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnBindView;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    UserListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    Button addMultiButton;
    IconFontTextView addButton, menuButton;
    UserViewModel userViewModel;

    Boolean flag = true;

    public UserFragment() {
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
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        adapter = new UserListAdapter(R.layout.item_user_information, userViewModel);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.userRecycleview);
        searchView = getView().findViewById(R.id.userSearchView);
        addButton = getView().findViewById(R.id.userAddButton);
        menuButton = getView().findViewById(R.id.menuButton);
        addMultiButton = getView().findViewById(R.id.addMultiUserButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty_layout);

        //设置diffCallback
        adapter.setDiffCallback(new UserDiffCallback());

        userViewModel.getAllUserLive().observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if(searchView.getQuery().equals("") || flag){
                    if (adapter.getData().size() == 0)
                        adapter.setNewInstance(users);
                    //通过setDiffNewData来通知adapter数据发生变化，并保留动画
                    adapter.setDiffNewData(users);
                    adapter.setList(users);
                }
            }
        });

       initAddMethod();
       initSearch();

        menuButton.setOnClickListener(v -> {
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

        addMultiButton.setOnClickListener(v->{
            Intent i2 = new Intent(getActivity().getApplicationContext(), FileChooser.class);
            i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
            startActivityForResult(i2, 988);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 988 && resultCode == Activity.RESULT_OK) {
            List<User> users = new ArrayList<>();
            Uri file = data.getData();
            String filePath = file.toString().replaceFirst("file:/","");
            if (!filePath.contains(".xls")){
                Toast.makeText(getActivity(),"不支持的文件类型！",Toast.LENGTH_LONG).show();
            }else{
                List<List<Object>> list = ExcelUtil.read2003XLS(filePath);
                for (int i = 1; i < list.size(); i++) {
                    String ID = list.get(i).get(0).toString();
                    String username = list.get(i).get(1).toString();
                    String password = list.get(i).get(2).toString();
                    boolean permission = list.get(i).get(3).toString().equals("1");
                    User user = new User(ID,username,password,permission);
                    Log.d("ExcelTest", user.toString());
                    users.add(user);
                }
                userViewModel.insertUser(users.toArray(new User[users.size()]));
            }
        }
    }

    public void initAddMethod(){
        addButton.setOnClickListener(v -> {
            final EditText[] editTextId = new EditText[1];
            final EditText[] editTextName = new EditText[1];
            final EditText[] editTextPassword = new EditText[1];
            final Switch[] adminSwitch = new Switch[1];
            BottomDialog.show("添加用户", new OnBindView<BottomDialog>(R.layout.dialog_add_user) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    editTextId[0] = v.findViewById(R.id.editTextId);
                    editTextName[0] = v.findViewById(R.id.editTextName);
                    editTextPassword[0] = v.findViewById(R.id.editTextPassword);
                    adminSwitch[0] = v.findViewById(R.id.adminSwitch);

                    editTextId[0].setFocusable(true);
                }
            }).setOkButton("确定", (baseDialog, v1) -> {
                String Id = editTextId[0].getText().toString();
                String name = editTextName[0].getText().toString();
                String password = editTextPassword[0].getText().toString();
                boolean permission = adminSwitch[0].isChecked();

                User user = new User(Id, name, password, permission);
                userViewModel.insertUser(user);
                return false;
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
                userViewModel.getAllQueriedUserLive(newText).observe(getActivity(), new Observer<List<User>>() {
                    @Override
                    public void onChanged(List<User> users) {
                        flag = false;
                        adapter.setDiffNewData(users);
                        adapter.setList(users);
                    }
                });
                return false;
            }
        });
    }
}