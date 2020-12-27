package com.aqinn.actmanagersysandroid.presenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.ShowManager;
import com.aqinn.actmanagersysandroid.activity.LoginActivity;
import com.aqinn.actmanagersysandroid.activity.MainActivity;
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
import com.google.gson.internal.LinkedTreeMap;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Aqinn
 * @date 2020/12/27 2:04 PM
 */
public class MyServiceManager implements ServiceManager {

    private static final String TAG = "MyServiceManager";

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
    public ShowItemService showItemService;
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
    public DataSource dsUserDesc;
    @Inject
    public ShowManager showManager;

    private Context mContext;

    public MyServiceManager() {
        MyApplication.getServiceManagerInjectComponent().inject(this);
        Log.d(TAG, "MyServiceManager: ShowManager => " + showManager);
        this.mContext = MyApplication.getContext();
    }

    @Override
    public void checkData(Context context) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            // 检查有没有登录信息
            if (CommonUtil.getNowUserIdFromSP(mContext).equals(CommonUtil.ERR_USER_ID)) {
                emitter.onError(new Throwable("没有任何已登录记录"));
                return;
            }
            // 初始化数据 请求网络数据
            Observable<ApiResult<UserDesc>> observableUserDesc = showItemService.getUserDesc(CommonUtil.getNowUserIdFromSP(mContext));
            Disposable d1 = observableUserDesc.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userDescApiResult -> {
                        CommonUtil.setNowUsernameToSP(mContext, userDescApiResult.data.getAccount());
                        ((Refreshable) dsUserDesc).refresh(userDescApiResult.data);
                        emitter.onNext("成功加载用户数据");
                    }, throwable -> {
                        Log.d(TAG, "MyApplication.onCreate() 加载用户数据出错: " + throwable.getMessage());
                        emitter.onNext("加载用户数据出错");
                    });
            Observable<ApiResult<List<ActIntroItem>>> observableActIntroItem
                    = showItemService.getActIntroItem(CommonUtil.getNowUserIdFromSP(mContext));
            Disposable d2 = observableActIntroItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listApiResult -> {
                        ((Refreshable) dsActC).refresh(listApiResult.data);
                        ((Refreshable) dsActP).refresh(listApiResult.data);
                        emitter.onNext("成功加载活动数据");
                    }, throwable -> {
                        Log.d(TAG, "MyApplication.onCreate() 加载活动数据出错: " + throwable.getMessage());
                        emitter.onNext("加载活动数据出错");
                    });
            Observable<ApiResult<List<CreateAttendIntroItem>>> observableCreateAttendIntroItem
                    = showItemService.getCreateAttendIntroItem(CommonUtil.getNowUserIdFromSP(mContext));
            Disposable d3 = observableCreateAttendIntroItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listApiResult -> {
                        ((Refreshable) dsAttC).refresh(listApiResult.data);
                        emitter.onNext("成功加载我创建的签到数据");
                    }, throwable -> {
                        Log.d(TAG, "MyApplication.onCreate() 加载我创建的签到数据出错: " + throwable.getMessage());
                        emitter.onNext("加载我创建的签到数据出错");
                    });
            Observable<ApiResult<List<ParticipateAttendIntroItem>>> observableParticipateAttendIntroItem
                    = showItemService.getParticipateAttendIntroItem(CommonUtil.getNowUserIdFromSP(mContext));
            Disposable d4 = observableParticipateAttendIntroItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listApiResult -> {
                        ((Refreshable) dsAttP).refresh(listApiResult.data);
                        emitter.onNext("成功加载我参与的签到数据");
                    }, throwable -> {
                        Log.d(TAG, "MyApplication.onCreate() 加载我参与的签到数据出错: " + throwable.getMessage());
                        emitter.onNext("加载我参与的签到数据出错");
                    });
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    private Disposable mDisposable;
                    private Dialog mDialog;
                    private StringBuffer sb;
                    private int finishLoadItem = 0;

                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        mDisposable = d;
                        mDialog = LoadDialogUtil.createLoadingDialog(context, "正在加载最新数据...");
                        sb = new StringBuffer();
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull String s) {
                        Log.d(TAG, "onNext: " + s);
                        finishLoadItem++;
                        if (!s.contains("成功"))
                            sb.append(s + ",");
                        if (finishLoadItem == 4) {
                            LoadDialogUtil.closeDialog(mDialog);
                            if (!sb.toString().isEmpty()) {
                                Toast.makeText(mContext, sb.toString().substring(0, sb.length() - 1), Toast.LENGTH_LONG).show();
                                Toast.makeText(mContext, "正在展示缓存数据, 部分操作可能会失效", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        mDisposable.dispose();
                        LoadDialogUtil.closeDialog(mDialog);
                        Toast.makeText(mContext, "加载失败, " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void login(FragmentActivity fragmentActivity, String account, String pwd, boolean isRemember) {
        Observable<ApiResult> observable = userService.userLogin(account.toString(), pwd);
        Disposable disposable = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    Log.d(TAG, "toMainActivity: 登录成功，正在跳转");
                    if (isRemember) {
                        CommonUtil.setUsernameToSP(mContext, account);
                        CommonUtil.setPwdToSP(mContext, pwd);
                    }
                    LinkedTreeMap linkedTreeMap = (LinkedTreeMap) apiResult.data;
                    Object o = linkedTreeMap.get("id");
                    Double d = (Double) o;
                    Long userId = d.longValue();
                    CommonUtil.setNowUserIdToSP(mContext, userId);
                    CommonUtil.setNowUsernameToSP(mContext, account);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                    fragmentActivity.finish();
                }, throwable -> {
                    Log.d(TAG, "toMainActivity: 登录失败 => " + throwable.getMessage());
                });
    }

    @Override
    public void startAct(Long id) {
        ActIntroItem aii = LitePal.find(ActIntroItem.class, id);
        Disposable disposable = actService.startAct(aii.getActId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResult>() {
                    @Override
                    public void accept(ApiResult apiResult) throws Exception {
                        if (apiResult.success) {
                            boolean success = showManager.startCreateAct(id);
                            if (success)
                                Toast.makeText(mContext, "开始活动成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, "开始活动失败, 本地问题", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "开始活动失败, 后台问题", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "开始活动失败, 网络问题", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void stopAct(Long id) {
        ActIntroItem aii = LitePal.find(ActIntroItem.class, id);
        Disposable disposable = actService.stopAct(aii.getActId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResult>() {
                    @Override
                    public void accept(ApiResult apiResult) throws Exception {
                        if (apiResult.success) {
                            boolean success = showManager.stopCreateAct(id);
                            if (success)
                                Toast.makeText(mContext, "停止活动成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, "停止活动失败, 本地问题", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "停止活动失败, 后台问题", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "停止活动失败, 网络问题", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void quitAct(Long id) {
        ActIntroItem aii = LitePal.find(ActIntroItem.class, id);
        Disposable disposable = userActService.userQuitAct(CommonUtil.getNowUserIdFromSP(mContext), aii.getActId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResult>() {
                    @Override
                    public void accept(ApiResult apiResult) throws Exception {
                        if (apiResult.success) {
                            boolean success = showManager.quitPartAct(id);
                            if (success)
                                Toast.makeText(mContext, "退出活动成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, "退出活动失败, 本地问题", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "退出活动失败, 后台问题", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "退出活动失败, 网络问题", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void editAct(ActIntroItem aii, EditActCallback callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", aii.getName());
        params.put("desc", aii.getIntro());
        params.put("location", aii.getLocation());
        params.put("time", aii.getTime());
        Observable<ApiResult> observable = actService.editAct(aii.getActId(), params);
        Disposable disposable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResult>() {
                    @Override
                    public void accept(ApiResult apiResult) throws Exception {
                        if (apiResult.success) {
                            boolean success = showManager.editCreateAct(aii);
                            if (success) {
                                Toast.makeText(mContext, "活动修改保存成功", Toast.LENGTH_SHORT).show();
                                callback.onFinish();
                            } else {
                                Toast.makeText(mContext, "活动修改保存失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "活动修改保存失败, 后台问题", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "活动修改保存失败, 网络问题", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void startAttend(Long id) {
        CreateAttendIntroItem caii = LitePal.find(CreateAttendIntroItem.class, id);
        Disposable disposable = attendService.startAttend(caii.getAttendId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResult>() {
                    @Override
                    public void accept(ApiResult apiResult) throws Exception {
                        if (apiResult.success) {
                            boolean success = showManager.startCreateAct(id);
                            if (success)
                                Toast.makeText(mContext, "开始签到成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, "开始签到失败, 本地问题", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "开始签到失败, 后台问题", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "开始签到失败, 网络问题", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void stopAttend(Long id) {
        CreateAttendIntroItem caii = LitePal.find(CreateAttendIntroItem.class, id);
        Disposable disposable = attendService.stopAttend(caii.getAttendId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResult>() {
                    @Override
                    public void accept(ApiResult apiResult) throws Exception {
                        if (apiResult.success) {
                            boolean success = showManager.startCreateAct(id);
                            if (success)
                                Toast.makeText(mContext, "关闭签到成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, "关闭签到失败, 本地问题", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "关闭签到失败, 后台问题", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "关闭签到失败, 网络问题", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void editAttend(Long id) {

    }

    @Override
    public void editAttendTime(Long id, String time) {
        CreateAttendIntroItem caii = LitePal.find(CreateAttendIntroItem.class, id);
        Disposable disposable = attendService.editAttendTime(caii.getAttendId(), time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResult>() {
                    @Override
                    public void accept(ApiResult apiResult) throws Exception {
                        if (apiResult.success) {
                            boolean success = showManager.changeCreateAttendTime(id, time);
                            if (success)
                                Toast.makeText(mContext, "编辑签到时间成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, "编辑签到时间失败, 本地问题", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "编辑签到时间失败, 后台问题", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "编辑签到时间失败, 网络问题", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void editAttendType(Long id, Integer type) {
        CreateAttendIntroItem caii = LitePal.find(CreateAttendIntroItem.class, id);
        Disposable disposable = attendService.editAttendType(caii.getAttendId(), type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResult>() {
                    @Override
                    public void accept(ApiResult apiResult) throws Exception {
                        if (apiResult.success) {
                            boolean success = showManager.changeCreateAttendType(id, type);
                            if (success)
                                Toast.makeText(mContext, "编辑签到类型成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, "编辑签到类型失败, 本地问题", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "编辑签到类型失败, 后台问题", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "编辑签到类型失败, 网络问题", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
