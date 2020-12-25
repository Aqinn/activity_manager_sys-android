package com.aqinn.actmanagersysandroid.data.real;

import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.entity.UserAttend;

import java.util.ArrayList;

/**
 * @author Aqinn
 * @date 2020/12/21 3:35 PM
 */
public class DataSourceUserAttend extends DataSource<UserAttend> {
    public DataSourceUserAttend() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        datas.add(new UserAttend(1L, 106L, "00:00", 1));
        datas.add(new UserAttend(1L, 105L, "00:00", 2));
        datas.add(new UserAttend(1L, 107L, "00:00", 1));

        datas.add(new UserAttend(2L, 115L, "00:00", 1));
        datas.add(new UserAttend(2L, 101L, "00:00", 2));

        datas.add(new UserAttend(3L, 105L, "00:00", 1));
        datas.add(new UserAttend(3L, 106L, "00:00", 2));
    }
}
