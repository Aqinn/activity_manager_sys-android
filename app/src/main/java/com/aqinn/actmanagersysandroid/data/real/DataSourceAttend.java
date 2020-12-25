package com.aqinn.actmanagersysandroid.data.real;

import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.entity.Attend;
import com.aqinn.actmanagersysandroid.utils.CommonUtil;

import java.util.ArrayList;

/**
 * @author Aqinn
 * @date 2020/12/21 3:22 PM
 */
public class DataSourceAttend extends DataSource<Attend> {

    public DataSourceAttend() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        datas.add(new Attend(101L, 1L, 11L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(102L, 1L, 12L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(103L, 1L, 13L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(104L, 1L, 14L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(115L, 1L, 14L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(105L, 2L, 21L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(106L, 2L, 22L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(107L, 2L, 23L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(108L, 2L, 24L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(109L, 2L, 24L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(110L, 3L, 31L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(111L, 3L, 32L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(112L, 3L, 33L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(113L, 3L, 34L, "01:00-23:59", new Integer[]{1, 2}));
        datas.add(new Attend(114L, 3L, 34L, "01:00-23:59", new Integer[]{1, 2}));
    }

}
