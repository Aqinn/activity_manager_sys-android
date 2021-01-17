package com.aqinn.actmanagersysandroid;


import android.content.ContentValues;
import android.util.Log;

import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.ParticipateAttendIntroItem;
import com.aqinn.actmanagersysandroid.qualifiers.ActCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.ActPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.UserDescDataSource;

import org.litepal.LitePal;

import java.util.List;

import javax.inject.Inject;

/**
 * 管理数据的展示
 *
 * @author Aqinn
 * @date 2020/12/18 5:33 PM
 */
public class ShowManager {

    private static final String TAG = "ShowManager";

    @Inject
    @ActCreateDataSource
    public DataSource actC;
    @Inject
    @ActPartDataSource
    public DataSource actP;
    @Inject
    @AttendCreateDataSource
    public DataSource attC;
    @Inject
    @AttendPartDataSource
    public DataSource attP;
    @Inject
    @UserDescDataSource
    public DataSource users;

    public ShowManager() {
        MyApplication.getApplicationComponent().inject(this);
    }

    /**
     * 停止创建的活动
     *
     * @param id
     * @return
     */
    public boolean stopCreateAct(Long id) {
        ContentValues values = new ContentValues();
        values.put("status", 3);
        int res = LitePal.update(ActIntroItem.class, values, id);
        if (res <= 0)
            return false;
        boolean success = false;
        for (ActIntroItem aii : (List<ActIntroItem>) actC.getDatas()) {
            if (aii.getId().equals(id)) {
                aii.setStatus(3);
                actC.notifyAllObserver();
                success = true;
                break;
            }
        }
        return success;
    }

    /**
     * 退出参与的活动
     *
     * @param id
     * @return
     */
    public boolean quitPartAct(Long id) {
        int res = LitePal.delete(ActIntroItem.class, id);
        if (res <= 0)
            return false;
        boolean success = false;
        Long actId = -1L;
        for (ActIntroItem aii : (List<ActIntroItem>) actP.getDatas()) {
            if (aii.getId().equals(id)) {
                actP.getDatas().remove(aii);
                actP.notifyAllObserver();
                success = true;
                actId = aii.getActId();
                break;
            }
        }
        if (success) {
            for (ParticipateAttendIntroItem paii : (List<ParticipateAttendIntroItem>) attP.getDatas()) {
                if (paii.getActId().equals(actId)) {
                    attP.getDatas().remove(paii);
                    paii.delete();
                }
            }
            attP.notifyAllObserver();
        }
        return success;
    }

    /**
     * 编辑创建的活动
     *
     * @param newAii
     * @return
     */
    public boolean editCreateAct(ActIntroItem newAii) {
        int res = newAii.update(newAii.getId());
        if (res <= 0)
            return false;
        boolean success = false;
        for (ActIntroItem aii : (List<ActIntroItem>) actC.getDatas()) {
            if (aii.getId().equals(newAii.getId())) {
                aii.setOwnerId(newAii.getOwnerId());
                aii.setActId(newAii.getActId());
                aii.setCreator(newAii.getCreator());
                aii.setName(newAii.getTime());
                aii.setStatus(newAii.getStatus());
                aii.setIntro(newAii.getIntro());
                aii.setLocation(newAii.getLocation());
                aii.setTime(newAii.getTime());
                actC.notifyAllObserver();
                success = true;
                break;
            }
        }
        return success;
    }

    /**
     * 开始创建的活动
     *
     * @param id
     * @return
     */
    public boolean startCreateAct(Long id) {
        ContentValues values = new ContentValues();
        values.put("status", 2);
        int res = LitePal.update(ActIntroItem.class, values, id);
        if (res <= 0)
            return false;
        boolean success = false;
        for (ActIntroItem aii : (List<ActIntroItem>) actC.getDatas()) {
            if (aii.getId().equals(id)) {
                aii.setStatus(2);
                actC.notifyAllObserver();
                success = true;
                break;
            }
        }
        return success;
    }

    /**
     * 更改创建的签到的签到方式
     *
     * @param id
     * @param type
     * @return
     */
    public boolean changeCreateAttendType(Long id, Integer type) {
        ContentValues values = new ContentValues();
        values.put("type", type);
        int res = LitePal.update(CreateAttendIntroItem.class, values, id);
        if (res <= 0)
            return false;
        boolean success = false;
        for (CreateAttendIntroItem caii_ : (List<CreateAttendIntroItem>) attC.getDatas()) {
            if (caii_.getId().equals(id)) {
                caii_.setType(type);
                attC.notifyAllObserver();
                success = true;
                break;
            }
        }
        return success;
    }

