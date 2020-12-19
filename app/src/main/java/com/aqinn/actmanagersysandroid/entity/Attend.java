package com.aqinn.actmanagersysandroid.entity;

/**
 * 签到 - 实体类
 *
 * @author Aqinn
 * @date 2020/12/19 7:35 PM
 */
public class Attend {

    private Long id;

    private Long actId;

    private String begin;

    private String end;

    // 签到方式 1: "视频签到" 2: "自助签到"
    private Integer type[];

    public Attend(Long id, Long actId, String begin, String end, Integer[] type) {
        this.id = id;
        this.actId = actId;
        this.begin = begin;
        this.end = end;
        this.type = type;
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

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer[] getType() {
        return type;
    }

    public void setType(Integer[] type) {
        this.type = type;
    }
}
