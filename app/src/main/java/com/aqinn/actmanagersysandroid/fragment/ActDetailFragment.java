package com.aqinn.actmanagersysandroid.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.adapter.ActIntroItemAdapter;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;
import com.aqinn.actmanagersysandroid.presenter.ServiceManager;
import com.aqinn.actmanagersysandroid.utils.DateFormatUtils;
import com.aqinn.actmanagersysandroid.view.CustomDatePicker;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Aqinn
 * @date 2020/12/14 8:25 AM
 */
public class ActDetailFragment extends BaseFragment {

    private static final String TAG = "ActDetailFragment";

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.tv_creator)
    TextView tvCreator;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.et_loc)
    EditText etLoc;
    @BindView(R.id.et_intro)
    EditText etIntro;

    @Inject
    public ServiceManager serviceManager;

    private Integer mFlag = -1;
    private boolean editEnable = false;
    private ActIntroItem mAii = null;
    private QMUIAlphaImageButton qaib;
    private final int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private CustomDatePicker timePicker;

    public ActDetailFragment(int flag, ActIntroItem aii, ActIntroItemAdapter aiia) {
        mFlag = flag;
        this.mAii = aii;
    }

    public ActDetailFragment(int flag, ActIntroItem aii, ActIntroItemAdapter aiia, boolean editEnable) {
        mFlag = flag;
        this.mAii = aii;
        this.editEnable = editEnable;
    }

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_act_detail, null);
        MyApplication.getApplicationComponent().inject(this);
        ButterKnife.bind(this, rootView);
        initTopBar();
        initData();
        initTimerPicker();
        return rootView;
    }

    /**
     * 初始化页面数据
     */
    private void initData() {
        tvCreator.setText(mAii.getCreator());
        etName.setText(mAii.getName());
        tv_time.setText(mAii.getTime());
        etLoc.setText(mAii.getLocation());
        etIntro.setText(mAii.getIntro());
        editModeOff();
        if (editEnable)
            editModeOn();
    }

    /**
     * 初始化顶部导航栏
     */
    private void initTopBar() {
        mTopBar.setTitle("活动详情");
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.isEnabled())
                    showEditNotFinishDialog(new ShowEditNotFinishCallable() {
                        @Override
                        public void quit(boolean isQuit) {
                            if (isQuit) {
                                editModeOff();
                                popBackStack();
                            }
                        }
                    });
                else
                    popBackStack();
            }
        });
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomSheetList();
                    }
                });
        qaib = mTopBar.addRightImageButton(R.mipmap.edit_finish, R.id.bt_edit_finish);
        qaib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActIntroItem newAii = new ActIntroItem(mAii.getId(), mAii.getOwnerId(), mAii.getActId(), mAii.getCode(), mAii.getPwd(), mAii.getCreator(),
                        etName.getText().toString(), tv_time.getText().toString(),
                        etLoc.getText().toString(), etIntro.getText().toString(),
                        mAii.getStatus());
                serviceManager.editAct(newAii, new ServiceManager.EditActCallback() {
                    @Override
                    public void onFinish() {
                        editModeOff();
                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(getContext(), "活动信息填写不符合规范", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getContext(), "修改出错, 原因未知", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        qaib.setVisibility(View.INVISIBLE);
    }

    /**
     * 弹出底部菜单栏
     */
    private void showBottomSheetList() {
        final QMUIBottomSheet.BottomListSheetBuilder sh = new QMUIBottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("查看签到");
        if (mFlag == 1) {
            sh.addItem("编辑活动");
            if (mAii.getStatus() == 1)
                sh.addItem("开始活动");
            if (mAii.getStatus() == 2)
                sh.addItem("结束活动");
            sh.addItem("创建签到");
        }
        if (mFlag == 2)
            sh.addItem("退出活动");
        sh.setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
            @Override
            public void onClick(QMUIBottomSheet dialog, final View itemView, int position, String tag) {
                dialog.dismiss();
                switch (position) {
                    case 0:
                        // 查看签到
                        Toast.makeText(getContext(), "查看签到", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // 编辑活动
                        if (mFlag == 1) {
                            editModeOn();
                            Toast.makeText(getContext(), "点击了编辑活动按钮", Toast.LENGTH_SHORT).show();
                        }
                        // 退出活动
                        else if (mFlag == 2) {
                            serviceManager.quitAct(mAii.getId());
                        }
                        break;
                    case 2:
                        // 开始活动
                        if (mAii.getStatus() == 1) {
                            serviceManager.startAct(mAii.getId());
                        }
                        // 结束活动
                        else if (mAii.getStatus() == 2) {
                            showActStopCheck(new ActStopCheckCallable() {
                                @Override
                                public void stopOrNot(boolean isStop) {
                                    serviceManager.stopAct(mAii.getId());
                                }
                            });
                            Toast.makeText(getContext(), "点击了结束活动", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        // 创建签到
                        Toast.makeText(getContext(), "创建签到", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }).build().show();
    }

    /**
     * 更换 EditText 的可编辑状态
     *
     * @param status 0: 全为 false
     *               1: 全为 true
     *               2: 自动切换为另一个状态
     */
    private void etEnabledChanged(int status) {
        switch (status) {
            case 0:
                etName.setEnabled(false);
                tv_time.setEnabled(false);
                etLoc.setEnabled(false);
                etIntro.setEnabled(false);
                break;
            case 1:
                etName.setEnabled(true);
                tv_time.setEnabled(true);
                etLoc.setEnabled(true);
                etIntro.setEnabled(true);
                break;
            case 2:
                etName.setEnabled(!etName.isEnabled());
                tv_time.setEnabled(!tv_time.isEnabled());
                etLoc.setEnabled(!etLoc.isEnabled());
                etIntro.setEnabled(!etIntro.isEnabled());
                break;
            default:
                break;
        }
    }

    /**
     * 在点击结束活动按钮时弹出提示框
     *
     * @param ascc
     */
    private void showActStopCheck(final ActStopCheckCallable ascc) {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("提示")
                .setMessage("确定要结束活动吗？")
                .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        ascc.stopOrNot(false);
                    }
                })
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        ascc.stopOrNot(true);
                        Toast.makeText(getActivity(), "活动已结束", Toast.LENGTH_SHORT).show();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    /**
     * 当在编辑状态时退出 Fragment 会弹出提示框
     *
     * @param senfc
     */
    private void showEditNotFinishDialog(final ShowEditNotFinishCallable senfc) {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("提示")
                .setMessage("修改还没保存，确定要退出吗？")
                .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                .addAction("继续修改", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        senfc.quit(false);
                    }
                })
                .addAction(0, "不保存并退出", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        senfc.quit(true);
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    /**
     * 开启编辑模式
     */
    private void editModeOn() {
        etEnabledChanged(1);
        mTopBar.findViewById(R.id.topbar_right_change_button).setClickable(false);
        qaib.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭编辑模式
     */
    private void editModeOff() {
        qaib.setVisibility(View.INVISIBLE);
        etEnabledChanged(0);
        mTopBar.findViewById(R.id.topbar_right_change_button).setClickable(true);
    }

    @OnClick(R.id.tv_time)
    public void setTv_time(View view) {
        timePicker.show(tv_time.getText().toString());
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

    /**
     * 回调: 当点击结束活动
     */
    interface ActStopCheckCallable {
        void stopOrNot(boolean isStop);
    }

    /**
     * 回调: 当保存状态下退出 Fragment
     */
    interface ShowEditNotFinishCallable {
        void quit(boolean isQuit);
    }

}
