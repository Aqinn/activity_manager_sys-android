package com.aqinn.actmanagersysandroid.adapter;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.StreamBlazeFaceDetector.StreamBlazeFaceDetectActivity;
import com.aqinn.actmanagersysandroid.StreamBlazeFaceDetector.StreamBlazeFaceDetectFragment;
import com.aqinn.actmanagersysandroid.activity.LoginActivity;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Observer;
import com.aqinn.actmanagersysandroid.entity.show.UserDesc;
import com.aqinn.actmanagersysandroid.fragment.BaseFragment;
import com.aqinn.actmanagersysandroid.utils.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Aqinn
 * @date 2020/12/12 9:06 PM
 */
public class UserDescRecyclerViewAdapter extends RecyclerView.Adapter<UserDescRecyclerViewAdapter.ViewHolder> implements Observer {

    private static final String TAG = "UserDescRecyclerViewAda";

    private Context mContext;
    private DataSource<UserDesc> mDataSource;
    private UserDesc mUserDesc;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public UserDescRecyclerViewAdapter(Context context, DataSource<UserDesc> dataSource) {
        mContext = context;
        mDataSource = dataSource;
        mDataSource.attach(this);
        mUserDesc = initUserDesc();
    }

    private UserDesc initUserDesc() {
        if (mDataSource.getDatas().isEmpty())
            return new UserDesc(-1L, "没有登录", "没有登录", 1, "没有登录", "没有登录");
        return ((UserDesc) mDataSource.getDatas().get(0));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View root = inflater.inflate(R.layout.item_recycler_personal, viewGroup, false);
        return new ViewHolder(root, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // 设置 ViewHolder 里的属性
        viewHolder.setAccount(mUserDesc.getAccount());
        viewHolder.setName(mUserDesc.getName());
        viewHolder.setSex(mUserDesc.getSex() == 1 ? "男" : "女");
        viewHolder.setContact(mUserDesc.getContact());
        viewHolder.setDesc(mUserDesc.getDesc());
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(RecyclerView.ViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    @Override
    public void update() {
        this.mUserDesc = initUserDesc();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.cl_top)
        ConstraintLayout cl_top;
        @BindView(R.id.cl_bottom)
        ConstraintLayout cl_bottom;
        @BindView(R.id.tv_account_text)
        TextView tv_account_text;
        @BindView(R.id.tv_account)
        TextView tv_account;
        @BindView(R.id.tv_name_text)
        TextView tv_name_text;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_sex_text)
        TextView tv_sex_text;
        @BindView(R.id.tv_sex)
        TextView tv_sex;
        @BindView(R.id.tv_contact_text)
        TextView tv_contact_text;
        @BindView(R.id.tv_contact)
        TextView tv_contact;
        @BindView(R.id.tv_desc_text)
        TextView tv_desc_text;
        @BindView(R.id.tv_desc)
        TextView tv_desc;
        @BindView(R.id.bt_gather_face)
        Button bt_gather_face;
        @BindView(R.id.bt_settings)
        Button bt_settings;
        @BindView(R.id.bt_logout)
        Button bt_logout;


        private UserDescRecyclerViewAdapter mAdapter;

        public ViewHolder(View itemView, UserDescRecyclerViewAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mAdapter = adapter;
        }

        public void setAccount(String account) {
            tv_account.setText(account);
        }

        public void setName(String name) {
            tv_name.setText(name);
        }

        public void setSex(String sex) {
            tv_sex.setText(sex);
        }

        public void setContact(String contact) {
            tv_contact.setText(contact);
        }

        public void setDesc(String desc) {
            tv_desc.setText(desc);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        @OnClick(R.id.bt_gather_face)
        public void gatherFace() {
            Log.d(TAG, "gatherFace: 点击了人脸采集按钮");
            Intent intent = new Intent();
            Activity activity = (FragmentActivity) mContext;
            intent.setClass(activity, StreamBlazeFaceDetectActivity.class);
            activity.startActivity(intent);
        }

        @OnClick(R.id.bt_settings)
        public void showSetting() {
            Log.d(TAG, "showSetting: 点击了设置按钮");
        }

        @OnClick(R.id.bt_logout)
        public void logout() {
            Log.d(TAG, "logout: 点击了退出登录按钮");
            CommonUtil.setNowUserIdToSP(mContext, CommonUtil.ERR_USER_ID);
            CommonUtil.setNowUsernameToSP(mContext, null);
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
            ((FragmentActivity) mContext).finish();
        }

    }
}
