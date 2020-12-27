package com.aqinn.actmanagersysandroid.fragment;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aqinn.actmanagersysandroid.ShowManager;
import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.adapter.ActIntroItemAdapter;
import com.aqinn.actmanagersysandroid.data.ApiResult;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;
import com.aqinn.actmanagersysandroid.presenter.MyServiceManager;
import com.aqinn.actmanagersysandroid.presenter.ServiceManager;
import com.aqinn.actmanagersysandroid.qualifiers.ActCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.ActPartDataSource;
import com.aqinn.actmanagersysandroid.service.ActService;
import com.aqinn.actmanagersysandroid.service.AttendService;
import com.aqinn.actmanagersysandroid.service.UserActService;
import com.aqinn.actmanagersysandroid.service.UserAttendService;
import com.aqinn.actmanagersysandroid.service.UserService;
import com.aqinn.actmanagersysandroid.utils.RetrofitUtils;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 活动中心
 *
 * @author Aqinn
 * @date 2020/12/12 11:58 AM
 */
public class ActCenterFragment extends BaseFragment {

    private static final String TAG = "ActCenterFragment";

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;

    @Inject
    @ActCreateDataSource
    public DataSource dsc;
    @Inject
    @ActPartDataSource
    public DataSource dsp;
    @Inject
    public ServiceManager serviceManager;

