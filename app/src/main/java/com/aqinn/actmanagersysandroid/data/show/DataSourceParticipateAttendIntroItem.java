package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.ShowManager;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Observer;
import com.aqinn.actmanagersysandroid.data.Refreshable;
import com.aqinn.actmanagersysandroid.entity.Act;
import com.aqinn.actmanagersysandroid.entity.Attend;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.ParticipateAttendIntroItem;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aqinn
 * @date 2020/12/18 3:42 PM
 */
public class DataSourceParticipateAttendIntroItem extends DataSource<ParticipateAttendIntroItem> implements Refreshable {


    public DataSourceParticipateAttendIntroItem() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        // 我创建的签到
        List<ParticipateAttendIntroItem> participateAttendIntroItemList = LitePal.findAll(ParticipateAttendIntroItem.class);
        datas.addAll(participateAttendIntroItemList);
    }

    @Override
    public void refresh(Object o) {
        List<ParticipateAttendIntroItem> participateAttendIntroItemList = (List<ParticipateAttendIntroItem>) o;
        datas.clear();
        datas.addAll(participateAttendIntroItemList);
        notifyAllObserver();
    }
}
