package com.aqinn.actmanagersysandroid.fragment;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aqinn.actmanagersysandroid.ShowManager;
import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.adapter.CreateAttendIntroItemAdapter;
import com.aqinn.actmanagersysandroid.adapter.ParticipateAttendIntroItemAdapter;
import com.aqinn.actmanagersysandroid.entity.show.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.entity.show.ParticipateAttendIntroItem;
import com.aqinn.actmanagersysandroid.presenter.ServiceManager;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendPartDataSource;
import com.aqinn.actmanagersysandroid.service.ActService;
import com.aqinn.actmanagersysandroid.service.AttendService;
import com.aqinn.actmanagersysandroid.service.UserActService;
import com.aqinn.actmanagersysandroid.service.UserAttendService;
import com.aqinn.actmanagersysandroid.service.UserService;
import com.aqinn.actmanagersysandroid.utils.CommonUtil;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 签到中心
 *
 * @author Aqinn
 * @date 2020/12/12 8:37 PM
 */
public class AttendCenterFragment extends BaseFragment {

    private static final String TAG = "AttendCenterFragment";

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;

    @Inject
    @AttendCreateDataSource
    public DataSource dsc;
    @Inject
    @AttendPartDataSource
    public DataSource dsp;
    @Inject
    public ServiceManager serviceManager;

    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private Map<AttendCenterFragment.ContentPage, View> mPageMap = new HashMap<>();
    private Map<Integer, ListView> mListViewMap = new HashMap<>();
    private CreateAttendIntroItemAdapter caiia = null;
    private ParticipateAttendIntroItemAdapter paiia = null;
    private AttendCenterFragment.ContentPage mDestPage = AttendCenterFragment.ContentPage.Item1;
    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return AttendCenterFragment.ContentPage.SIZE;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            AttendCenterFragment.ContentPage page = AttendCenterFragment.ContentPage.getPage(position);
            View view = getPageView(page);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, params);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    };

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_attend_center, null);
        MyApplication.getApplicationComponent().inject(this);
        Log.d("singleTest", "onCreateView: dsc" + dsc);
        ButterKnife.bind(this, rootView);

        initTopBar();
        initTabAndPager();

        return rootView;
    }

    /**
     * 初始化顶部导航栏
     */
    private void initTopBar() {
        mTopBar.setTitle("签到中心");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomSheetList();
                    }
                });
    }

    /**
     * 弹出底部功能菜单
     */
    private void showBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("xixi")
                .addItem("xixi")
                .addItem("xixi")
                .addItem("xixi")
                .addItem("xixi")
                .addItem("xixi")
                .addItem("xixi")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();
    }

    /**
     * 初始化页面
     */
    private void initTabAndPager() {
        mContentViewPager.setAdapter(mPagerAdapter);
        mContentViewPager.setCurrentItem(mDestPage.getPosition(), false);
        QMUITabBuilder builder = mTabSegment.tabBuilder();
        mTabSegment.addTab(builder.setText(getString(R.string.attend_center_tab_1_title)).build(getContext()));
        mTabSegment.addTab(builder.setText(getString(R.string.attend_center_tab_2_title)).build(getContext()));
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {

            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
            }

            @Override
            public void onDoubleTap(int index) {
                mTabSegment.clearSignCountView(index);
            }
        });
    }

    private View getPageView(AttendCenterFragment.ContentPage page) {
        View view = mPageMap.get(page);
        if (view == null) {
            if (page == AttendCenterFragment.ContentPage.Item1) {
                view = getListView(1);
            } else if (page == AttendCenterFragment.ContentPage.Item2) {
                view = getListView(2);
            }
            mPageMap.put(page, view);
        }
        return view;
    }

    private ListView getListView(int flag) {
        if (mListViewMap.containsKey(flag))
            return mListViewMap.get(flag);
        ListView listView = new ListView(getContext());
        ViewGroup.LayoutParams vglp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(vglp);
        listView.setDivider(null);
//                listView.setDividerHeight(-35);
        listView.setOnItemClickListener(new AttendCenterFragment.Avocl(flag));
        if (flag == 1) {
            CreateAttendIntroItemAdapter caiia = new CreateAttendIntroItemAdapter(getContext(), dsc);
            listView.setAdapter(caiia);
            this.caiia = caiia;
        } else {
            ParticipateAttendIntroItemAdapter paiia = new ParticipateAttendIntroItemAdapter(getContext(), dsp);
            listView.setAdapter(paiia);
            this.paiia = paiia;
        }
        mListViewMap.put(flag, listView);
        // TODO 设置 ListView 为空的时候的视图
        return listView;
    }

    private class Avocl implements AdapterView.OnItemClickListener {
        private Integer mFlag = -1;

        private Avocl(int flag) {
            mFlag = flag;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, final View view, final int click_item_position, long id) {
            boolean isShow = false;
            CreateAttendIntroItem caiiTemp = null;
            if (mFlag == 1)
                caiiTemp = (CreateAttendIntroItem) caiia.getItem(click_item_position);
            final CreateAttendIntroItem clickCaii = caiiTemp;
            ParticipateAttendIntroItem paiiTemp = null;
            if (mFlag == 2)
                paiiTemp = (ParticipateAttendIntroItem) paiia.getItem(click_item_position);
            final ParticipateAttendIntroItem clickPaii = paiiTemp;
            QMUIQuickAction qqa = QMUIPopups.quickAction(getContext(),
                    QMUIDisplayHelper.dp2px(getContext(), 56),
                    QMUIDisplayHelper.dp2px(getContext(), 56))
                    .shadow(true)
                    .skinManager(QMUISkinManager.defaultInstance(getContext()))
                    .edgeProtection(QMUIDisplayHelper.dp2px(getContext(), 20));
            if (mFlag == 1) {
                isShow = true;
                qqa.addAction(new QMUIQuickAction.Action().text("编辑时间").onClick(
                        new QMUIQuickAction.OnClickListener() {
                            @Override
                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                quickAction.dismiss();
                                showEditAttendTimePopup(view, clickCaii.getId());
                                Toast.makeText(getContext(), "点击了编辑签到时间按钮", Toast.LENGTH_SHORT).show();
                            }
                        }
                ));
                qqa.addAction(new QMUIQuickAction.Action().text("编辑方式").onClick(
                        new QMUIQuickAction.OnClickListener() {
                            @Override
                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                quickAction.dismiss();
                                showEditAttendTimePopup(clickCaii);
                                Toast.makeText(getContext(), "点击了编辑签到方式按钮", Toast.LENGTH_SHORT).show();
                            }
                        }
                ));
                if (clickCaii.getStatus() == 1)
                    qqa.addAction(new QMUIQuickAction.Action().text("开启签到").onClick(
                            new QMUIQuickAction.OnClickListener() {
                                @Override
                                public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                    quickAction.dismiss();
                                    serviceManager.startAttend(clickCaii.getId());
                                }
                            }
                    ));
                if (clickCaii.getStatus() == 2)
                    qqa.addAction(new QMUIQuickAction.Action().text("停止签到").onClick(
                            new QMUIQuickAction.OnClickListener() {
                                @Override
                                public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                    quickAction.dismiss();
                                    serviceManager.stopAttend(clickCaii.getId());
                                }
                            }
                    ));
                if (clickCaii.getStatus() == 1) {
                    boolean flag = false;
                    Integer type[] = CommonUtil.dec2typeArr(clickCaii.getType());
                    for (int i = 0; i < type.length; i++) {
                        if (type[i] == 1) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag)
                        qqa.addAction(new QMUIQuickAction.Action().text("视频签到").onClick(
                                new QMUIQuickAction.OnClickListener() {
                                    @Override
                                    public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                        quickAction.dismiss();
                                        Toast.makeText(getContext(), "点击了视频签到按钮", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ));
                }
            }
            if (mFlag == 2) {
                if (clickPaii.getuStatus() == 2) {
                    boolean flag = false;
                    Integer type[] = CommonUtil.dec2typeArr(clickPaii.getType());
                    for (int i = 0; i < type.length; i++) {
                        if (type[i] == 2) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        isShow = true;
                        qqa.addAction(new QMUIQuickAction.Action().text("自助签到").onClick(
                                new QMUIQuickAction.OnClickListener() {
                                    @Override
                                    public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                        quickAction.dismiss();
                                        boolean success = false;
                                        if (success)
                                            Toast.makeText(getContext(), "自助签到成功", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(getContext(), "自助签到失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ));
                    }
                }
            }
            if (isShow)
                qqa.show(view);
        }
    }

    /**
     * 弹窗编辑签到时间
     *
     * @param v
     */
    private void showEditAttendTimePopup(View v, final Long id) {
        // 这里必须用 QMUI 的布局作为父布局，不然不能调节提示框的大小
        QMUISkinValueBuilder builder = QMUISkinValueBuilder.acquire();
        QMUIFrameLayout frameLayout = new QMUIFrameLayout(getContext());
        frameLayout.setBackground(
                QMUIResHelper.getAttrDrawable(getContext(), R.attr.qmui_skin_support_popup_bg));
        builder.background(R.attr.qmui_skin_support_popup_bg);
        QMUISkinHelper.setSkinValue(frameLayout, builder);
        frameLayout.setRadius(QMUIDisplayHelper.dp2px(getContext(), 12));
        final int padding = QMUIDisplayHelper.dp2px(getContext(), 20);
        frameLayout.setPadding(padding, padding, padding, padding);
        builder.clear();
        builder.textColor(R.attr.app_skin_common_title_text_color);
        builder.release();
        int height = QMUIDisplayHelper.dp2px(getContext(), 200);
        int width = QMUIDisplayHelper.dp2px(getContext(), 135);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(height, width);

        final View editView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_create_attend_detail, null);
        editView.setBackground(QMUIResHelper.getAttrDrawable(getContext(), R.attr.qmui_skin_support_popup_bg));
        CreateAttendIntroItem caii = getCreateAttendIntroById(id);
        EditText et_time = (EditText) editView.findViewById(R.id.et_time);
        et_time.setText(caii.getTime());
        frameLayout.addView(editView, lp);
        // 这个弹框的 closeBtn 用来当做确认按钮 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // closeIcon 已更换成 一个勾的图案
        QMUIPopups.fullScreenPopup(getContext())
                .addView(frameLayout)
                .closeBtn(true)
                .closeIcon(getContext().getDrawable(R.drawable.icon_popup_yes))
                .onBlankClick(new QMUIFullScreenPopup.OnBlankClickListener() {
                    @Override
                    public void onBlankClick(QMUIFullScreenPopup popup) {
                        popup.onDismiss(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                Toast.makeText(getContext(), "取消弹框", Toast.LENGTH_SHORT).show();
                            }
                        });
                        popup.dismiss();
                    }
                })
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        serviceManager.editAttendTime(id, ((EditText) editView.findViewById(R.id.et_time)).getText().toString());
                    }
                })
                .show(v);
    }

    /**
     * 弹窗编辑签到方式
     */
    private void showEditAttendTimePopup(final CreateAttendIntroItem caii) {
        Integer type[] = CommonUtil.dec2typeArr(caii.getType());
        int[] caiiType = new int[type.length];
        for (int i = 0; i < type.length; i++) {
            caiiType[i] = type[i] - 1;
        }
//        final String[] items = new String[]{"视频签到", "自助签到", "视频弱签到(暂时没用)", "自助弱签到(暂时没用)"};
        final String[] items = new String[]{"视频签到", "自助签到"};
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(getActivity())
                .setCheckedItems(caiiType)
                .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.addAction("取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction("确认", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                String result = "你选择了 ";
                Integer[] caiiType = new Integer[builder.getCheckedItemIndexes().length];
                for (int i = 0; i < builder.getCheckedItemIndexes().length; i++) {
                    result += "" + builder.getCheckedItemIndexes()[i] + "; ";
                    Log.d(TAG, "onClick: " + builder.getCheckedItemIndexes()[i]);
                    caiiType[i] = builder.getCheckedItemIndexes()[i] + 1;
                }
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                serviceManager.editAttendType(caii.getId(), CommonUtil.typeArr2dec(caiiType));
                dialog.dismiss();
            }
        });
        builder.create(mCurrentDialogStyle).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 返回 Fragment 的时候，需要滚动一下，不然功能菜单无法出现。可能是需要滚动触发某些组件的状态更新？原因待检查。
        if (caiia != null && mListViewMap.containsKey(1)) {
            if (mListViewMap.get(1).getChildAt(0).getTop() == 0)
                mListViewMap.get(1).smoothScrollBy(1, 1);
            else
                mListViewMap.get(1).smoothScrollBy(-1, 1);
        }
        if (paiia != null && mListViewMap.containsKey(2)) {
            if (mListViewMap.get(2).getChildAt(0).getTop() == 0)
                mListViewMap.get(2).smoothScrollBy(1, 1);
            else
                mListViewMap.get(2).smoothScrollBy(-1, 1);
        }
    }

    /**
     * 根据 id 获取创建的签到
     *
     * @param id
     * @return
     */
    public CreateAttendIntroItem getCreateAttendIntroById(Long id) {
        for (CreateAttendIntroItem caii : (List<CreateAttendIntroItem>) dsc.getDatas()) {
            if (caii.getId().equals(id)) {
                return caii;
            }
        }
        return null;
    }

    public enum ContentPage {
        Item1(0),
        Item2(1);
        public static final int SIZE = 2;
        private final int position;

        ContentPage(int pos) {
            position = pos;
        }

        public static AttendCenterFragment.ContentPage getPage(int position) {
            switch (position) {
                case 0:
                    return Item1;
                case 1:
                    return Item2;
                default:
                    return Item1;
            }
        }

        public int getPosition() {
            return position;
        }

    }

}

