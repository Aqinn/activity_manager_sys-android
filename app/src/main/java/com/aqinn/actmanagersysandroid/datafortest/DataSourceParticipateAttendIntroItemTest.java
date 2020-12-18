package com.aqinn.actmanagersysandroid.datafortest;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author Aqinn
 * @date 2020/12/18 3:42 PM
 */
public class DataSourceParticipateAttendIntroItemTest extends DataSource<ParticipateAttendIntroItem> {

    public DataSourceParticipateAttendIntroItemTest() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        // 我创建的签到
        datas.add(new ParticipateAttendIntroItem("测试参与签到的活动名称1", "00:00-23:59", "视频签到, 自助签到", "未签到", "已结束"));
        datas.add(new ParticipateAttendIntroItem("测试参与签到的活动名称2", "00:00-23:59", "视频签到", "已签到", "进行中"));
        datas.add(new ParticipateAttendIntroItem("测试参与签到的活动名称3", "00:00-23:59", "视频签到, 自助签到", "已签到", "已结束"));
        datas.add(new ParticipateAttendIntroItem("测试参与签到的活动名称4", "00:00-23:59", "自助签到", "未签到", "未开始"));
        datas.add(new ParticipateAttendIntroItem("测试参与签到的活动名称5", "00:00-23:59", "自助签到", "已签到", "进行中"));
        datas.add(new ParticipateAttendIntroItem("测试参与签到的活动名称6", "00:00-23:59", "视频签到, 自助签到", "未签到", "未开始"));
    }

}
