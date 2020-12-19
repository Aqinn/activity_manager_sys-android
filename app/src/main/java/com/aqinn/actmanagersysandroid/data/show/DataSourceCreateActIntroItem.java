package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Observer;
import com.aqinn.actmanagersysandroid.entity.Act;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aqinn
 * @date 2020/12/18 2:59 PM
 */
public class DataSourceCreateActIntroItem extends DataSource<ActIntroItem> implements Observer {

    private DataSource mDataSource;

    public DataSourceCreateActIntroItem() {
        // 我创建的活动
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    public void setDataSource(DataSource dataSource) {
        if (this.mDataSource != null)
            this.mDataSource.disposed(this);
        this.mDataSource = dataSource;
        this.mDataSource.attach(this);
    }

    private void initData() {
        datas.add(new ActIntroItem(1L, "_aqinn", "测试创建活动名称1111", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", 2));
        datas.add(new ActIntroItem(2L, "_aqinn", "测试创建活动名称2", "10:00 - 23:00", "海华六栋 B308", "测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。", 1));
        datas.add(new ActIntroItem(3L, "_aqinn", "测试创建活动名称3", "01:00 - 13:00", "海华六栋 B307", "zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅zbc好帅", 2));
        datas.add(new ActIntroItem(4L, "_aqinn", "测试创建活动名称4", "02:00 - 20:59", "海华六栋 B307", "没啥的，就是测试一下", 1));
        datas.add(new ActIntroItem(5L, "_aqinn", "测试创建活动名称5", "10:00 - 21:50", "海华六栋 B307", "没啥的，就是测试一下", 3));
    }

    /**
     * 根据最新的数据源更新本数据源
     * TODO 怎样可以更快地更新? 现在的做法太低效了。
     * TODO creator 以及 status 需要根据数据判断
     */
    private void updataData() {
        datas.clear();
        for (Act a : ((List<Act>) this.mDataSource.getDatas())) {
            datas.add(new ActIntroItem(a.getId(), "待获取", a.getName(), a.getTime(), a.getLocation(), a.getDesc(), 2));
        }
    }

    @Override
    public void update() {
        updataData();
        notifyAllObserver();
    }
}
