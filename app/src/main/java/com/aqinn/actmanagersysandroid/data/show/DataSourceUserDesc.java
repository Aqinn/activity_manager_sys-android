package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Observer;
import com.aqinn.actmanagersysandroid.data.Refreshable;
import com.aqinn.actmanagersysandroid.entity.show.UserDesc;
import com.aqinn.actmanagersysandroid.utils.CommonUtil;

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
        if (CommonUtil.getNowUsernameFromSP(MyApplication.getContext()) == null)
            return;
        List<UserDesc> userDescList = LitePal.where("account = ?", CommonUtil.getNowUsernameFromSP(MyApplication.getContext())).find(UserDesc.class);
        datas.add(0, userDescList.get(0));
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
