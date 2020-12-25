package com.aqinn.actmanagersysandroid.entity.show;

import org.litepal.crud.LitePalSupport;

/**
 * 我参与的签到简介数据 - 仅展示用
 *
 * @author Aqinn
 * @date 2020/12/12 5:30 PM
 */
public class ParticipateAttendIntroItem extends LitePalSupport {

    private Long ownerId;

    private Long id;

    private Long actId;

    private String name;

    private String time;

    private Integer type;

    // 你的签到情况 1: "已签到" 2: "未签到"
    private Integer uStatus;

    // 签到状态 活动状态 1: "未开始" 2: "进行中" 3: "已结束"
    private Integer status;

    public ParticipateAttendIntroItem(Long ownerId, Long id, Long actId, String name, String time, Integer type, Integer uStatus, Integer status) {
        this.ownerId = ownerId;
        this.id = id;
        this.actId = actId;
        this.name = name;
        this.time = time;
        this.type = type;
        this.uStatus = uStatus;
        this.status = status;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getuStatus() {
        return uStatus;
    }

    public void setuStatus(Integer uStatus) {
        this.uStatus = uStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ParticipateAttendIntroItem{" +
                "ownerId=" + ownerId +
                ", id=" + id +
                ", actId=" + actId +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", type=" + type +
                ", uStatus=" + uStatus +
                ", status=" + status +
                '}';
    }
}
