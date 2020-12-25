package com.aqinn.actmanagersysandroid.fragment;

import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.adapter.UserDescRecyclerViewAdapter;
import com.aqinn.actmanagersysandroid.components.DaggerFragmentComponent;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.entity.show.UserDesc;
import com.aqinn.actmanagersysandroid.qualifiers.UserDescDataSource;
import com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人中心
 *
 * @author Aqinn
 * @date 2020/12/12 9:01 PM
 */
public class PersonalCenterFragment extends BaseFragment {

    private static final String TAG = "PersonalCenterFragment";

    @Inject
    @UserDescDataSource
    public DataSource dsu;

    UserDescRecyclerViewAdapter userDescRecyclerViewAdapter;
    LinearLayoutManager mPagerLayoutManager;


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.collapsing_topbar_layout)
    QMUICollapsingTopBarLayout mCollapsingTopBarLayout;
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_personal, null);
        DaggerFragmentComponent.builder().dataSourceComponent(((MyApplication) getActivity().getApplication()).getDataSourceComponent()).build().inject(this);
        ButterKnife.bind(this, rootView);
        initTopBar();
        mPagerLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mPagerLayoutManager);
        UserDesc userDesc = null;
        userDesc = initUserDesc();
        userDescRecyclerViewAdapter = new UserDescRecyclerViewAdapter(userDesc);
        mRecyclerView.setAdapter(userDescRecyclerViewAdapter);
//        mCollapsingTopBarLayout.setScrimUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                Log.i(TAG, "scrim: " + animation.getAnimatedValue());
//            }
//        });
//
//        mCollapsingTopBarLayout.addOnOffsetUpdateListener(new QMUICollapsingTopBarLayout.OnOffsetUpdateListener() {
//            @Override
//            public void onOffsetChanged(QMUICollapsingTopBarLayout layout, int offset, float expandFraction) {
//                Log.i(TAG, "offset = " + offset + "; expandFraction = " + expandFraction);
//            }
//        });

        return rootView;
    }

    private UserDesc initUserDesc() {
        if (dsu.getDatas().isEmpty())
            return new UserDesc("没有登录", "没有登录", 1, "没有登录", "没有登录");
        return ((UserDesc) dsu.getDatas().get(0));
    }

    @Override
    protected boolean translucentFull() {
        return true;
    }

    private void initTopBar() {
        mCollapsingTopBarLayout.setTitle("个人中心");
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
}

