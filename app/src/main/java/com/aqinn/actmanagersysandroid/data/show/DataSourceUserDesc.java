package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Observer;
import com.aqinn.actmanagersysandroid.data.Refreshable;
import com.aqinn.actmanagersysandroid.entity.User;
import com.aqinn.actmanagersysandroid.entity.show.UserDesc;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aqinn
 * @date 2020/12/18 3:42 PM
 */
public class DataSourceUserDesc extends DataSource<UserDesc> implements Refreshable {


    public DataSourceUserDesc() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        List<UserDesc> userDescList = LitePal.where("account = ?", MyApplication.nowUserAccount).find(UserDesc.class);
        datas.addAll(userDescList);
    }

    @Override
    public void refresh(Object o) {
        UserDesc userDesc = (UserDesc) o;
        datas.clear();
        datas.add(userDesc);
        notifyAllObserver();
        LitePal.deleteAll(UserDesc.class, "account = ?", String.valueOf(userDesc.getAccount()));
        userDesc.save();
    }

}
