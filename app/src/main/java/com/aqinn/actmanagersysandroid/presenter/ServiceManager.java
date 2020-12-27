package com.aqinn.actmanagersysandroid.presenter;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.aqinn.actmanagersysandroid.activity.LoginActivity;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;

/**
 * 服务管理器
 * 根据展示项目（UserDesc、ActIntroItem、CreateAttendIntroItem 以及 ParticipateAttendIntroItem）的 id 提供服务
 * @author Aqinn
 * @date 2020/12/27 1:58 PM
 */
public interface ServiceManager {

    void checkData(Context context);

    void login(FragmentActivity fragmentActivity, String account, String pwd, boolean isRemember);

    void startAct(Long id);

    void stopAct(Long id);

    void quitAct(Long id);

    void editAct(ActIntroItem aii, EditActCallback callback);

    void startAttend(Long id);

    void stopAttend(Long id);

    void editAttend(Long id);

    void editAttendTime(Long id, String time);

    void editAttendType(Long id, Integer type);

    interface EditActCallback{
        void onFinish();
    }

}
