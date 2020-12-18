package com.aqinn.actmanagersysandroid.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.adapter.CreateAttendIntroItemAdapter;
import com.aqinn.actmanagersysandroid.adapter.ParticipateAttendIntroItemAdapter;
import com.aqinn.actmanagersysandroid.components.DaggerFragmentComponent;
import com.aqinn.actmanagersysandroid.datafortest.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.datafortest.DataSource;
import com.aqinn.actmanagersysandroid.datafortest.DataSourceCreateAttendIntroItemTest;
import com.aqinn.actmanagersysandroid.datafortest.DataSourceParticipateAttendIntroItemTest;
import com.aqinn.actmanagersysandroid.datafortest.ParticipateAttendIntroItem;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendPartDataSource;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import java.util.ArrayList;
import java.util.HashMap;
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
    private Map<AttendCenterFragment.ContentPage, View> mPageMap = new HashMap<>();
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
        DaggerFragmentComponent.builder().dataSourceComponent(((MyApplication) getActivity().getApplication()).getDataSourceComponent()).build().inject(this);
        ButterKnife.bind(this, rootView);

        initTopBar();
        initTabAndPager();

        return rootView;
    }

    private void initTopBar() {
//        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popBackStack();
//            }
//        });
        mTopBar.setTitle("签到中心");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomSheetList();
                    }
                });
    }

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
        ListView listView = new ListView(getContext());
        ViewGroup.LayoutParams vglp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(vglp);
        listView.setDivider(null);
//                listView.setDividerHeight(-35);
        if (flag == 1) {
            CreateAttendIntroItemAdapter caiia = new CreateAttendIntroItemAdapter(getContext());
            caiia.setDataSource(dsc);
            listView.setAdapter(caiia);
        } else {
            ParticipateAttendIntroItemAdapter paiia = new ParticipateAttendIntroItemAdapter(getContext());
            paiia.setDataSource(dsp);
            listView.setAdapter(paiia);
        }
        // TODO 设置 ListView 为空的时候的视图
        return listView;
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

