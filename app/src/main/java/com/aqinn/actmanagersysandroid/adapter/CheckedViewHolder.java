package com.aqinn.actmanagersysandroid.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aqinn.actmanagersysandroid.R;

/**
 * @author Aqinn
 * @date 2021/1/16 12:56 AM
 */
public class CheckedViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_item;

    public CheckedViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_item = itemView.findViewById(R.id.tv_item);
    }

}
