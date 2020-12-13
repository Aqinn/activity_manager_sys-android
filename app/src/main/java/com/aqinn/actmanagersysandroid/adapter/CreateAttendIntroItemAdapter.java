package com.aqinn.actmanagersysandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.data.CreateAttendIntroItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aqinn
 * @date 2020/12/12 8:02 PM
 */
public class CreateAttendIntroItemAdapter extends BaseAdapter {

    private static final String TAG = "CreateAttendIntroItemAd";

    private List<CreateAttendIntroItem> createAttendIntroItemList;
    private Context mContext;

    public CreateAttendIntroItemAdapter(List<CreateAttendIntroItem> createAttendIntroItemList, Context mContext) {
        this.createAttendIntroItemList = createAttendIntroItemList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return createAttendIntroItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return createAttendIntroItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CreateAttendIntroItemAdapter.ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_create_attend_intro, parent, false);
            viewHolder = new CreateAttendIntroItemAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CreateAttendIntroItemAdapter.ViewHolder) convertView.getTag();
        }
        CreateAttendIntroItem item = createAttendIntroItemList.get(position);
        viewHolder.tv_name.setText(item.getName());
        viewHolder.tv_time.setText(item.getTime());
        viewHolder.tv_type.setText(item.getType());
        viewHolder.tv_count.setText("应签到/未签到/已签到 = " + item.getShouldAttendCount() + "/"
                + item.getHaveAttendCount() + "/"
                + item.getNotAttendCount());
        viewHolder.tv_status.setText(item.getStatus());
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
        viewHolder.cl_item_create_attend_intro_inner.setBackground(mContext.getDrawable(statusBgColor));
        viewHolder.cl_item_create_attend_intro_inner.getBackground().mutate().setAlpha(153);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.cl_item_create_attend_intro_inner)
        ConstraintLayout cl_item_create_attend_intro_inner;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_time) TextView tv_time;
        @BindView(R.id.tv_type) TextView tv_type;
        @BindView(R.id.tv_count) TextView tv_count;
        @BindView(R.id.tv_status) TextView tv_status;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
