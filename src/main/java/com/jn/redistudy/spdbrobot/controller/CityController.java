package com.jn.redistudy.spdbrobot.controller;

import com.jn.redistudy.spdbrobot.enumtype.SupportedListType;
import com.jn.redistudy.spdbrobot.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lee on 2018/4/27 9:35.
 */
@RestController
public class CityController {

    @Autowired
    private CityService cityService;

    /**
     * 服务基本流程是：接收到发送时，首先获取用户的身份信息，然后将身份信息和URL一起保存到LIST
     * ，同时从LIST中返回5个非本人，生成对应页面，返回给客户端
     * 缺省业务逻辑：
     * 1、不能超过15个；
     * 2、发送过去的可能有问题，需要校验格式、判断过期
     * 3、需要区分链接的类型，相同类型的链接不应该分享到不同类型的用户
     * 数据基本的基本想法：
     * 1、将同一类型的放在一个ZSET中，SET中包含openid,url，次数，ZSET的SCORE为过期的时间
     * 2、分享URL应该存在过期时间和分享次数，分享一次减一，超出时间或者次数 则去除URL
     *
     * 数据结构要重新考虑一下：
     * 1、考虑更改为hashmap,openid作为 key,value为hashmap,保存类型和次数:URL:过期时间
     *
     * 数据结构重新进行调整：
     * 多个List，不同的类型进入不同的list, 按照先进先出的原则排列，用以保存用户发送的顺序，key 为 队列类型, value 为 openid
     * 一个HashMap, 用以保存用户发送的信息，不同list的信息都保存于此，key为user:openid，hashmap的key为 spdb分享次数、spdb过期时间和spdbURL
     *
     * 传入的参数可能不变，部分参数需要从url中解析出来
     * */

    @RequestMapping("/save")
    public void save() {
        boolean ret = cityService.savetoredis("2345555l", "https://weixin.spdbccc.com.cn/wxrp-page-redpacketsharepage/newShare?packetId=Q8DCZM22TQ66NPF41086512981-1524894879000ff255453&noCheck=1&hash=75&dataDt=20180420", 36);

        cityService.savetoredis("23455552", "https://weixin.spdbccc.com.cn/wxrp-page-redpacketsharepage/newShare?packetId=Q8DCZM22TQ66NPF41086512981-1524894879000ff255453&noCheck=1&hash=75&dataDt=20180420", 24);

        cityService.savetoredis("23455553", "https://weixin.spdbccc.com.cn/wxrp-page-redpacketsharepage/newShare?packetId=Q8DCZM22TQ66NPF41086512981-1524894879000ff255453&noCheck=1&hash=75&dataDt=20180420", 20);

        cityService.savetoredis("23455553", "https://ofo-misc.ofo.com/regular_packet/index.html#/?random=https://img.ofo.so/cms/7d0ed865c419f1926a729e0671ca0fe8.jpg,#1412102801/8d25cefa3b1d1063d70fa497ffdf7e80d1f8b3cb9c6b09b96409d9c97a1f1843c9dda78f7bd830dc5c627426e33fe1062954b41cdeb17ea7815a23c029c8633b696f08952529908a5e84e82c5a170356", 8);

        cityService.savetoredis("23455553", "https://ofo-misc.ofo.com/regular_packet/index.html#/?random=https://img.ofo.so/cms/7d0ed865c419f1926a729e0671ca0fe8.jpg,#1412102801/8d25cefa3b1d1063d70fa497ffdf7e80d1f8b3cb9c6b09b96409d9c97a1f1843c9dda78f7bd830dc5c627426e33fe1062954b41cdeb17ea7815a23c029c8633b696f08952529908a5e84e82c5a170356", 8);

        cityService.savetoredis("23455553", "https://study.ef.cn/regular_packet/index.html#/?random=https://img.ofo.so/cms/7d0ed865c419f1926a729e0671ca0fe8.jpg,#1412102801/8d25cefa3b1d1063d70fa497ffdf7e80d1f8b3cb9c6b09b96409d9c97a1f1843c9dda78", 8);


    }

    @RequestMapping("/get")
    public List<HashMap<String, String>> get() {
        List<HashMap<String, String>> listurl = cityService.getfromredis("23455553", SupportedListType.SPDB);
        return listurl;
    }
}
