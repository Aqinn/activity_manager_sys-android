package com.aqinn.actmanagersysandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aqinn.actmanagersysandroid.R;

import java.util.List;

/**
 * @author Aqinn
 * @date 2021/1/16 12:52 AM
 */
public class CheckedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mDatas;

    public CheckedAdapter(Context context, List<String> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_check_recycler_view,parent, false);
        return new CheckedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CheckedViewHolder myHolder = (CheckedViewHolder) holder;
        myHolder.tv_item.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addChecked(String info) {
        mDatas.add(info);
        notifyDataSetChanged();
    }

}
