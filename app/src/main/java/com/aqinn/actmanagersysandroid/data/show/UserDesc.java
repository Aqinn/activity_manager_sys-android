package com.aqinn.actmanagersysandroid.data.show;

/**
 * 用户个人信息 - 仅展示用
 * @author Aqinn
 * @date 2020/12/13 12:41 PM
 */
public class UserDesc {

    private String account;

    private String name;

    private String sex;

    private String contact;

    private String desc;

    public UserDesc(String account, String name, String sex, String contact, String desc) {
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
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
