package com.aqinn.actmanagersysandroid.data.real;

import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.entity.User;
import com.aqinn.actmanagersysandroid.utils.CommonUtil;

import java.util.ArrayList;

/**
 * @author Aqinn
 * @date 2020/12/19 8:13 PM
 */
public class DataSourceUser extends DataSource<User> {

    public DataSourceUser() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        datas.add(new User(1L, "_aqinn", "biubiubiu", "Aqinn", "13192205220", 1, "《我是猫》"));
        datas.add(new User(2L, "zzq", "biubiubiu", "zchin", "15521048948", 0, "我是一只鱼"));
        datas.add(new User(3L, "zzf", "biubiubiu", "zfung", "444502543@qq.com", 1, "《 I NEVER TOLD YOU 》"));
    }

}
