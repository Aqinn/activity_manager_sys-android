package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Refreshable;
import com.aqinn.actmanagersysandroid.entity.show.ParticipateAttendIntroItem;
import com.aqinn.actmanagersysandroid.utils.CommonUtil;

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
        List<ParticipateAttendIntroItem> participateAttendIntroItemList = LitePal.where("ownerId = ?", String.valueOf(CommonUtil.getNowUserIdFromSP(MyApplication.getContext()))).find(ParticipateAttendIntroItem.class);
        datas.addAll(participateAttendIntroItemList);
    }

    @Override
    public void refresh(Object o) {
        List<ParticipateAttendIntroItem> participateAttendIntroItemList = (List<ParticipateAttendIntroItem>) o;
        LitePal.deleteAll(ParticipateAttendIntroItem.class, "ownerId = ?", String.valueOf(CommonUtil.getNowUserIdFromSP(MyApplication.getContext())));
        LitePal.saveAll(participateAttendIntroItemList);
        datas.clear();
        datas.addAll(participateAttendIntroItemList);
        notifyAllObserver();
    }

}
