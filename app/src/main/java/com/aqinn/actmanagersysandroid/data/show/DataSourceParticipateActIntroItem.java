package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Refreshable;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;
import com.aqinn.actmanagersysandroid.utils.CommonUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aqinn
 * @date 2020/12/18 3:13 PM
 */
public class DataSourceParticipateActIntroItem extends DataSource<ActIntroItem> implements Refreshable {

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

    @Override
    public void refresh(Object o) {
        List<ActIntroItem> actIntroItemList = (List<ActIntroItem>) o;
        List<ActIntroItem> temp = new ArrayList<>();
        for (ActIntroItem aii:actIntroItemList) {
            if (!MyApplication.nowUserAccount.equals(aii.getCreator()))
                temp.add(aii);
        }
        datas.clear();
        datas.addAll(temp);
        notifyAllObserver();
    }
}
