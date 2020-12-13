package com.aqinn.actmanagersysandroid.data;

import android.util.Log;

import java.util.ArrayList;

/**
 * 数据中心 - 提供所有测试数据 - 仅展示用
 * @author Aqinn
 * @date 2020/12/12 3:57 PM
 */
public class DataCenter {

    private static final String TAG = "DataCenter";

    private static ArrayList<ActIntroItem> allActIntroItemICreate;
    private static ArrayList<ActIntroItem> allActIntroItemIParticipate;
    private static ArrayList<ParticipateAttendIntroItem> allParticipateAttendIntroItem;
    private static ArrayList<CreateAttendIntroItem> allCreateAttendIntroItem;
    private static ArrayList<UserDesc> allUserDesc;

    // 初始化 活动数据
    static {
        // 我创建的活动
        allActIntroItemICreate = new ArrayList<ActIntroItem>();
        allActIntroItemICreate.add(new ActIntroItem("测试创建活动名称1", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        allActIntroItemICreate.add(new ActIntroItem("测试创建活动名称2", "10:00 - 23:00", "海华六栋 B308", "测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。测试一下长度。", "未开始"));
        allActIntroItemICreate.add(new ActIntroItem("测试创建活动名称3", "01:00 - 13:00", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        allActIntroItemICreate.add(new ActIntroItem("测试创建活动名称4", "02:00 - 20:59", "海华六栋 B307", "没啥的，就是测试一下", "未开始"));
        allActIntroItemICreate.add(new ActIntroItem("测试创建活动名称5", "10:00 - 21:50", "海华六栋 B307", "没啥的，就是测试一下", "已结束"));

        // 我参与的活动
        allActIntroItemIParticipate = new ArrayList<ActIntroItem>();
        allActIntroItemIParticipate.add(new ActIntroItem("测试参与活动名称1", "10:00 - 21:50", "海华六栋 B307", "没啥的，就是测试一下", "已结束"));
        allActIntroItemIParticipate.add(new ActIntroItem("测试参与活动名称2", "01:00 - 13:00", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        allActIntroItemIParticipate.add(new ActIntroItem("测试参与活动名称3", "01:00 - 13:00", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        allActIntroItemIParticipate.add(new ActIntroItem("测试参与活动名称4", "10:00 - 21:50", "海华六栋 B307", "没啥的，就是测试一下", "已结束"));
        allActIntroItemIParticipate.add(new ActIntroItem("测试参与活动名称5", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        allActIntroItemIParticipate.add(new ActIntroItem("测试参与活动名称6", "14:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        allActIntroItemIParticipate.add(new ActIntroItem("测试参与活动名称7", "06:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", "未开始"));
        allActIntroItemIParticipate.add(new ActIntroItem("测试参与活动名称8", "07:00 - 08:00", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        allActIntroItemIParticipate.add(new ActIntroItem("测试参与活动名称9", "07:00 - 09:00", "海华六栋 B307", "没啥的，就是测试一下", "已结束"));
        allActIntroItemIParticipate.add(new ActIntroItem("测试参与活动名称10", "10:10 - 20:20", "海华六栋 B307", "没啥的，就是测试一下", "已结束"));
        allActIntroItemIParticipate.add(new ActIntroItem("测试参与活动名称11", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
        allActIntroItemIParticipate.add(new ActIntroItem("测试参与活动名称12", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", "未开始"));
        allActIntroItemIParticipate.add(new ActIntroItem("测试参与活动名称13", "00:00 - 23:59", "海华六栋 B307", "没啥的，就是测试一下", "进行中"));
    }

    // 初始化 签到数据
    static {
        // 我创建的签到
        allCreateAttendIntroItem = new ArrayList<CreateAttendIntroItem>();
        allCreateAttendIntroItem.add(new CreateAttendIntroItem("测试创建签到的活动名称1", "00:00-23:59", "视频签到, 自助签到", "未开始", "100", "50", "50"));
        allCreateAttendIntroItem.add(new CreateAttendIntroItem("测试创建签到的活动名称2", "00:00-23:59", "自助签到", "未开始", "100", "50", "50"));
        allCreateAttendIntroItem.add(new CreateAttendIntroItem("测试创建签到的活动名称3", "00:00-23:59", "视频签到, 自助签到", "已结束", "100", "50", "50"));
        allCreateAttendIntroItem.add(new CreateAttendIntroItem("测试创建签到的活动名称4", "00:00-23:59", "视频签到", "进行中", "100", "50", "50"));

        // 我参与的签到
        allParticipateAttendIntroItem = new ArrayList<ParticipateAttendIntroItem>();
        allParticipateAttendIntroItem.add(new ParticipateAttendIntroItem("测试参与签到的活动名称1", "00:00-23:59", "视频签到, 自助签到", "未签到", "已结束"));
        allParticipateAttendIntroItem.add(new ParticipateAttendIntroItem("测试参与签到的活动名称2", "00:00-23:59", "视频签到", "已签到", "进行中"));
        allParticipateAttendIntroItem.add(new ParticipateAttendIntroItem("测试参与签到的活动名称3", "00:00-23:59", "视频签到, 自助签到", "已签到", "已结束"));
        allParticipateAttendIntroItem.add(new ParticipateAttendIntroItem("测试参与签到的活动名称4", "00:00-23:59", "自助签到", "未签到", "未开始"));
        allParticipateAttendIntroItem.add(new ParticipateAttendIntroItem("测试参与签到的活动名称5", "00:00-23:59", "自助签到", "已签到", "进行中"));
        allParticipateAttendIntroItem.add(new ParticipateAttendIntroItem("测试参与签到的活动名称6", "00:00-23:59", "视频签到, 自助签到", "未签到", "未开始"));
    }

    // 初始化 用户数据
    static {
        allUserDesc = new ArrayList<UserDesc>();
        allUserDesc.add(new UserDesc("_aqinn", "钟兆锋", "男", "13192205220", "侧田（Justin Lo），1976年7月1日出生于美国纽约，香港歌手、音乐人。" +
                "2003年，侧田被音乐人雷颂德发掘；2005年推出专辑《Justin》，" +
                "年底获得香港四大传媒音乐颁奖礼中所有新人男歌手的奖项；2006年3月推出第二张个人专辑《No Protection》，" +
                "同年获得2006年度十大劲歌金曲颁奖典礼杰出表现金奖；2007年，推出第三张个人专辑《JTV》，" +
                "并凭《男人KTV》在2007年度香港的四大音乐颁奖典礼中，襄括所有十大歌曲奖（其一）；" +
                "2009年，参演电影《保持爱你》及创作及演唱插曲《无言无语》，" +
                "同年推出个人首张精选大碟《From JUSTIN － Collection of His First 3 years》；" +
                "2010年，推出个人粤语专辑《我没有变过 爱的习惯》；2012年11月，推出国语创作单曲《很想很想说再见》；" +
                "2014年为TVB剧集《使徒行者》演唱主题曲《行者》。\n" +
                "代表作有《美丽之最》《好人》《情歌》《Kong》《三十日》《男人KTV》《命硬》《很想很想说再见》。"));
    }

    public static ArrayList<ActIntroItem> getAllActIntroItemICreate() {
        Log.d(TAG, "getAllActIntroItemICreate: 当前我创建的活动数据条数为 " + allActIntroItemICreate.size());
        return allActIntroItemICreate;
    }

    public static ArrayList<ActIntroItem> getAllActIntroItemIParticipate() {
        Log.d(TAG, "getAllActIntroItemIParticipate: 当前我参与的活动数据条数为 " + allActIntroItemIParticipate.size());
        return allActIntroItemIParticipate;
    }

    public static ArrayList<CreateAttendIntroItem> getAllCreateAttendIntroItem() {
        Log.d(TAG, "getAllCreateAttendIntroItem: 当前我创建的活动数据条数为 " + allCreateAttendIntroItem.size());
        return allCreateAttendIntroItem;
    }

    public static ArrayList<ParticipateAttendIntroItem> getAllParticipateAttendIntroItem() {
        Log.d(TAG, "getAllParticipateAttendIntroItem: 当前我参与的签到数据条数为 " + allParticipateAttendIntroItem.size());
        return allParticipateAttendIntroItem;
    }

    public static ArrayList<UserDesc> getAllUserDesc() {
        Log.d(TAG, "getAllUserDesc: 当前用户数据条数为 " + allUserDesc.size());
        return allUserDesc;
    }

}