package com.cardy.design.adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cardy.design.R;
import com.cardy.design.entity.User;
import com.cardy.design.viewmodel.UserViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.module.BaseDraggableModule;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

import java.util.List;

public class UserListAdapter extends BaseQuickAdapter<User, MyUserViewHolder> implements DraggableModule, LoadMoreModule {
    public UserListAdapter(int layoutResId, UserViewModel userViewModel) {
        super(layoutResId);
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                List<User> list = getData();
                final EditText[] editTextId = new EditText[1];
                final EditText[] editTextName = new EditText[1];
                final EditText[] editTextPassword = new EditText[1];
                final Switch[] adminSwitch = new Switch[1];

                BottomDialog.show("修改用户信息", new OnBindView<BottomDialog>(R.layout.dialog_add_user) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        editTextId[0] = v.findViewById(R.id.editTextId);
                        editTextName[0] = v.findViewById(R.id.editTextName);
                        editTextPassword[0] = v.findViewById(R.id.editTextPassword);
                        adminSwitch[0] = v.findViewById(R.id.adminSwitch);

                        editTextId[0].setFocusable(false);
                        editTextId[0].setText(list.get(position).getId());
                        editTextName[0].setText(list.get(position).getUsername());
                        editTextPassword[0].setText(list.get(position).getPassword());
                        adminSwitch[0].setChecked(list.get(position).getPermission());
                    }
                }).setOkButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
                    @Override
                    public boolean onClick(BottomDialog baseDialog, View v) {
                        String Id = editTextId[0].getText().toString();
                        String name = editTextName[0].getText().toString();
                        String password = editTextPassword[0].getText().toString();
                        boolean permission = adminSwitch[0].isChecked();

                        User user = new User(Id, name, password, permission);
                        try {
                            userViewModel.updateUser(user);
                            PopTip.show("修改成功");
                        } catch (Exception exception) {
                            PopTip.show("修改信息出错");
                        }
                        return false;
                    }
                }).setCancelButton("取消");
            }
        });
        addDraggableModule(this);
    }

    @Override
    protected void convert(@NonNull MyUserViewHolder holder, User user) {
        holder.id.setText(user.getId());
        holder.name.setText(user.getUsername());
        holder.password.setText(user.getPassword());
        if (user.getPermission())
            holder.avatar.setImageDrawable(getContext().getDrawable(R.drawable.manager_green));
        else
            holder.avatar.setImageDrawable(getContext().getDrawable(R.drawable.normal_blue));
    }

    @NonNull
    @Override
    public BaseDraggableModule addDraggableModule(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new BaseDraggableModule(baseQuickAdapter);
    }

    @NonNull
    @Override
    public BaseLoadMoreModule addLoadMoreModule(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new BaseLoadMoreModule(baseQuickAdapter);
    }
}

class MyUserViewHolder extends BaseViewHolder {
    TextView id, name, password;
    ImageView avatar;

    public MyUserViewHolder(@NonNull View view) {
        super(view);
        id = view.findViewById(R.id.userId);
        name = view.findViewById(R.id.userName);
        password = view.findViewById(R.id.userPassword);
        avatar = view.findViewById(R.id.imageView);
    }
}