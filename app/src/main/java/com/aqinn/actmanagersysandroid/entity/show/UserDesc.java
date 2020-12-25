package com.aqinn.actmanagersysandroid.entity.show;

import org.litepal.crud.LitePalSupport;

/**
 * 用户个人信息 - 仅展示用
 * @author Aqinn
 * @date 2020/12/13 12:41 PM
 */
public class UserDesc extends LitePalSupport {

    private String account;

    private String name;

    private Integer sex;

    private String contact;

    private String desc;

    public UserDesc(String account, String name, Integer sex, String contact, String desc) {
        this.account = account;
        this.name = name;
        this.sex = sex;
        this.contact = contact;
        this.desc = desc;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
