package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Observer;
import com.aqinn.actmanagersysandroid.entity.User;
import com.aqinn.actmanagersysandroid.entity.show.ParticipateAttendIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.UserDesc;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aqinn
 * @date 2020/12/18 3:42 PM
 */
public class DataSourceUserDesc extends DataSource<UserDesc> {


    public DataSourceUserDesc() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }


    private void initData() {
        List<UserDesc> userDescList = LitePal.findAll(UserDesc.class);
        datas.addAll(userDescList);

    }


}
