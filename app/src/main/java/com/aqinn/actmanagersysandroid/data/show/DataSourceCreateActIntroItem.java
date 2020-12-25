package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Observer;
import com.aqinn.actmanagersysandroid.entity.Act;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;

import org.litepal.LitePal;

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
        List<ActIntroItem> actIntroItemList = LitePal.findAll(ActIntroItem.class);
        datas.addAll(actIntroItemList);
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
