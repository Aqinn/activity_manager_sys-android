package com.aqinn.actmanagersysandroid.datafortest;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author Aqinn
 * @date 2020/12/18 3:40 PM
 */
public class DataSourceCreateAttendIntroItemTest extends DataSource<CreateAttendIntroItem> {

    public DataSourceCreateAttendIntroItemTest() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        // 我创建的签到
        datas.add(new CreateAttendIntroItem("测试创建签到的活动名称1", "00:00-23:59", "视频签到, 自助签到", "未开始", "100", "50", "50"));
        datas.add(new CreateAttendIntroItem("测试创建签到的活动名称2", "00:00-23:59", "自助签到", "未开始", "100", "50", "50"));
        datas.add(new CreateAttendIntroItem("测试创建签到的活动名称3", "00:00-23:59", "视频签到, 自助签到", "已结束", "100", "50", "50"));
        datas.add(new CreateAttendIntroItem("测试创建签到的活动名称4", "00:00-23:59", "视频签到", "进行中", "100", "50", "50"));
    }

}
