package com.aqinn.actmanagersysandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.data.show.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Observer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aqinn
 * @date 2020/12/12 8:02 PM
 */
public class CreateAttendIntroItemAdapter extends BaseAdapter implements Observer {

    private static final String TAG = "CreateAttendIntroItemAd";

    private DataSource<CreateAttendIntroItem> mDataSource;
    private Context mContext;

    public CreateAttendIntroItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDataSource(DataSource<CreateAttendIntroItem> dataSource) {
        if (this.mDataSource != null)
            this.mDataSource.disposed(this);
        this.mDataSource = dataSource;
        this.mDataSource.attach(this);
    }

    public CreateAttendIntroItemAdapter(Context mContext, DataSource<CreateAttendIntroItem> dataSource) {
        this.mContext = mContext;
        this.mDataSource = dataSource;
        this.mDataSource.attach(this);
    }

    private List<CreateAttendIntroItem> getCreateAttendIntroItemList() {
        return this.mDataSource.getDatas();
    }

    @Override
    public int getCount() {
        return getCreateAttendIntroItemList().size();
    }

    @Override
    public Object getItem(int position) {
        return getCreateAttendIntroItemList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CreateAttendIntroItemAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_create_attend_intro, parent, false);
            viewHolder = new CreateAttendIntroItemAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CreateAttendIntroItemAdapter.ViewHolder) convertView.getTag();
        }
        CreateAttendIntroItem item = getCreateAttendIntroItemList().get(position);
        viewHolder.tv_name.setText(item.getName());
        viewHolder.tv_time.setText(item.getTime());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < item.getType().length; i++) {
            if (item.getType()[i] == 1)
                sb.append(mContext.getString(R.string.attend_type_1) + " ");
            if (item.getType()[i] == 2)
                sb.append(mContext.getString(R.string.attend_type_2) + " ");
        }
        viewHolder.tv_type.setText(sb.toString());
        viewHolder.tv_count.setText("应签到/未签到/已签到 = " + item.getShouldAttendCount() + "/"
                + item.getHaveAttendCount() + "/"
                + item.getNotAttendCount());
        String statusText = "";
        int statusTextColor = R.color.thing_default;
        int statusBgColor = R.color.thing_default_bg;
        if (2 == item.getStatus()) {
            statusText = mContext.getResources().getString(R.string.thing_status_ing);
            statusTextColor = R.color.thing_ing;
            statusBgColor = R.drawable.background_thing_ing;
        }
        if (1 == item.getStatus()) {
            statusText = mContext.getResources().getString(R.string.thing_status_not_begin);
            statusTextColor = R.color.thing_not_begin;
            statusBgColor = R.drawable.background_thing_not_begin;
        }
        if (3 == item.getStatus()) {
            statusText = mContext.getResources().getString(R.string.thing_status_finish);
            statusTextColor = R.color.thing_finish;
            statusBgColor = R.drawable.background_thing_finish;
        }
        viewHolder.tv_status.setText(statusText);
        viewHolder.tv_status.setTextColor(mContext.getResources().getColor(statusTextColor));
        viewHolder.cl_item_create_attend_intro_inner.setBackground(mContext.getDrawable(statusBgColor));
//        viewHolder.cl_item_create_attend_intro_inner.getBackground().mutate().setAlpha(153);
        return convertView;
    }

    @Override
    public void update() {
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.cl_item_create_attend_intro_inner)
        ConstraintLayout cl_item_create_attend_intro_inner;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_type)
        TextView tv_type;
        @BindView(R.id.tv_count)
        TextView tv_count;
        @BindView(R.id.tv_status)
        TextView tv_status;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
