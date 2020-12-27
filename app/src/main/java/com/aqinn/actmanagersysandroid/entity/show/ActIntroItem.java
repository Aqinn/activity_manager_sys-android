package com.aqinn.actmanagersysandroid.entity.show;

import org.litepal.crud.LitePalSupport;

/**
 * 活动简介数据 - 仅展示用
 *
 * @author Aqinn
 * @date 2020/12/12 3:34 PM
 */
public class ActIntroItem extends LitePalSupport {

    private Long id;

    private Long ownerId;

    private Long actId;

    private String creator;

    private String name;

    private String time;

    private String location;

    private String intro;

    // 活动状态 1: "未开始" 2: "进行中" 3: "已结束"
    private Integer status;

    public ActIntroItem(Long id, Long ownerId, Long actId, String creator, String name, String time, String location, String intro, Integer status) {
        this.id = id;
        this.ownerId = ownerId;
        this.actId = actId;
        this.creator = creator;
        this.name = name;
        this.time = time;
        this.location = location;
        this.intro = intro;
        this.status = status;
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

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ActIntroItem{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", actId=" + actId +
                ", creator='" + creator + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                ", intro='" + intro + '\'' +
                ", status=" + status +
                '}';
    }
}
