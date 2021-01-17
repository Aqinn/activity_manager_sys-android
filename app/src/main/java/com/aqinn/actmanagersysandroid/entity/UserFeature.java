package com.aqinn.actmanagersysandroid.entity;

/**
 * @Author Aqinn
 * @Date 2021/1/15 1:18 下午
 */
public class UserFeature {

    private Long uId;

    /**
     * 用字符串的形式存储各个特征，如
     * "1,2,3,4,5,6,7,8 ... 128"
     */
    private String feature;

    public UserFeature() {
    }

    public UserFeature(Long uId, String feature) {
        this.uId = uId;
        this.feature = feature;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    @Override
    public String toString() {
        return "UserFeature{" +
                "uId=" + uId +
                ", feature='" + feature + '\'' +
                '}';
    }
}
