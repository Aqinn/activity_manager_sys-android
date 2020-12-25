package com.aqinn.actmanagersysandroid.data.show;

import com.aqinn.actmanagersysandroid.ShowManager;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Observer;
import com.aqinn.actmanagersysandroid.entity.Act;
import com.aqinn.actmanagersysandroid.entity.Attend;
import com.aqinn.actmanagersysandroid.entity.show.CreateAttendIntroItem;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aqinn
 * @date 2020/12/18 3:40 PM
 */
public class DataSourceCreateAttendIntroItem extends DataSource<CreateAttendIntroItem> {


    public DataSourceCreateAttendIntroItem() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }


    private void initData() {
        // 我创建的签到
        List<CreateAttendIntroItem> createAttendIntroItemList = LitePal.findAll(CreateAttendIntroItem.class);
        datas.addAll(createAttendIntroItemList);
    }

}
