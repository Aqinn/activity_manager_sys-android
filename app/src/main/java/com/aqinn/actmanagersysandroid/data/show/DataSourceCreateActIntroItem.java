package com.aqinn.actmanagersysandroid.data.show;

import android.util.Log;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Refreshable;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;
import com.aqinn.actmanagersysandroid.utils.CommonUtils;

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
        List<ActIntroItem> actIntroItemList = LitePal.where("ownerId = ?", String.valueOf(CommonUtils.getNowUserIdFromSP(MyApplication.getContext()))).find(ActIntroItem.class);
        String nowUserAccount = CommonUtils.getNowUsernameFromSP(MyApplication.getContext());
        for (ActIntroItem aii:actIntroItemList) {
            if (nowUserAccount.equals(aii.getCreator())) {
                datas.add(aii);
            }
        }
    }

    @Override
    public void refresh(Object o) {
        List<ActIntroItem> actIntroItemList = (List<ActIntroItem>) o;
        Log.d(TAG, "refresh: actIntroItemList" + actIntroItemList);
        LitePal.deleteAll(ActIntroItem.class, "ownerId = ?", String.valueOf(CommonUtils.getNowUserIdFromSP(MyApplication.getContext())));
        List<ActIntroItem> temp = new ArrayList<>();
        String nowUserAccount = CommonUtils.getNowUsernameFromSP(MyApplication.getContext());
        for (ActIntroItem aii:actIntroItemList) {
            if (nowUserAccount.equals(aii.getCreator())) {
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
