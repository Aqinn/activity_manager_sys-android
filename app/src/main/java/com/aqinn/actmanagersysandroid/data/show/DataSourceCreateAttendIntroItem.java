package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Refreshable;
import com.aqinn.actmanagersysandroid.entity.show.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.utils.CommonUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aqinn
 * @date 2020/12/18 3:40 PM
 */
public class DataSourceCreateAttendIntroItem extends DataSource<CreateAttendIntroItem> implements Refreshable {


    public DataSourceCreateAttendIntroItem() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        // 我创建的签到
        List<CreateAttendIntroItem> createAttendIntroItemList = LitePal.where("ownerId = ?", String.valueOf(CommonUtils.getNowUserIdFromSP(MyApplication.getContext()))).find(CreateAttendIntroItem.class);
        datas.addAll(createAttendIntroItemList);
    }

    @Override
    public void refresh(Object o) {
        List<CreateAttendIntroItem> createAttendIntroItemList = (List<CreateAttendIntroItem>) o;
        LitePal.deleteAll(CreateAttendIntroItem.class, "ownerId = ?", String.valueOf(CommonUtils.getNowUserIdFromSP(MyApplication.getContext())));
        LitePal.saveAll(createAttendIntroItemList);
        datas.clear();
        datas.addAll(createAttendIntroItemList);
        notifyAllObserver();
    }

}
