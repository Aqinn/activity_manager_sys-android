package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

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
        List<ActIntroItem> actIntroItemList = LitePal.findAll(ActIntroItem.class);
        datas.addAll(actIntroItemList);
    }

}
