package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.data.DataSource;

import java.util.ArrayList;

/**
 * @author Aqinn
 * @date 2020/12/18 3:13 PM
 */
public class DataSourceParticipateActIntroItem extends DataSource<ActIntroItem> {

    public DataSourceParticipateActIntroItem() {
        // 我参与的活动
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        datas.add(new ActIntroItem(6L, "_aqinn", "测试参与活动名称1", "10:00 - 21:50", "海华六栋 B307", "没啥的，就是测试一下", 3));
        datas.add(new ActIntroItem(7L, "_aqinn", "测试参与活动名称2", "01:00 - 13:00", "海华六栋 B307", "没啥的，就是测试一下", 2));
        datas.add(new ActIntroItem(8L, "_aqinn", "测试参与活动名称3", "01:00 - 13:00", "海华六栋 B307", "没啥的，就是测试一下", 2));
        datas.add(new ActIntroItem(9L, "_aqinn", "测试参与活动名称4", "10:00 - 21:50", "海华六栋 B307", "没啥的，就是测试一下", 3));
        datas.add(new ActIntroItem(10L, "_aqinn", "测试参与活动名称5", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", 2));
        datas.add(new ActIntroItem(11L, "_aqinn", "测试参与活动名称6", "14:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", 2));
        datas.add(new ActIntroItem(12L, "_aqinn", "测试参与活动名称7", "06:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", 1));
        datas.add(new ActIntroItem(13L, "_aqinn", "测试参与活动名称8", "07:00 - 08:00", "海华六栋 B307", "没啥的，就是测试一下", 2));
        datas.add(new ActIntroItem(14L, "_aqinn", "测试参与活动名称9", "07:00 - 09:00", "海华六栋 B307", "没啥的，就是测试一下", 3));
        datas.add(new ActIntroItem(15L, "_aqinn", "测试参与活动名称10", "10:10 - 20:20", "海华六栋 B307", "没啥的，就是测试一下", 3));
        datas.add(new ActIntroItem(16L, "_aqinn", "测试参与活动名称11", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", 2));
        datas.add(new ActIntroItem(17L, "_aqinn", "测试参与活动名称12", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", 1));
        datas.add(new ActIntroItem(18L, "_aqinn", "测试参与活动名称13", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", 2));
    }

}
