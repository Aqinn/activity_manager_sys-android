package com.aqinn.actmanagersysandroid.data;

/**
 * 我参与的签到简介数据 - 仅展示用
 * @author Aqinn
 * @date 2020/12/12 5:30 PM
 */
public class ParticipateAttendIntroItem {

    private String name;

    private String time;

    private String type;

    // 你的签到情况
    private String uStatus;

    // 签到状态
    private String status;

    public ParticipateAttendIntroItem(String name, String time, String type, String uStatus, String status) {
        this.name = name;
        this.time = time;
        this.type = type;
        this.uStatus = uStatus;
        this.status = status;
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

    public String getuStatus() {
        return uStatus;
    }

    public void setuStatus(String uStatus) {
        this.uStatus = uStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
