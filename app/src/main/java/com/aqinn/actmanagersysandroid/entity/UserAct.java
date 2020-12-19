package com.aqinn.actmanagersysandroid.entity;

/**
 * 用户参与活动 - 实体类
 * @author Aqinn
 * @date 2020/12/19 7:49 PM
 */
public class UserAct {

    private Long uId;

    private Long actId;

    public UserAct(Long uId, Long actId) {
        this.uId = uId;
        this.actId = actId;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }
}
