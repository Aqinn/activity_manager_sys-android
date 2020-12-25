package com.aqinn.actmanagersysandroid.components;

import com.aqinn.actmanagersysandroid.modules.RetrofitServiceModule;
import com.aqinn.actmanagersysandroid.scopes.RetrofitServiceScope;
import com.aqinn.actmanagersysandroid.service.ActService;
import com.aqinn.actmanagersysandroid.service.AttendService;
import com.aqinn.actmanagersysandroid.service.ShowItemService;
import com.aqinn.actmanagersysandroid.service.UserActService;
import com.aqinn.actmanagersysandroid.service.UserAttendService;
import com.aqinn.actmanagersysandroid.service.UserService;
import com.aqinn.actmanagersysandroid.utils.RetrofitUtils;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Aqinn
 * @date 2020/12/25 3:13 PM
 */
@Component(modules = RetrofitServiceModule.class)
public
interface RetrofitServiceComponent {

    UserService getUserService();

    ActService getActService();

    AttendService getAttendService();

    UserActService getUserActService();

    UserAttendService getUserAttendService();

    ShowItemService getShowItemService();

}
