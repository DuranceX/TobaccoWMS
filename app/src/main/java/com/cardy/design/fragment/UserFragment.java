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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import com.cardy.design.R;
import com.cardy.design.adapter.UserListAdapter;
import com.cardy.design.entity.CustomerTest;
import com.cardy.design.entity.User;
import com.cardy.design.util.TestDatabase;
import com.cardy.design.util.WMSDatabase;
import com.cardy.design.widget.IconFontTextView;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    UserListAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    IconFontTextView addButton,menuButton;
    List<User> list;
    LiveData<List<User>> listLive;

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
        adapter = new UserListAdapter(R.layout.item_user_information);
        adapter.setAnimationEnable(true);
        recyclerView = getView().findViewById(R.id.userRecycleview);
        searchView = getView().findViewById(R.id.userSearchView);
        addButton = getView().findViewById(R.id.userAddButton);
        menuButton = getView().findViewById(R.id.menuButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        //TODO: 从数据库中获取数据
        listLive = TestDatabase.Companion.getINSTANCE().userDao().getAllUser();
        listLive.observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.setList(users);
                list = users;
            }
        });

        addButton.setOnClickListener(v->{
            final EditText[] editTextId = new EditText[1];
            final EditText[] editTextName = new EditText[1];
            final EditText[] editTextPassword = new EditText[1];
            final Switch[] adminSwitch = new Switch[1];
            BottomDialog.show("添加用户",new OnBindView<BottomDialog>(R.layout.dialog_add_user) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    //TODO: 添加“添加”事件
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

                User user = new User(Id,name,password,permission);
                try{
                    TestDatabase.Companion.getINSTANCE().userDao().insertUser(user);
                    PopTip.show("添加成功");
                }catch(Exception exception){
                    PopTip.show("添加出错");
                }
                return false;
            }).setCancelButton("取消");
        });

        menuButton.setOnClickListener(v->{
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });


        // 侧滑监听
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            User user;

            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "view swiped start: " + pos);
                user = list.get(pos);
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "view swiped reset: " + pos);
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d("Swipe", "View Swiped: " + pos);
                // TODO: 调用Supplier的删除方法
                TestDatabase.Companion.getINSTANCE().userDao().deleteUser(user);
                PopTip.show("用户信息已删除","撤回").setOnButtonClickListener(new OnDialogButtonClickListener<PopTip>() {
                    @Override
                    public boolean onClick(PopTip baseDialog, View v) {
                        // TODO: 调用Supplier的添加方法重新添加
                        PopTip.show("已撤销删除操作");
                        adapter.addData(pos,user);
                        TestDatabase.Companion.getINSTANCE().userDao().insertUser(user);
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