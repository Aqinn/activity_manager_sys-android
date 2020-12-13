package com.aqinn.actmanagersysandroid.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.data.ActIntroItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 活动简介列表的适配器
 * @author Aqinn
 * @date 2020/12/12 3:29 PM
 */
public class ActIntroItemAdapter extends BaseAdapter {

    private static final String TAG = "ActIntroItemAdapter";

    private List<ActIntroItem> actIntroItemList;
    private Context mContext;

    public ActIntroItemAdapter(List<ActIntroItem> actIntroItemList, Context mContext) {
        this.actIntroItemList = actIntroItemList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return actIntroItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return actIntroItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_act_intro, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ActIntroItem item = actIntroItemList.get(position);
        viewHolder.tv_name.setText(item.getName());
        viewHolder.tv_time.setText(item.getTime());
        viewHolder.tv_loc.setText(item.getLocation());
        viewHolder.tv_intro_text.setText("活动简介");
        viewHolder.tv_intro.setText(item.getIntro());
        viewHolder.tv_status.setText(item.getStatus());
        Log.d(TAG, "getView: " + item.getStatus());
        int statusTextColor = R.color.thing_default;
        int statusBgColor = R.color.thing_default_bg;
        if ("进行中".equals(item.getStatus())) {
            statusTextColor = R.color.thing_ing;
            statusBgColor = R.drawable.background_thing_ing;
        }
        if ("未开始".equals(item.getStatus())) {
            statusTextColor = R.color.thing_not_begin;
            statusBgColor = R.drawable.background_thing_not_begin;
        }
        if ("已结束".equals(item.getStatus())) {
            statusTextColor = R.color.thing_finish;
            statusBgColor = R.drawable.background_thing_finish;
        }
        viewHolder.tv_status.setTextColor(mContext.getResources().getColor(statusTextColor));
        viewHolder.cl_item_act_intro_inner.setBackground(mContext.getDrawable(statusBgColor));
        viewHolder.cl_item_act_intro_inner.getBackground().mutate().setAlpha(153);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.cl_item_act_intro_inner) ConstraintLayout cl_item_act_intro_inner;
        @BindView(R.id.tv_name) TextView tv_name;
        @BindView(R.id.tv_time) TextView tv_time;
        @BindView(R.id.tv_loc) TextView tv_loc;
        @BindView(R.id.tv_intro_text) TextView tv_intro_text;
        @BindView(R.id.tv_intro) TextView tv_intro;
        @BindView(R.id.tv_status) TextView tv_status;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}