package com.aqinn.actmanagersysandroid.data.show;

/**
 * @author Aqinn
 * @date 2020/12/12 7:55 PM
 */
public class CreateAttendIntroItem {

    private Long id;

    private Long actId;

    private String name;

    private String time;

    // 签到方式 1: "视频签到" 2: "自助签到"
    private Integer type[];

    // 签到状态 1: "未开始" 2: "进行中" 3: "已结束"
    private Integer status;

    // 应签到人数
    private String shouldAttendCount;

    // 已签到人数
    private String haveAttendCount;

    // 未签到人数
    private String notAttendCount;

    public CreateAttendIntroItem(Long id, Long actId, String name, String time, Integer type[], Integer status, String shouldAttendCount, String haveAttendCount, String notAttendCount) {
        this.id = id;
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

    public Integer[] getType() {
        return type;
    }

    public void setType(Integer[] type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getShouldAttendCount() {
        return shouldAttendCount;
    }

    public void setShouldAttendCount(String shouldAttendCount) {
        this.shouldAttendCount = shouldAttendCount;
    }

    public String getHaveAttendCount() {
        return haveAttendCount;
    }

    public void setHaveAttendCount(String haveAttendCount) {
        this.haveAttendCount = haveAttendCount;
    }

    public String getNotAttendCount() {
        return notAttendCount;
    }

    public void setNotAttendCount(String notAttendCount) {
        this.notAttendCount = notAttendCount;
    }
}
