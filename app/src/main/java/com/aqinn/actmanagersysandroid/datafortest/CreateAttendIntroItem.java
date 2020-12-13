package com.aqinn.actmanagersysandroid.datafortest;

/**
 * @author Aqinn
 * @date 2020/12/12 7:55 PM
 */
public class CreateAttendIntroItem {

    private String name;

    private String time;

    private String type;

    // 签到状态
    private String status;

    // 应签到人数
    private String shouldAttendCount;

    // 已签到人数
    private String haveAttendCount;

    // 未签到人数
    private String notAttendCount;

    public CreateAttendIntroItem(String name, String time, String type, String status, String shouldAttendCount, String haveAttendCount, String notAttendCount) {
        this.name = name;
        this.time = time;
        this.type = type;
        this.status = status;
        this.shouldAttendCount = shouldAttendCount;
        this.haveAttendCount = haveAttendCount;
        this.notAttendCount = notAttendCount;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
