package com.aqinn.actmanagersysandroid.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.utils.CommonUtils;
import com.aqinn.actmanagersysandroid.utils.DateFormatUtils;
import com.aqinn.actmanagersysandroid.view.CustomDatePicker;
import com.aqinn.actmanagersysandroid.view.PickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Aqinn
 * @date 2021/1/16 6:09 PM
 */
public class CreateAttendFragment extends BaseDialogFragment {

    private static final String TAG = "CreateAttendFragment";

    private TextView tv_time;
    private CheckBox cb_self;
    private CheckBox cb_video;
    private Button bt_confirm;

    private Callback callback;

    //时间选择器
    private CustomDatePicker timePicker;


    public CreateAttendFragment(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_attend, container);
        initAllView(root);
        initDialog();
        return root;
    }

    private void initAllView(View root) {
        tv_time = root.findViewById(R.id.tv_time);
        tv_time.setClickable(true);
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show(tv_time.getText().toString());
            }
        });
        cb_self = root.findViewById(R.id.cb_self);
        cb_video = root.findViewById(R.id.cb_video);
        bt_confirm = root.findViewById(R.id.bt_confirm);
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mytest", "setBt_confirm: 点击了");
                callback.onConfirm(tv_time.getText().toString(), CommonUtils.typeArr2dec(
                        new Integer[]{cb_video.isChecked() ? 2 : 0, cb_self.isChecked() ? 1 : 0}
                ));
                dismiss();
            }
        });
        initTimerPicker();
    }

    public interface Callback {
        void onConfirm(String time, Integer type);
    }

    private void initTimerPicker() {
        String beginTime = "1999-12-09 03:00";
        String endTime = DateFormatUtils.long2Str(1893427199000L, true);
        String currentTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        tv_time.setText(currentTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        timePicker = new CustomDatePicker(getContext(), new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_time.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        timePicker.setCancelable(true);
        // 显示时和分
        timePicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        timePicker.setScrollLoop(true);
        // 允许滚动动画
        timePicker.setCanShowAnim(true);
    }

}