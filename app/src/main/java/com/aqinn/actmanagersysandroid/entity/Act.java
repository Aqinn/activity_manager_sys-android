package com.aqinn.actmanagersysandroid.entity;

/**
 * 活动 - 实体类
 * @author Aqinn
 * @date 2020/12/19 7:40 PM
 */
public class Act {

    private Long id;

    private Long uId;

    private Long code;

    private Long pwd;

    private String name;

    private String desc;

    private String location;

    private String time;

    public Act(Long id, Long uId, Long code, Long pwd, String name, String desc, String location, String time) {
        this.id = id;
        this.uId = uId;
        this.code = code;
        this.pwd = pwd;
        this.name = name;
        this.desc = desc;
        this.location = location;
        this.time = time;
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

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getPwd() {
        return pwd;
    }

    public void setPwd(Long pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
