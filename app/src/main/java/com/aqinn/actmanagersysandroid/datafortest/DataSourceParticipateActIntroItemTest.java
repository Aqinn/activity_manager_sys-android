package com.aqinn.actmanagersysandroid.datafortest;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author Aqinn
 * @date 2020/12/18 3:13 PM
 */
public class DataSourceParticipateActIntroItemTest  extends DataSource<ActIntroItem> {

    public DataSourceParticipateActIntroItemTest() {
        // 我参与的活动
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        datas.add(new ActIntroItem("_aqinn", "测试参与活动名称1", "10:00 - 21:50", "海华六栋 B307", "没啥的，就是测试一下", "已结束"));
        datas.add(new ActIntroItem("_aqinn", "测试参与活动名称2", "01:00 - 13:00", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        datas.add(new ActIntroItem("_aqinn", "测试参与活动名称3", "01:00 - 13:00", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        datas.add(new ActIntroItem("_aqinn", "测试参与活动名称4", "10:00 - 21:50", "海华六栋 B307", "没啥的，就是测试一下", "已结束"));
        datas.add(new ActIntroItem("_aqinn", "测试参与活动名称5", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        datas.add(new ActIntroItem("_aqinn", "测试参与活动名称6", "14:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        datas.add(new ActIntroItem("_aqinn", "测试参与活动名称7", "06:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", "未开始"));
        datas.add(new ActIntroItem("_aqinn", "测试参与活动名称8", "07:00 - 08:00", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        datas.add(new ActIntroItem("_aqinn", "测试参与活动名称9", "07:00 - 09:00", "海华六栋 B307", "没啥的，就是测试一下", "已结束"));
        datas.add(new ActIntroItem("_aqinn", "测试参与活动名称10", "10:10 - 20:20", "海华六栋 B307", "没啥的，就是测试一下", "已结束"));
        datas.add(new ActIntroItem("_aqinn", "测试参与活动名称11", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        datas.add(new ActIntroItem("_aqinn", "测试参与活动名称12", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", "未开始"));
        datas.add(new ActIntroItem("_aqinn", "测试参与活动名称13", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
    }

}
