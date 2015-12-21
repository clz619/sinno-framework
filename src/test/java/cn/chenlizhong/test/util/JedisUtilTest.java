package cn.chenlizhong.test.util;

import cn.chenlizhong.util.JedisUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : lizhong.chen
 * @version : 1.0
 *          Descr : Jedis test
 * @since : 15/12/22 上午12:04
 */
public class JedisUtilTest {

    private static final Logger LOG = LoggerFactory.getLogger(JedisUtilTest.class);
    private Jedis jedis;

    @Before
    public void init() {
        String host = "sinno01";
        int port = 6379;
        String password = "clz619";
        jedis = JedisUtil.getJedis(host, port, password);
    }

    /**
     * test key
     */
    @Test
    public void testKey() {

        //值
        jedis.set("msg", "hello word1");

        LOG.info("key:msg,value:" + jedis.get("msg"));
        //Assert.assertEquals(jedis.get("msg"), "hello word");

        //查询一个key是否存在
        LOG.info("Is redis has key:msg = " + String.valueOf(jedis.exists("msg")));

        //设置key的过期时间
        jedis.expire("msg", 1);
        LOG.info("exprie key:msg,ex:1");
        try {
            //Thread.sleep(800l);
            //获取key的有效时间（单位：秒）
            LOG.info("ttl key:msg," + jedis.ttl("msg"));
            LOG.info("pttl key:msg," + jedis.pttl("msg"));
            //获取key的存储类型
            LOG.info("key:msg,type:" + jedis.type("msg"));
            //移除key的过期时间
            jedis.persist("msg");
            Thread.sleep(1100l);
            LOG.info("Thread sleep 1.1s");
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("key:msg,value:" + jedis.get("msg"));

        LOG.info("rename oldkey:msg,newkey:newmsg");
        //重命名
        //jedis.rename("msg", "newmsg");
        jedis.renamenx("msg", "newmsg");

        LOG.info("key:newmsg,value:" + jedis.get("newmsg"));
        //设置带有有效期的值
        jedis.setex("exm", 1, "你好");
        LOG.info("setx key:exm,ex:1,value:你好");
        LOG.info("key:exm,value:" + jedis.get("exm"));
        Assert.assertEquals(jedis.get("exm"), "你好");
        try {
            Thread.sleep(1100l);
            LOG.info("Thread sleep 1.1s");
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("key:exm,value:" + jedis.get("exm"));
        Assert.assertEquals(jedis.get("exm"), null);

        for (int i = 0; i < 3; i++) {
            //随机获得一个key
            LOG.info("randomKey key:" + jedis.randomKey());
        }

        //增量迭代key
        ScanResult<String> result = jedis.scan("0");
        List<String> scanResult = result.getResult();
        if (CollectionUtils.isNotEmpty(scanResult)) {
            LOG.info("scan result:");
            for (String r : scanResult) {
                LOG.info(r);
            }
        }
    }

    @Test
    public void testString() {
        //追加一个值到key上
        jedis.append("msg", "a.");
        LOG.info("get key:msg,value:" + jedis.get("msg"));

        LOG.info("bitcount key:msg,bitcount:" + jedis.bitcount("msg"));

        LOG.info("getrange 1 5 key:msg,value:" + jedis.getrange("msg", 1, 5));

        //设置一个值,只有当该键不存在
        jedis.setnx("msg", "asdsd");
        LOG.info("setnx key:msg,value:asdsd");
        LOG.info("get key:msg,value:" + jedis.get("msg"));

        jedis.set("num", "10086");
        LOG.info("set key:num,value:10086");
        LOG.info("get key:num,value:" + jedis.get("num"));
        //整数原子减1
        jedis.decr("num");
        LOG.info("decr key:num");
        LOG.info("get key:num,value:" + jedis.get("num"));
        //原子减指定的整数
        jedis.decrBy("num", 86);
        LOG.info("decrBy 86 key:num");
        LOG.info("get key:num,value:" + jedis.get("num"));

        //执行原子加1
        jedis.incr("num");
        LOG.info("incr key:num");
        LOG.info("get key:num,value:" + jedis.get("num"));

        //执行原子增加一个整数
        jedis.incrBy("num", 11);
        LOG.info("incrBy 11 key:num");
        LOG.info("get key:num,value:" + jedis.get("num"));

        //获取指定key值的长度
        LOG.info("strlen key:num,value:" + jedis.strlen("num"));

        //设置多个key value
        jedis.mset("msga", "aaa", "msgb", "bbb");
        LOG.info("mset key:msga,value:aaa;key:msgb,value:bbb");
    }

    /**
     * 测试hash
     */
    @Test
    public void testHash() {
        //设置hash book里面的一个字段的值,author
        jedis.hset("book", "author", "lz.chen");
        LOG.info("hset book author li.chen");
        LOG.info("hget book author value:" + jedis.hget("book", "author"));

        Map<String, String> book = Maps.newHashMap();
        book.put("pagesise", "324");
        book.put("curpage", "243");
        jedis.hmset("book", book);

        Map<String, String> b = jedis.hgetAll("book");

        Set<String> keys = b.keySet();
        for (String key : keys) {
            LOG.info("key:" + key + ",value:" + b.get(key));
        }
    }

    @After
    public void destroy() {
        JedisUtil.closeJedis(jedis);
    }
}
