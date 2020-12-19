package com.aqinn.actmanagersysandroid;


import com.aqinn.actmanagersysandroid.components.DaggerActManagerComponent;
import com.aqinn.actmanagersysandroid.data.show.ActIntroItem;
import com.aqinn.actmanagersysandroid.data.show.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.qualifiers.ActCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.ActPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.UserDescDataSource;
import com.aqinn.actmanagersysandroid.utils.CommonUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Aqinn
 * @date 2020/12/18 5:33 PM
 */
public class ActManager {

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

    public ActManager() {
        DaggerActManagerComponent.builder().dataSourceComponent(MyApplication.getDataSourceComponent()).build().inject(this);
    }

    /**
     * 停止创建的活动
     *
     * @param actId
     * @return
     */
    public boolean stopCreateAct(Long actId) {
        boolean success = false;
        for (ActIntroItem aii : (List<ActIntroItem>) actC.getDatas()) {
            if (aii.getId().equals(actId)) {
                if (aii.getStatus() != 2)
                    break;
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
     * @param actId
     * @return
     */
    // TODO 还要把数据库中此人参与活动的记录移除
    public boolean quitPartAct(Long actId) {
        boolean success = false;
        for (ActIntroItem aii : (List<ActIntroItem>) actP.getDatas()) {
            if (aii.getId().equals(actId)) {
                actP.getDatas().remove(aii);
                actP.notifyAllObserver();
                success = true;
                break;
            }
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
        boolean success = false;
        for (ActIntroItem aii : (List<ActIntroItem>) actC.getDatas()) {
            if (aii.getId().equals(newAii.getId())) {
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
     * @param actId
     * @return
     */
    public boolean startCreateAct(Long actId) {
        boolean success = false;
        for (ActIntroItem aii : (List<ActIntroItem>) actC.getDatas()) {
            if (aii.getId().equals(actId)) {
                if (aii.getStatus() != 1)
                    break;
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
     * @param caii
     * @return
     */
    public boolean changeCreateAttendType(CreateAttendIntroItem caii) {
        boolean success = false;
        for (CreateAttendIntroItem caii_ : (List<CreateAttendIntroItem>) attC.getDatas()) {
            if (caii_.getId().equals(caii.getId())) {
                caii_.setType(caii.getType());
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
     * @param attId
     * @return
     */
    public boolean changeCreateAttendTime(Long attId, String newTime) {
        boolean success = false;
        for (CreateAttendIntroItem caii : (List<CreateAttendIntroItem>) attC.getDatas()) {
            if (caii.getId().equals(attId)) {
                caii.setTime(newTime);
                attC.notifyAllObserver();
                success = true;
                break;
            }
        }
        return success;
    }

    /**
     * 根据 id 获取创建的签到
     *
     * @param attId
     * @return
     */
    public CreateAttendIntroItem getCreateAttendIntroById(Long attId) {
        for (CreateAttendIntroItem caii : (List<CreateAttendIntroItem>) attC.getDatas()) {
            if (caii.getId().equals(attId)) {
                return caii;
            }
        }
        return null;
    }

    /**
     * 开启创建的签到
     *
     * @param attId
     * @return
     */
    public boolean startCreateAttend(Long attId) {
        boolean success = false;
        for (CreateAttendIntroItem caii : (List<CreateAttendIntroItem>) attC.getDatas()) {
            if (caii.getId().equals(attId)) {
                if (caii.getStatus() == 1) {
                    success = true;
                    caii.setStatus(2);
                    attC.notifyAllObserver();
                }
                break;
            }
        }
        return success;
    }

    /**
     * 结束创建的签到
     *
     * @param attId
     * @return
     */
    public boolean stopCreateAttend(Long attId) {
        boolean success = false;
        for (CreateAttendIntroItem caii : (List<CreateAttendIntroItem>) attC.getDatas()) {
            if (caii.getId().equals(attId)) {
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
     * TODO 还需要验证是否是本人的活动
     * @param newAii
     * @return
     */
    public boolean createAct(ActIntroItem newAii) {
        boolean success = false;
        newAii.setId(getRandomLong());
        success = actC.getDatas().add(newAii);
        if (success)
            actC.notifyAllObserver();
        return success;
    }

    /**
     * 创建一个签到
     * @param newCaii
     * @return
     */
    public boolean createAttend(CreateAttendIntroItem newCaii) {
        boolean success = false;
        newCaii.setId(getRandomLong());
        success = attC.getDatas().add(newCaii);
        if (success)
            attC.notifyAllObserver();
        return success;
    }

    /**
     * 获取一个随机 Long 类型数值
     * @return
     */
    private Long getRandomLong() {
        return CommonUtil.getRandomLong();
    }

}
