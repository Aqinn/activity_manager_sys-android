package com.aqinn.actmanagersysandroid.datafortest;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author Aqinn
 * @date 2020/12/18 3:42 PM
 */
public class DataSourceUserDescTest extends DataSource<UserDesc> {

    public DataSourceUserDescTest() {
        observers = new ArrayList<>();
        datas = new ArrayList<>();
        initData();
    }

    private void initData() {
        datas.add(new UserDesc("_aqinn", "钟兆锋", "男", "13192205220", "侧田（Justin Lo），1976年7月1日出生于美国纽约，香港歌手、音乐人。" +
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

}
