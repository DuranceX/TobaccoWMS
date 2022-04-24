package com.cardy.design.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

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
import com.cardy.design.adapter.UserListAdapter;
import com.cardy.design.entity.User;
import com.cardy.design.util.diff.UserDiffCallback;
import com.cardy.design.viewmodel.UserViewModel;
import com.cardy.design.widget.IconFontTextView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnBindView;

import java.util.List;

public class UserFragment extends Fragment {

    UserListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton, menuButton;
    UserViewModel userViewModel;
    List<User> list;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.getLoadMoreModule().setEnableLoadMore(true);

        //设置diffCallback
        adapter.setDiffCallback(new UserDiffCallback());

        userViewModel.getAllUserLive().observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (adapter.getData().size() == 0)
                    adapter.setNewInstance(users);
                //通过setDiffNewData来通知adapter数据发生变化，并保留动画
                adapter.setDiffNewData(users);
                adapter.setList(users);
                //重写的setList方法更新adapter中的list数据
                list = users;
            }
        });

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
                try {
                    userViewModel.insertUser(user);
                    adapter.addData(user);
                    PopTip.show("添加成功");
                } catch (Exception exception) {
                    PopTip.show("添加出错");
                }
                return false;
            }).setCancelButton("取消");
        });

        menuButton.setOnClickListener(v -> {
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });
    }
}