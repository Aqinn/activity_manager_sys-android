package com.aqinn.actmanagersysandroid.entity.show;

import org.litepal.crud.LitePalSupport;

/**
 * @author Aqinn
 * @date 2020/12/12 7:55 PM
 */
public class CreateAttendIntroItem extends LitePalSupport {

    private Long id;

    private Long ownerId;

    private Long attendId;

    private Long actId;

    private String name;

    private String time;

    // 签到方式 1: "视频签到" 2: "自助签到"
    private Integer type;

    // 签到状态 1: "未开始" 2: "进行中" 3: "已结束"
    private Integer status;

    // 应签到人数
    private Integer shouldAttendCount;

    // 已签到人数
    private Integer haveAttendCount;

    // 未签到人数
    private Integer notAttendCount;

    public CreateAttendIntroItem(Long id, Long ownerId, Long attendId, Long actId, String name, String time, Integer type, Integer status, Integer shouldAttendCount, Integer haveAttendCount, Integer notAttendCount) {
        this.id = id;
        this.ownerId = ownerId;
        this.attendId = attendId;
        this.actId = actId;
        this.name = name;
        this.time = time;
        this.type = type;
        this.status = status;
        this.shouldAttendCount = shouldAttendCount;
        this.haveAttendCount = haveAttendCount;
        this.notAttendCount = notAttendCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getAttendId() {
        return attendId;
    }

    public void setAttendId(Long attendId) {
        this.attendId = attendId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getShouldAttendCount() {
        return shouldAttendCount;
    }

    public void setShouldAttendCount(Integer shouldAttendCount) {
        this.shouldAttendCount = shouldAttendCount;
    }

    public Integer getHaveAttendCount() {
        return haveAttendCount;
    }

    public void setHaveAttendCount(Integer haveAttendCount) {
        this.haveAttendCount = haveAttendCount;
    }

    public Integer getNotAttendCount() {
        return notAttendCount;
    }

    public void setNotAttendCount(Integer notAttendCount) {
        this.notAttendCount = notAttendCount;
    }

    @Override
    public String toString() {
        return "CreateAttendIntroItem{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", attendId=" + attendId +
                ", actId=" + actId +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", shouldAttendCount=" + shouldAttendCount +
                ", haveAttendCount=" + haveAttendCount +
                ", notAttendCount=" + notAttendCount +
                '}';
    }
}
