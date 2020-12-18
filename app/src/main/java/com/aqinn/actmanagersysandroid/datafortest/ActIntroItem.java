package com.aqinn.actmanagersysandroid.datafortest;

/**
 * 活动简介数据 - 仅展示用
 * @author Aqinn
 * @date 2020/12/12 3:34 PM
 */
public class ActIntroItem {

    private String creator;

    private String name;

    private String time;

    private String location;

    private String intro;

    private String status;

    public ActIntroItem(String creator, String name, String time, String location, String intro, String status) {
        this.creator = creator;
        this.name = name;
        this.time = time;
        this.location = location;
        this.intro = intro;
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
