package com.jn.redistudy.spdbrobot.redisentity;

import com.jn.redistudy.spdbrobot.enumtype.SupportedListType;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Lee on 2018/5/2 11:18.
 */
public class RobotData {



    /** KEYTYPE和对应URL PATTERN的枚举类型*/
    private final static HashMap<Enum<SupportedListType>, String> URLPATTERN = new HashMap() {{
        put(SupportedListType.SPDB, "https://weixin.spdbccc.com.cn/wxrp-page-redpacketsharepage/newShare.*");
        put(SupportedListType.DIDI, "https://didi");
        put(SupportedListType.DIANPING, "https://dazhongdianping");
        put(SupportedListType.OFO, "https://ofo-misc.ofo.com/regular_packet/.*");
        put(SupportedListType.NOTSUPPORTED, "");
    }};

    /** 保存list的key*/
    private String listkey;

    /** 用户id*/
    private String openid;

    /** 可分享次数*/
    private Integer count;

    /** 过期时间,采用秒数保存*/
    private long time2expire;

    /** 分享url*/
    private String url;

    /** 生成的URL类型*/
    private Enum<SupportedListType> urltype;


    //返回URL匹配的KEYTYPE
    public Enum<SupportedListType> getUrlType(String url) {
        Enum<SupportedListType> listtype = SupportedListType.NOTSUPPORTED;

        for (Map.Entry<Enum<SupportedListType>, String> entry : URLPATTERN.entrySet()) {
            if(Pattern.matches(entry.getValue(), url)) {
                listtype = entry.getKey();
            }
        }

        return listtype;
    }

    public RobotData(String openid, Integer count, long time2expire, String url) {
        this.openid = openid;
        this.count = count;
        this.time2expire = time2expire;
        this.url = url;

        this.urltype = getUrlType(url);
        this.listkey = this.urltype.toString();
    }


    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public long getTime2expire() {
        return time2expire;
    }

    public void setTime2expire(long time2expire) {
        this.time2expire = time2expire;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getListkey() {
        return listkey;
    }

    public void setListkey(String listkey) {
        this.listkey = listkey;
    }
}