    private Map<ContentPage, View> mPageMap = new HashMap<>();
    private Map<Integer, ListView> mListViewMap = new HashMap<>();
    private Map<Integer, ActIntroItemAdapter> mAdapterMap = new HashMap<>();
    private ContentPage mDestPage = ContentPage.Item1;
    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return ContentPage.SIZE;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            ContentPage page = ContentPage.getPage(position);
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
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_act_center, null);
        MyApplication.getFragmentComponent().inject(this);
        ButterKnife.bind(this, rootView);
        initTopBar();
        initTabAndPager();
        return rootView;
    }

    /**
     * 初始化顶部导航栏
     */
    private void initTopBar() {
        mTopBar.setTitle("活动中心");
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
                .addItem("创建活动")
                .addItem("加入活动")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                // 创建活动

                                break;
                            case 1:
                                // 加入活动

                                break;
                            default:
                                break;
                        }
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
        mTabSegment.addTab(builder.setText(getString(R.string.act_center_tab_1_title)).build(getContext()));
        mTabSegment.addTab(builder.setText(getString(R.string.act_center_tab_2_title)).build(getContext()));
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
    }

    private View getPageView(ContentPage page) {
        View view = mPageMap.get(page);
        if (view == null) {
            if (page == ContentPage.Item1) {
                view = getListView(1);
            } else if (page == ContentPage.Item2) {
                view = getListView(2);
            }
            mPageMap.put(page, view);
        }
        return view;
    }

    private synchronized ListView getListView(final int flag) {
        if (mListViewMap.containsKey(flag))
            return mListViewMap.get(flag);
        ListView listView = new ListView(getContext());
        ViewGroup.LayoutParams vglp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(vglp);
        listView.setDivider(null);
        listView.setOnItemClickListener(new Avocl(flag));
        ActIntroItemAdapter aiia = new ActIntroItemAdapter(getContext());
        aiia.setDataSource(flag == 1 ? dsc : dsp);
        listView.setAdapter(aiia);
        // TODO 设置 ListView 为空的时候的视图
        mListViewMap.put(flag, listView);
        mAdapterMap.put(flag, aiia);
        return listView;
    }

    // TODO 小弹窗展示活动详情
    private void showActDetail(View v) {
        QMUISkinValueBuilder builder = QMUISkinValueBuilder.acquire();
        QMUIFrameLayout frameLayout = new QMUIFrameLayout(getContext());
        frameLayout.setBackground(
                QMUIResHelper.getAttrDrawable(getContext(), R.attr.qmui_skin_support_popup_bg));
        builder.background(R.attr.qmui_skin_support_popup_bg);
        QMUISkinHelper.setSkinValue(frameLayout, builder);
        frameLayout.setRadius(QMUIDisplayHelper.dp2px(getContext(), 12));
        int padding = QMUIDisplayHelper.dp2px(getContext(), 20);
        frameLayout.setPadding(padding, padding, padding, padding);

        TextView textView = new TextView(getContext());
        textView.setLineSpacing(QMUIDisplayHelper.dp2px(getContext(), 4), 1.0f);
        textView.setPadding(padding, padding, padding, padding);
        textView.setText("这是自定义显示的内容");
        textView.setTextColor(
                QMUIResHelper.getAttrColor(getContext(), R.attr.app_skin_common_title_text_color));

        builder.clear();
        builder.textColor(R.attr.app_skin_common_title_text_color);
        QMUISkinHelper.setSkinValue(textView, builder);
        textView.setGravity(Gravity.CENTER);

        builder.release();

        int size = QMUIDisplayHelper.dp2px(getContext(), 200);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size);
        frameLayout.addView(textView, lp);

        QMUIPopups.fullScreenPopup(getContext())
                .addView(frameLayout)
                .closeBtn(true)
                .skinManager(QMUISkinManager.defaultInstance(getContext()))
                .onBlankClick(new QMUIFullScreenPopup.OnBlankClickListener() {
                    @Override
                    public void onBlankClick(QMUIFullScreenPopup popup) {
                        Toast.makeText(getContext(), "点击到空白区域", Toast.LENGTH_SHORT).show();
                    }
                })
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Toast.makeText(getContext(), "onDismiss", Toast.LENGTH_SHORT).show();
                    }
                })
                .show(v);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private class Avocl implements AdapterView.OnItemClickListener {
        private Integer mFlag = -1;

        private Avocl(int flag) {
            mFlag = flag;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int click_item_position, long id) {
            final ActIntroItem clickAii = (ActIntroItem) mAdapterMap.get(mFlag).getItem(click_item_position);
            QMUIQuickAction qqa = QMUIPopups.quickAction(getContext(),
                    QMUIDisplayHelper.dp2px(getContext(), 56),
                    QMUIDisplayHelper.dp2px(getContext(), 56))
                    .shadow(true)
                    .skinManager(QMUISkinManager.defaultInstance(getContext()))
                    .edgeProtection(QMUIDisplayHelper.dp2px(getContext(), 20));
            qqa.addAction(new QMUIQuickAction.Action().text("查看详情").onClick(
                    new QMUIQuickAction.OnClickListener() {
                        @Override
                        public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                            quickAction.dismiss();
                            ActDetailFragment adf = new ActDetailFragment(mFlag, clickAii, mAdapterMap.get(mFlag));
                            startFragment(adf);
                            Toast.makeText(getContext(), "查看详情成功", Toast.LENGTH_SHORT).show();
                        }
                    }
            ));
            if (mFlag == 1) {
                qqa.addAction(new QMUIQuickAction.Action().text("编辑活动").onClick(
                        new QMUIQuickAction.OnClickListener() {
                            @Override
                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                quickAction.dismiss();
                                ActDetailFragment adf = new ActDetailFragment(mFlag, clickAii, mAdapterMap.get(mFlag), true);
                                startFragment(adf);
                                Toast.makeText(getContext(), "打开编辑活动界面成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                ));
                if (clickAii.getStatus() == 1)
                    qqa.addAction(new QMUIQuickAction.Action().text("开始活动").onClick(
                            new QMUIQuickAction.OnClickListener() {
                                @Override
                                public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                    quickAction.dismiss();
                                    serviceManager.startAct(clickAii.getId());
                                }
                            }
                    ));
                if (clickAii.getStatus() == 2)
                    qqa.addAction(new QMUIQuickAction.Action().text("结束活动").onClick(
                            new QMUIQuickAction.OnClickListener() {
                                @Override
                                public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                    quickAction.dismiss();
                                    serviceManager.stopAct(clickAii.getId());
                                }
                            }
                    ));
            }
            if (mFlag == 2) {
                if (clickAii.getStatus() != 3)
                    qqa.addAction(new QMUIQuickAction.Action().text("退出活动").onClick(
                            new QMUIQuickAction.OnClickListener() {
                                @Override
                                public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                    quickAction.dismiss();
                                    serviceManager.quitAct(clickAii.getId());
                                }
                            }
                    ));
            }
            qqa.addAction(new QMUIQuickAction.Action().text("查看签到").onClick(
                    new QMUIQuickAction.OnClickListener() {
                        @Override
                        public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                            quickAction.dismiss();
                            Toast.makeText(getContext(), "查看签到成功", Toast.LENGTH_SHORT).show();
                        }
                    }
            ));
            qqa.show(view);
        }
    }

    public enum ContentPage {
        Item1(0),
        Item2(1);
        public static final int SIZE = 2;
        private final int position;

        ContentPage(int pos) {
            position = pos;
        }

        public static ContentPage getPage(int position) {
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

    @Override
    public void onResume() {
        super.onResume();
        // 返回 Fragment 的时候，需要滚动一下，不然功能菜单无法出现。可能是需要滚动触发某些组件的状态更新？原因待检查。
        for (int i = 1; i <= 2; i++) {
            if (!mAdapterMap.containsKey(i) || !mListViewMap.containsKey(i))
                continue;
            if (mAdapterMap.get(i).isEmpty())
                continue;
            if (mListViewMap.get(i).getChildAt(0).getTop() == 0)
                mListViewMap.get(i).smoothScrollBy(1, 1);
            else
                mListViewMap.get(i).smoothScrollBy(-1, 1);
        }
    }

}
