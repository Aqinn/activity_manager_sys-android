package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.data.DataSource;

import java.util.ArrayList;

/**
 * @author Aqinn
 * @date 2020/12/18 3:42 PM
 */
public class DataSourceParticipateAttendIntroItem extends DataSource<ParticipateAttendIntroItem> {

    public DataSourceParticipateAttendIntroItem() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        // 我创建的签到
        datas.add(new ParticipateAttendIntroItem(1L, 11L,"测试参与签到的活动名称1", "00:00-23:59", new Integer[]{1, 2}, 2, 3));
        datas.add(new ParticipateAttendIntroItem(2L, 6L,"测试参与签到的活动名称2", "00:00-23:59", new Integer[]{1}, 1, 2));
        datas.add(new ParticipateAttendIntroItem(3L, 8L,"测试参与签到的活动名称3", "00:00-23:59", new Integer[]{1, 2}, 1, 3));
        datas.add(new ParticipateAttendIntroItem(4L, 14L,"测试参与签到的活动名称4", "00:00-23:59", new Integer[]{2}, 2, 1));
        datas.add(new ParticipateAttendIntroItem(5L, 10L,"测试参与签到的活动名称5", "00:00-23:59", new Integer[]{2}, 1, 2));
        datas.add(new ParticipateAttendIntroItem(6L, 9L,"测试参与签到的活动名称6", "00:00-23:59", new Integer[]{1, 2}, 2, 1));
    }

}