    /**
     * 更改创建的签到的签到时间
     *
     * @param id
     * @return
     */
    public boolean changeCreateAttendTime(Long id, String newTime) {
        ContentValues values = new ContentValues();
        values.put("time", newTime);
        int res = LitePal.update(CreateAttendIntroItem.class, values, id);
        if (res <= 0)
            return false;
        boolean success = false;
        for (CreateAttendIntroItem caii : (List<CreateAttendIntroItem>) attC.getDatas()) {
            if (caii.getId().equals(id)) {
                caii.setTime(newTime);
                attC.notifyAllObserver();
                success = true;
                break;
            }
        }
        return success;
    }

    /**
     * 开启创建的签到
     *
     * @param id
     * @return
     */
    public boolean startCreateAttend(Long id) {
        ContentValues values = new ContentValues();
        values.put("status", 2);
        int res = LitePal.update(CreateAttendIntroItem.class, values, id);
        if (res <= 0)
            return false;
        boolean success = false;
        for (CreateAttendIntroItem caii : (List<CreateAttendIntroItem>) attC.getDatas()) {
            if (caii.getId().equals(id)) {
                success = true;
                caii.setStatus(2);
                attC.notifyAllObserver();
                break;
            }
        }
        return success;
    }

    /**
     * 结束创建的签到
     *
     * @param id
     * @return
     */
    public boolean stopCreateAttend(Long id) {
        ContentValues values = new ContentValues();
        values.put("status", 3);
        int res = LitePal.update(CreateAttendIntroItem.class, values, id);
        if (res <= 0)
            return false;
        boolean success = false;
        for (CreateAttendIntroItem caii : (List<CreateAttendIntroItem>) attC.getDatas()) {
            if (caii.getId().equals(id)) {
                if (caii.getStatus() == 2) {
                    success = true;
                    caii.setStatus(3);
                    attC.notifyAllObserver();
                }
                break;
            }
        }
        return success;
    }

    /**
     * 创建一个活动
     *
     * @param newAii
     * @return
     */
    public boolean createAct(ActIntroItem newAii) {
        boolean res = newAii.save();
        if (!res)
            return false;
        ActIntroItem dbAii = LitePal.findLast(ActIntroItem.class);
        actC.getDatas().add(0, dbAii);
        actC.notifyAllObserver();
        return true;
    }

    /**
     * 创建一个签到
     *
     * @param newCaii
     * @return
     */
    public boolean createAttend(CreateAttendIntroItem newCaii) {
        boolean success = false;
        success = newCaii.save();
        if (!success)
            return success;
        success = attC.getDatas().add(newCaii);
        if (success)
            attC.notifyAllObserver();
        return success;
    }

    /**
     * 加入活动
     *
     * @param aii
     * @return
     */
    public boolean joinAct(ActIntroItem aii) {
        boolean res = aii.save();
        if (!res)
            return false;
        ActIntroItem dbAii = LitePal.findLast(ActIntroItem.class);
        actP.getDatas().add(0, dbAii);
        actP.notifyAllObserver();
        return true;
    }

    public boolean refreshAttendCount(CreateAttendIntroItem newCaii) {
        boolean success = false;
        List<CreateAttendIntroItem> list = attC.getDatas();
        for (CreateAttendIntroItem caii: list) {
            if (caii.getId().equals(newCaii.getId())) {
                caii.setShouldAttendCount(newCaii.getShouldAttendCount());
                caii.setHaveAttendCount(newCaii.getHaveAttendCount());
                caii.setNotAttendCount(newCaii.getNotAttendCount());
                success = true;
                break;
            }
        }
        attC.notifyAllObserver();
        return success;
    }

    // 不要用这个，是有问题的，懒得删
    @Deprecated
    public boolean refreshAttendCount_old(CreateAttendIntroItem newCaii) {
        boolean success = false;
        for (int i = 0; i < attC.getDatas().size(); i++) {
            if (((CreateAttendIntroItem) attC.getDatas().get(i)).getAttendId().equals(newCaii.getAttendId())) {
                ContentValues values = new ContentValues();
                if (((CreateAttendIntroItem) attC.getDatas().get(i)).getShouldAttendCount().equals(newCaii.getShouldAttendCount()) &&
                        ((CreateAttendIntroItem) attC.getDatas().get(i)).getHaveAttendCount().equals(newCaii.getHaveAttendCount()))
                    return true;
                values.put("shouldAttendCount", newCaii.getShouldAttendCount());
                values.put("haveAttendCount", newCaii.getHaveAttendCount());
                values.put("notAttendCount", newCaii.getNotAttendCount());
                String attendId = String.valueOf(newCaii.getAttendId());
                int res = LitePal.updateAll(CreateAttendIntroItem.class, values, "attendId = ?", attendId);
                if (res > 0)
                    success = true;
                break;
            }
        }
        if (success)
            attC.notifyAllObserver();
        return success;
    }

}
