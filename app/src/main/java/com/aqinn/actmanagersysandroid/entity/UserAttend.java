package com.aqinn.actmanagersysandroid.entity;

/**
 * 用户签到 - 实体类
 * @author Aqinn
 * @date 2020/12/19 7:49 PM
 */
public class UserAttend {

    private Long uId;

    private Long attendId;

    private String attendTime;

    private Integer attendType;

    public UserAttend(Long uId, Long attendId, String attendTime, Integer attendType) {
        this.uId = uId;
        this.attendId = attendId;
        this.attendTime = attendTime;
        this.attendType = attendType;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public Long getAttendId() {
        return attendId;
    }

    public void setAttendId(Long attendId) {
        this.attendId = attendId;
    }

    public String getAttendTime() {
        return attendTime;
    }

    public void setAttendTime(String attendTime) {
        this.attendTime = attendTime;
    }

    public Integer getAttendType() {
        return attendType;
    }

    public void setAttendType(Integer attendType) {
        this.attendType = attendType;
    }
}
