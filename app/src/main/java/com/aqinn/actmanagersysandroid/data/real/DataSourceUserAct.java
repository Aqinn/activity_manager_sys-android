package com.aqinn.actmanagersysandroid.data.real;

import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.entity.UserAct;

import java.util.ArrayList;

/**
 * @author Aqinn
 * @date 2020/12/21 3:31 PM
 */
public class DataSourceUserAct extends DataSource<UserAct> {

    public DataSourceUserAct() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        datas.add(new UserAct(1L, 21L));
        datas.add(new UserAct(1L, 22L));
        datas.add(new UserAct(1L, 23L));
        datas.add(new UserAct(1L, 24L));
        datas.add(new UserAct(1L, 25L));
        datas.add(new UserAct(1L, 26L));
        datas.add(new UserAct(1L, 27L));
        datas.add(new UserAct(1L, 31L));
        datas.add(new UserAct(1L, 32L));
        datas.add(new UserAct(1L, 33L));
        datas.add(new UserAct(1L, 34L));
        datas.add(new UserAct(1L, 35L));
        datas.add(new UserAct(1L, 36L));
        datas.add(new UserAct(1L, 37L));

        datas.add(new UserAct(2L, 11L));
        datas.add(new UserAct(2L, 12L));
        datas.add(new UserAct(2L, 13L));
        datas.add(new UserAct(2L, 14L));
        datas.add(new UserAct(2L, 15L));
        datas.add(new UserAct(2L, 16L));
        datas.add(new UserAct(2L, 17L));
        datas.add(new UserAct(2L, 31L));
        datas.add(new UserAct(2L, 32L));
        datas.add(new UserAct(2L, 33L));
        datas.add(new UserAct(2L, 34L));
        datas.add(new UserAct(2L, 35L));
        datas.add(new UserAct(2L, 36L));
        datas.add(new UserAct(2L, 37L));

        datas.add(new UserAct(3L, 21L));
        datas.add(new UserAct(3L, 22L));
        datas.add(new UserAct(3L, 23L));
        datas.add(new UserAct(3L, 24L));
        datas.add(new UserAct(3L, 25L));
        datas.add(new UserAct(3L, 26L));
        datas.add(new UserAct(3L, 27L));
        datas.add(new UserAct(3L, 11L));
        datas.add(new UserAct(3L, 12L));
        datas.add(new UserAct(3L, 13L));
        datas.add(new UserAct(3L, 14L));
        datas.add(new UserAct(3L, 15L));
        datas.add(new UserAct(3L, 16L));
        datas.add(new UserAct(3L, 17L));


    }
}
