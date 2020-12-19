package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.data.DataSource;

import java.util.ArrayList;

/**
 * @author Aqinn
 * @date 2020/12/18 3:40 PM
 */
public class DataSourceCreateAttendIntroItem extends DataSource<CreateAttendIntroItem> {

    public DataSourceCreateAttendIntroItem() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        // 我创建的签到
        datas.add(new CreateAttendIntroItem(1L, 1L,"测试创建签到的活动名称1", "01:00-23:59", new Integer[]{1, 2}, 1, "100", "50", "50"));
        datas.add(new CreateAttendIntroItem(2L, 1L,"测试创建签到的活动名称2", "02:00-21:00", new Integer[]{2}, 1, "100", "50", "50"));
        datas.add(new CreateAttendIntroItem(3L, 2L,"测试创建签到的活动名称3", "03:00-15:5", new Integer[]{1, 2}, 3, "100", "50", "50"));
        datas.add(new CreateAttendIntroItem(4L, 4L,"测试创建签到的活动名称4", "04:00-12:59", new Integer[]{1}, 2, "100", "50", "50"));
    }

}
