package com.aqinn.actmanagersysandroid.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.activity.MainActivity;
import com.aqinn.actmanagersysandroid.components.DaggerFragmentComponent;
import com.aqinn.actmanagersysandroid.data.ApiResult;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Refreshable;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.ParticipateAttendIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.UserDesc;
import com.aqinn.actmanagersysandroid.qualifiers.ActCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.ActPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.UserDescDataSource;
import com.aqinn.actmanagersysandroid.service.ActService;
import com.aqinn.actmanagersysandroid.service.AttendService;
import com.aqinn.actmanagersysandroid.service.ShowItemService;
import com.aqinn.actmanagersysandroid.service.UserActService;
import com.aqinn.actmanagersysandroid.service.UserAttendService;
import com.aqinn.actmanagersysandroid.service.UserService;
import com.aqinn.actmanagersysandroid.utils.CommonUtil;
import com.aqinn.actmanagersysandroid.utils.LoadDialogUtil;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.QMUIFragmentPagerAdapter;
import com.qmuiteam.qmui.widget.QMUIViewPager;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 主界面 - 作为 Fragment 容器
 * @author Aqinn
 * @date 2020/12/11 7:41 PM
 */
public class MainFragment extends BaseFragment {

    private static final String TAG = "MainFragment";

    @BindView(R.id.pager)
    QMUIViewPager mViewPager;
    @BindView(R.id.tabs)
    QMUITabSegment mTabSegment;

    @Inject
    public UserService userService;
    @Inject
    public ActService actService;
    @Inject
    public UserActService userActService;
    @Inject
    public AttendService attendService;
    @Inject
    public UserAttendService userAttendService;
    @Inject
    @ActCreateDataSource
    public DataSource dsActC;
    @Inject
    @ActPartDataSource
    public DataSource dsActP;
    @Inject
    @AttendCreateDataSource
    public DataSource dsAttC;
    @Inject
    @AttendPartDataSource
    public DataSource dsAttP;
    @Inject
    @UserDescDataSource
    public DataSource dsUsers;
    @Inject
    public ShowItemService showItemService;

    @Override
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, layout);
        DaggerFragmentComponent.builder().dataSourceComponent(MyApplication.getDataSourceComponent())
                .retrofitServiceComponent(MyApplication.getRetrofitServiceComponent()).build().inject(this);
        initPagers();
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            // 检查有没有登录信息
            MyApplication.nowUserId = CommonUtil.getNowUserIdFromSP(getContext());
            if (MyApplication.nowUserId == -1L) {
                emitter.onError(new Throwable("没有任何已登录记录"));
                return;
            }
            // 初始化数据 请求网络数据
            Observable<ApiResult<UserDesc>> observableUserDesc = showItemService.getUserDesc(MyApplication.nowUserId);
            Disposable d1 = observableUserDesc.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userDescApiResult -> {
                        MyApplication.nowUserAccount = userDescApiResult.data.getAccount();
                        ((Refreshable) dsUsers).refresh(userDescApiResult.data);
                    }, throwable -> {
                        Log.d(TAG, "MyApplication.onCreate() 加载用户数据出错: " + throwable.getMessage());
                    });
            Observable<ApiResult<List<ActIntroItem>>> observableActIntroItem
                    = showItemService.getActIntroItem(MyApplication.nowUserId);
            Disposable d2 = observableActIntroItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listApiResult -> {
                        ((Refreshable) dsActC).refresh(listApiResult.data);
                        ((Refreshable) dsActP).refresh(listApiResult.data);
                    }, throwable -> {
                        Log.d(TAG, "MyApplication.onCreate() 加载活动数据出错: " + throwable.getMessage());
                    });
            Observable<ApiResult<List<CreateAttendIntroItem>>> observableCreateAttendIntroItem
                    = showItemService.getCreateAttendIntroItem(MyApplication.nowUserId);
            Disposable d3 = observableCreateAttendIntroItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listApiResult -> {
                        ((Refreshable) dsAttC).refresh(listApiResult.data);
                    }, throwable -> {
                        Log.d(TAG, "MyApplication.onCreate() 加载我创建的签到数据出错: " + throwable.getMessage());
                    });
            Observable<ApiResult<List<ParticipateAttendIntroItem>>> observableParticipateAttendIntroItem
                    = showItemService.getParticipateAttendIntroItem(MyApplication.nowUserId);
            Disposable d4 = observableParticipateAttendIntroItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listApiResult -> {
                        ((Refreshable) dsAttP).refresh(listApiResult.data);
                    }, throwable -> {
                        Log.d(TAG, "MyApplication.onCreate() 加载我参与的签到数据出错: " + throwable.getMessage());
                    });
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    private Disposable mDisposable;
                    private Dialog mDialog;

                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        mDisposable = d;
                        mDialog = LoadDialogUtil.createLoadingDialog(getContext(), "正在加载最新数据...");
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull String s) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        mDisposable.dispose();
                        LoadDialogUtil.closeDialog(mDialog);
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        LoadDialogUtil.closeDialog(mDialog);
                        Toast.makeText(getContext(), "加载完毕", Toast.LENGTH_SHORT).show();
                    }
                });
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
