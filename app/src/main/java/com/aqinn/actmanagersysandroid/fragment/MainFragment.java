package com.aqinn.actmanagersysandroid.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.aqinn.actmanagersysandroid.R;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.QMUIFragmentPagerAdapter;
import com.qmuiteam.qmui.widget.QMUIViewPager;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主界面 - 作为 Fragment 容器
 * @author Aqinn
 * @date 2020/12/11 7:41 PM
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.pager)
    QMUIViewPager mViewPager;
    @BindView(R.id.tabs)
    QMUITabSegment mTabSegment;

    @Override
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, layout);
        initPagers();
        return layout;
    }

    private void initPagers() {
        QMUIFragmentPagerAdapter pagerAdapter = new QMUIFragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public QMUIFragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new ActCenterFragment();
                    case 1:
                        return new AttendCenterFragment();
                    case 2:
                        return new PersonalCenterFragment();
                    case 3:
                    default:
                        return new ErrorFragment(null, null);
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "活动";
                    case 1:
                        return "签到";
                    case 2:
                        return "个人";
                    case 3:
                    default:
                        return "ErrorFragment";
                }
            }
        };
        mViewPager.setAdapter(pagerAdapter);
        mTabSegment.setupWithViewPager(mViewPager);
    }

    @Override
    protected boolean canDragBack() {
        return mViewPager.getCurrentItem() == 0;
    }

}
