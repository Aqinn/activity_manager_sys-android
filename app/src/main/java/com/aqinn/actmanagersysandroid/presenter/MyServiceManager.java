package com.aqinn.actmanagersysandroid.presenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.alibaba.fastjson.JSONObject;
import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.ShowManager;
import com.aqinn.actmanagersysandroid.activity.MainActivity;
import com.aqinn.actmanagersysandroid.data.ApiResult;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Refreshable;
import com.aqinn.actmanagersysandroid.entity.User;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.ParticipateAttendIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.UserDesc;
import com.aqinn.actmanagersysandroid.qualifiers.ActCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.ActPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.UserDescDataSource;
import com.aqinn.actmanagersysandroid.retrofitservice.ActService;
import com.aqinn.actmanagersysandroid.retrofitservice.AttendService;
import com.aqinn.actmanagersysandroid.retrofitservice.ShowItemService;
import com.aqinn.actmanagersysandroid.retrofitservice.UserActService;
import com.aqinn.actmanagersysandroid.retrofitservice.UserAttendService;
import com.aqinn.actmanagersysandroid.retrofitservice.UserService;
import com.aqinn.actmanagersysandroid.utils.CommonUtils;
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
        MyApplication.getApplicationComponent().inject(this);
        Log.d("singleTest", "MyServiceManager: dsAttC => " + dsAttC);
        this.mContext = MyApplication.getContext();
    }

    @Override
    public void checkData(Context context) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            // 检查有没有登录信息
            if (CommonUtils.getNowUserIdFromSP(mContext).equals(CommonUtils.ERR_USER_ID)) {
                emitter.onError(new Throwable("没有任何已登录记录"));
                return;
            }
            // 初始化数据 请求网络数据
            Observable<ApiResult<UserDesc>> observableUserDesc = showItemService.getUserDesc(CommonUtils.getNowUserIdFromSP(mContext));
            Disposable d1 = observableUserDesc.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userDescApiResult -> {
                        CommonUtils.setNowUsernameToSP(mContext, userDescApiResult.data.getAccount());
                        ((Refreshable) dsUserDesc).refresh(userDescApiResult.data);
                        emitter.onNext("成功加载用户数据");
                    }, throwable -> {
                        Log.d(TAG, "MyApplication.onCreate() 加载用户数据出错: " + throwable.getMessage());
                        emitter.onNext("加载用户数据出错");
                    });
            Observable<ApiResult<List<ActIntroItem>>> observableActIntroItem
                    = showItemService.getActIntroItem(CommonUtils.getNowUserIdFromSP(mContext));
            Disposable d2 = observableActIntroItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listApiResult -> {
                        if (listApiResult.data.isEmpty()) {
                            emitter.onNext("成功加载活动数据, 活动数据为空");
                        }
                        ((Refreshable) dsActC).refresh(listApiResult.data);
                        ((Refreshable) dsActP).refresh(listApiResult.data);
                        emitter.onNext("成功加载活动数据");
                    }, throwable -> {
                        Log.d(TAG, "MyApplication.onCreate() 加载活动数据出错: " + throwable.getMessage());
                        emitter.onNext("加载活动数据出错");
                    });
            Observable<ApiResult<List<CreateAttendIntroItem>>> observableCreateAttendIntroItem
                    = showItemService.getCreateAttendIntroItem(CommonUtils.getNowUserIdFromSP(mContext));
            Disposable d3 = observableCreateAttendIntroItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listApiResult -> {
                        if (listApiResult.data.isEmpty()) {
                            emitter.onNext("成功加载我创建的签到数据, 活动数据为空");
                        }
                        ((Refreshable) dsAttC).refresh(listApiResult.data);
                        emitter.onNext("成功加载我创建的签到数据");
                    }, throwable -> {
                        Log.d(TAG, "MyServiceManager.checkData() 加载我创建的签到数据出错: " + throwable.getMessage());
                        emitter.onNext("加载我创建的签到数据出错");
                    });
            Observable<ApiResult<List<ParticipateAttendIntroItem>>> observableParticipateAttendIntroItem
                    = showItemService.getParticipateAttendIntroItem(CommonUtils.getNowUserIdFromSP(mContext));
            Disposable d4 = observableParticipateAttendIntroItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listApiResult -> {
                        if (listApiResult.data.isEmpty()) {
                            emitter.onNext("成功加载我参与的签到数据, 活动数据为空");
                        }
                        ((Refreshable) dsAttP).refresh(listApiResult.data);
                        emitter.onNext("成功加载我参与的签到数据");
                    }, throwable -> {
                        throwable.printStackTrace();
                        Log.d(TAG, "MyServiceManager.checkData() 加载我参与的签到数据出错: " + throwable.getMessage());
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
    public void register(User user, RegisterCallback callback) {
        Observable<ApiResult> observable = userService.createUser(
                user.getAccount(), user.getPwd(), user.getName(), user.getContact(), user.getSex(), user.getIntro()
        );
        Disposable disposable = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (!apiResult.success) {
                        Log.d(TAG, "register: 注册失败, " + apiResult.errMsg);
                        Toast.makeText(mContext, "登录失败, " + apiResult.errMsg, Toast.LENGTH_LONG).show();
                    }
                    Log.d(TAG, "register: 注册成功，正在跳转");
                    Toast.makeText(mContext, "注册成功，正在跳转", Toast.LENGTH_LONG).show();
                    callback.onFinish();
                }, throwable -> {
                    Log.d(TAG, "register: 注册失败 => " + throwable.getMessage());
                    Toast.makeText(mContext, "注册失败, 网络出问题了", Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public void login(FragmentActivity fragmentActivity, String account, String pwd, boolean isRemember) {
        Observable<ApiResult> observable = userService.userLogin(account.toString(), pwd);
        Disposable disposable = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (!apiResult.success) {
                        Log.d(TAG, "toMainActivity: 登录失败, " + apiResult.errMsg);
                        Toast.makeText(mContext, "登录失败, " + apiResult.errMsg, Toast.LENGTH_LONG).show();
                        return;
                    }
                    Log.d(TAG, "toMainActivity: 登录成功，正在跳转");
                    if (isRemember) {
                        CommonUtils.setUsernameToSP(mContext, account);
                        CommonUtils.setPwdToSP(mContext, pwd);
                    }
                    LinkedTreeMap linkedTreeMap = (LinkedTreeMap) apiResult.data;
                    Object o = linkedTreeMap.get("id");
                    Double d = (Double) o;
                    Long userId = d.longValue();
                    CommonUtils.setNowUserIdToSP(mContext, userId);
                    CommonUtils.setNowUsernameToSP(mContext, account);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    fragmentActivity.finish();
                }, throwable -> {
                    Log.d(TAG, "toMainActivity: 登录失败 => " + throwable.getMessage());
                    Toast.makeText(mContext, "登录失败, 网络出问题了", Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public void createAct(ActIntroItem aii, CreateActCallback callback) {
        if (!verifyAct(aii)) {
            callback.onFail();
            return;
        }
        Disposable disposable = actService.createAct(
                aii.getOwnerId(), aii.getName(), aii.getIntro(), aii.getLocation(), aii.getTime()
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResult>() {
                    @Override
                    public void accept(ApiResult apiResult) throws Exception {
                        if (apiResult.success) {
                            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) apiResult.data;
                            Long actId = ((Double) (linkedTreeMap.get("id"))).longValue();
                            Long code = ((Double) (linkedTreeMap.get("code"))).longValue();
                            Long pwd = ((Double) (linkedTreeMap.get("pwd"))).longValue();
                            aii.setActId(actId);
                            aii.setCode(code);
                            aii.setPwd(pwd);
                            boolean success = showManager.createAct(aii);
                            if (success) {
                                Toast.makeText(mContext, "创建活动成功", Toast.LENGTH_SHORT).show();
                                callback.onFinish();
                            } else {
                                Toast.makeText(mContext, "创建活动失败, 本地问题", Toast.LENGTH_SHORT).show();
                                callback.onError();
                            }
                        } else {
                            Toast.makeText(mContext, "创建活动失败, 后台问题: " + apiResult.errMsg, Toast.LENGTH_SHORT).show();
                            callback.onError();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "创建活动失败, 网络问题", Toast.LENGTH_SHORT).show();
                        callback.onError();
                    }
                });
    }

    @Override
    public void joinAct(Long code, Long pwd) {
        Disposable disposable = userActService.userJoinAct(CommonUtils.getNowUserIdFromSP(mContext), code, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResult<ActIntroItem>>() {
                    @Override
                    public void accept(ApiResult<ActIntroItem> actIntroItemApiResult) throws Exception {
                        if (actIntroItemApiResult.success) {
                            ActIntroItem aii = (ActIntroItem) actIntroItemApiResult.data;
                            boolean success = showManager.joinAct(aii);
                            if (success) {
                                Toast.makeText(mContext, "加入活动成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "加入活动失败, 本地问题", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "加入活动失败, 后台问题: " + actIntroItemApiResult.errMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "加入活动失败, 网络问题", Toast.LENGTH_SHORT).show();
                    }
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
                            Toast.makeText(mContext, "开始活动失败, 后台问题: " + apiResult.errMsg, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(mContext, "停止活动失败, 后台问题: " + apiResult.errMsg, Toast.LENGTH_SHORT).show();
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
        Disposable disposable = userActService.userQuitAct(CommonUtils.getNowUserIdFromSP(mContext), aii.getActId())
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
                            Toast.makeText(mContext, "退出活动失败, 后台问题: " + apiResult.errMsg, Toast.LENGTH_SHORT).show();
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
        if (!verifyAct(aii)) {
            callback.onFail();
            return;
        }
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
                                callback.onError();
                            }
                        } else {
                            Toast.makeText(mContext, "活动修改保存失败, 后台问题: " + apiResult.errMsg, Toast.LENGTH_SHORT).show();
                            callback.onError();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        Toast.makeText(mContext, "活动修改保存失败, 网络问题", Toast.LENGTH_SHORT).show();
                        callback.onError();
                    }
                });
    }

    @Override
    public void createAttend(Long actId, String time, Integer type, CreateAttendCallback callback) {
        Observable<ApiResult> observable = attendService.createAttend(CommonUtils.getNowUserIdFromSP(MyApplication.getContext()), actId, time, type);
        Disposable d = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.success) {
                        JSONObject jo = JSONObject.parseObject(String.valueOf(apiResult.data));
                        Long ownerId = jo.getLong("ownerId");
                        Long attendId = jo.getLong("attendId");
                        String name = jo.getString("actName");
                        int shouldAttendCount = jo.getInteger("shouldAttendCount");
                        CreateAttendIntroItem caii = new CreateAttendIntroItem(
                            ownerId,attendId,actId,name,time,type,1,shouldAttendCount,0,shouldAttendCount
                        );
                        if (showManager.createAttend(caii)) {
                            callback.onFinish();
                        }
                    } else {
                        Log.d(TAG, "createAttend: 网络请求成功，但是返回的数据是 false，错误信息如下: " + apiResult.errMsg);
                        callback.onFail();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    Log.d(TAG, "createAttend: 网络请求出错， 报错信息如下: " + throwable.getMessage());
                    callback.onFail();
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
                            boolean success = showManager.startCreateAttend(id);
                            if (success)
                                Toast.makeText(mContext, "开始签到成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, "开始签到失败, 本地问题", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "开始签到失败, 后台问题: " + apiResult.errMsg, Toast.LENGTH_SHORT).show();
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
                            boolean success = showManager.stopCreateAttend(id);
                            if (success)
                                Toast.makeText(mContext, "关闭签到成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, "关闭签到失败, 本地问题", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "关闭签到失败, 后台问题: " + apiResult.errMsg, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(mContext, "编辑签到时间失败, 后台问题: " + apiResult.errMsg, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(mContext, "编辑签到类型失败, 后台问题: " + apiResult.errMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "编辑签到类型失败, 网络问题", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean verifyAct(ActIntroItem aii) {
        if (aii.getName().replace(" ", "").isEmpty()
                || aii.getName().length() > 20
                || aii.getTime().replace(" ", "").isEmpty())
            return false;
        return true;
    }

    @Override
    public void refreshCreateAttend(Long attendId, Integer shouldAttendCount, Integer haveAttendCount, Integer notAttendCount) {
        CreateAttendIntroItem caii = LitePal.where("attendId = ?", String.valueOf(attendId)).find(CreateAttendIntroItem.class).get(0);
        showManager.refreshAttendCount(caii);
    }

    //    LinkedTreeMap linkedTreeMap = (LinkedTreeMap) apiResult.data;
//    Object o = linkedTreeMap.get("id");
//    Double d = (Double) o;
//    Long userId = d.longValue();

}
