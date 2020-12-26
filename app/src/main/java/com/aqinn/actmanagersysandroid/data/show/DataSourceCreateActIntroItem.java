package com.aqinn.actmanagersysandroid.data.show;

import android.util.Log;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Observer;
import com.aqinn.actmanagersysandroid.data.Refreshable;
import com.aqinn.actmanagersysandroid.entity.Act;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aqinn
 * @date 2020/12/18 2:59 PM
 */
public class DataSourceCreateActIntroItem extends DataSource<ActIntroItem> implements Refreshable {

    private static final String TAG = "DataSourceCreateActIntr";

    public DataSourceCreateActIntroItem() {
        // 我创建的活动
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        List<ActIntroItem> actIntroItemList = LitePal.where("ownerId = ?", String.valueOf(MyApplication.nowUserId)).find(ActIntroItem.class);
        for (ActIntroItem aii:actIntroItemList) {
            if (MyApplication.nowUserAccount.equals(aii.getCreator())) {
                datas.add(aii);
            }
        }
    }

    @Override
    public void refresh(Object o) {
        List<ActIntroItem> actIntroItemList = (List<ActIntroItem>) o;
        LitePal.deleteAll(ActIntroItem.class, "ownerId = ?", String.valueOf(actIntroItemList.get(0).getOwnerId()));
        List<ActIntroItem> temp = new ArrayList<>();
        for (ActIntroItem aii:actIntroItemList) {
            if (MyApplication.nowUserAccount.equals(aii.getCreator())) {
                temp.add(aii);
            }
        }
        Log.d(TAG, "refresh: temp.size()" + temp.size());
        LitePal.saveAll(temp);
        Log.d(TAG, "refresh: saveAll() done.");
        datas.clear();
        datas.addAll(temp);
        notifyAllObserver();
    }

}
