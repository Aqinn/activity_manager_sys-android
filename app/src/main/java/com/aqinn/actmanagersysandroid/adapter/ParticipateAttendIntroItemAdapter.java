package com.aqinn.actmanagersysandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.datafortest.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.datafortest.DataSource;
import com.aqinn.actmanagersysandroid.datafortest.Observer;
import com.aqinn.actmanagersysandroid.datafortest.ParticipateAttendIntroItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aqinn
 * @date 2020/12/12 8:03 PM
 */
public class ParticipateAttendIntroItemAdapter extends BaseAdapter implements Observer {

    private static final String TAG = "ParticipateAttendIntroI";

    private DataSource<ParticipateAttendIntroItem> mDataSource;
    private Context mContext;

    public ParticipateAttendIntroItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDataSource(DataSource<ParticipateAttendIntroItem> dataSource) {
        if (this.mDataSource != null)
            this.mDataSource.disposed(this);
        this.mDataSource = dataSource;
        this.mDataSource.attach(this);
    }

    public ParticipateAttendIntroItemAdapter(Context mContext, DataSource<ParticipateAttendIntroItem> dataSource) {
        this.mContext = mContext;
        this.mDataSource = dataSource;
        this.mDataSource.attach(this);
    }

    private List<ParticipateAttendIntroItem> getParticipateAttendIntroItemList() {
        return this.mDataSource.getDatas();
    }

    @Override
    public int getCount() {
        return getParticipateAttendIntroItemList().size();
    }

    @Override
    public Object getItem(int position) {
        return getParticipateAttendIntroItemList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ParticipateAttendIntroItemAdapter.ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_participate_attend_intro, parent, false);
            viewHolder = new ParticipateAttendIntroItemAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ParticipateAttendIntroItemAdapter.ViewHolder) convertView.getTag();
        }
        ParticipateAttendIntroItem item = getParticipateAttendIntroItemList().get(position);
        viewHolder.tv_name.setText(item.getName());
        viewHolder.tv_time.setText(item.getTime());
        viewHolder.tv_type.setText(item.getType());
        viewHolder.tv_u_status.setText(item.getuStatus());
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
        viewHolder.cl_item_participate_attend_intro_inner.setBackground(mContext.getDrawable(statusBgColor));
//        viewHolder.cl_item_participate_attend_intro_inner.getBackground().mutate().setAlpha(153);
        return convertView;
    }

    @Override
    public void update() {
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.cl_item_participate_attend_intro_inner)
        ConstraintLayout cl_item_participate_attend_intro_inner;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_time) TextView tv_time;
        @BindView(R.id.tv_type) TextView tv_type;
        @BindView(R.id.tv_u_status) TextView tv_u_status;
        @BindView(R.id.tv_status) TextView tv_status;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}