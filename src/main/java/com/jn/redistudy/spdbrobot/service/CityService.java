package com.jn.redistudy.spdbrobot.service;

import com.jn.redistudy.spdbrobot.constants.Const;
import com.jn.redistudy.spdbrobot.enumtype.SupportedListType;
import com.jn.redistudy.spdbrobot.redisentity.RobotData;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lee on 2018/4/27 9:07.
 */
@Service
public class CityService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /** 按时间顺序，保存SPDB的队列*/
    private ListOperations<String, Object> spdblist;

    /** 按时间顺序，保存OFO的队列*/
    private ListOperations<String, Object> ofolist;


    /** 保存用户分享信息的哈希表*/
    private HashOperations<String, String, String> userhash;

    @PostConstruct
    private void init() {
        spdblist = redisTemplate.opsForList();
        ofolist = redisTemplate.opsForList();
        userhash = redisTemplate.opsForHash();
    }


    /**
     *
     * @param openid    用户id
     * @param url       分享url
     * @param count     可分享次数
     * @return
     */
    public boolean savetoredis(String openid, String url, Integer count) {
        RobotData RobotData = new RobotData(openid, count, Const.MAX_ALIVE_TIME, url);

        //计算过期时间
        long second = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        second = second + Const.MAX_ALIVE_TIME;

        if(RobotData.getUrlType(url) == SupportedListType.SPDB) {
            spdblist.rightPush(RobotData.getListkey(), openid);
            userhash.put("user:" + openid, "spdbcount", String.valueOf(count));
            userhash.put("user:" + openid, "spdbtime2expire", String.valueOf(second));
            userhash.put("user:" + openid, "spdburl", String.valueOf(url));
        } else if(RobotData.getUrlType(url) == SupportedListType.OFO) {
            ofolist.rightPush(RobotData.getListkey(), openid);
            userhash.put("user:" + openid, "ofocount", String.valueOf(count));
            userhash.put("user:" + openid, "ofotime2expire", String.valueOf(second));
            userhash.put("user:" + openid, "ofourl", String.valueOf(url));
        } else {
                return false;
        }

        return true;
    }

    public List<HashMap<String, String>> getfromredis(String openid, Enum<SupportedListType> keytype) {
        if(keytype == SupportedListType.SPDB) {
            List<String> listchoice = new ArrayList<String>();
            long l = 0;
            Integer count = 0;

            //获取符合条件和数量的openid
            while (count<Const.RETURN_URL_NUM) {
                //获取当前时间，计算是否过期

                Object temp = spdblist.index(SupportedListType.SPDB.toString(), l);
                if(temp == null) break;
                else if(!temp.toString().equals(openid)) {
                    listchoice.add(temp.toString());
                    count++;
                }
                l++;
            }

            //根据上面获取的openid获取用户信息
            //判断是否过期，过期则删除
            //对次数进行减一，为0 则删除
            //否则对次数减一，构造返回Url列表
            List<HashMap<String, String>> listreturn = new ArrayList<HashMap<String, String>>();

            for(String stropenid:listchoice) {
                String key = "user:"+stropenid;
                String hashkey1 = "spdbcount";
                String hashkey2 = "spdbtime2expire";
                String hashkey3 = "spdburl";

                String hashvalue1 = userhash.get(key, hashkey1);
                String hashvalue2 = userhash.get(key, hashkey2);
                String hashvalue3 = userhash.get(key, hashkey3);
                boolean delflag = false;

                long current = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));

                if(Long.parseLong(hashvalue2) <= current) {
                    //从listchoice中删除，从redis list和hashmap中删除
                    delflag = true;
                }
                if(Integer.parseInt(hashvalue1) == 1) {
                    delflag = true;
                }
                HashMap<String, String> ret = new HashMap<String, String>();
                ret.put(stropenid, hashvalue3);
                listreturn.add(ret);

                if(delflag) {
                    userhash.delete(key, hashkey1);
                    userhash.delete(key, hashkey2);
                    userhash.delete(key, hashkey3);

                    spdblist.remove(SupportedListType.SPDB.toString(), 1, stropenid);
                }


            }

            return listreturn;


        } else if(keytype == SupportedListType.OFO) {
            List<String> listchoice = new ArrayList<String>();
            long l = 0;
            Integer count = 0;

            //获取符合条件和数量的openid
            while (count<Const.RETURN_URL_NUM) {
                //获取当前时间，计算是否过期

                String temp = ofolist.index(SupportedListType.OFO.toString(), l).toString();
                if(temp == null) break;
                else if(!temp.equals(openid)) {
                    listchoice.add(temp);
                    count++;
                }
                l++;
            }

            //根据上面获取的openid获取用户信息
            //判断是否过期，过期则删除
            //对次数进行减一，为0 则删除
            //否则对次数减一，构造返回Url列表
            List<HashMap<String, String>> listreturn = new ArrayList<HashMap<String, String>>();

            for(String stropenid:listchoice) {
                String key = "user:"+stropenid;
                String hashkey1 = "ofocount";
                String hashkey2 = "ofotime2expire";
                String hashkey3 = "ofourl";

                String hashvalue1 = userhash.get(key, hashkey1);
                String hashvalue2 = userhash.get(key, hashkey2);
                String hashvalue3 = userhash.get(key, hashkey3);
                boolean delflag = false;

                long current = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));

                if(Long.parseLong(hashvalue2) <= current) {
                    //从listchoice中删除，从redis list和hashmap中删除
                    delflag = true;
                }
                if(Integer.parseInt(hashvalue1) == 1) {
                    delflag = true;
                }
                HashMap<String, String> ret = new HashMap<String, String>();
                ret.put(stropenid, hashvalue3);
                listreturn.add(ret);

                if(delflag) {
                    userhash.delete(key, hashkey1);
                    userhash.delete(key, hashkey2);
                    userhash.delete(key, hashkey3);

                    ofolist.remove(SupportedListType.OFO.toString(), 1, stropenid);
                }


            }

            return listreturn;

        } else {
            return null;

        }

    }

}
