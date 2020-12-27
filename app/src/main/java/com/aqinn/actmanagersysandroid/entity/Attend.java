package com.aqinn.actmanagersysandroid.entity;

/**
 * 签到 - 实体类
 *
 * @author Aqinn
 * @date 2020/12/19 7:35 PM
 */
public class Attend {

    private Long id;

    private Long uId;

    private Long actId;

    private String time;

    // 签到方式 01: "视频签到" 10: "自助签到" 11: "两个都有签到"
    private Integer type;

    // 签到状态 1: 未开始 2: 进行中 3: 已结束
    private Integer isOpen;

    public Attend(Long id, Long uId, Long actId, String time, Integer type, Integer isOpen) {
        this.id = id;
        this.uId = uId;
        this.actId = actId;
        this.time = time;
        this.type = type;
        this.isOpen = isOpen;
    }

    public Attend(Long uId, Long actId, String time, Integer type, Integer isOpen) {
        this.uId = uId;
        this.actId = actId;
        this.time = time;
        this.type = type;
        this.isOpen = isOpen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    @Override
    public String toString() {
        return "Attend{" +
                "id=" + id +
                ", uId=" + uId +
                ", actId=" + actId +
                ", time='" + time + '\'' +
                ", type=" + type +
                ", isOpen=" + isOpen +
                '}';
    }
}
