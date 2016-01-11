package cn.chenlizhong.test.util;

import cn.chenlizhong.sinno.util.JedisUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;

import java.util.Iterator;
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

    //    @Before
    public void init() {
        String host = "sinno01";
        int port = 6379;
        String password = "";
        jedis = JedisUtil.getJedis(host, port, password);
    }

    /**
     * test key
     */
//    @Test
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

    //    @Test
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
     * 测试hashs
     */
//    @Test
    public void testHashs() {

        //设置hash集book域author的值
        jedis.hset("book", "author", "lz.chen");
        LOG.info("hset book author li.chen");
        //设置hash集book域author的值
        LOG.info("hget book author value:" + jedis.hget("book", "author"));

        Map<String, String> book = Maps.newHashMap();
        book.put("pagesize", "324.87");
        book.put("curpage", "243");
        //设置hash集book的2个域的值(pagesize,cuspage)
        jedis.hmset("book", book);

        //获取hash集所有的域和值
        Map<String, String> b = jedis.hgetAll("book");

        Set<String> keys = b.keySet();
        for (String key : keys) {
            LOG.info("key:" + key + ",value:" + b.get(key));
        }

        //将哈希集中指定的域的值增加给定的数字
        jedis.hincrBy("book", "curpage", 11);

        LOG.info("after hincrBy book pagesize 11 value:" + jedis.hget("book", "curpage"));

        //将哈希集中指定的域的值增加给定的浮点数
        jedis.hincrByFloat("book", "pagesize", 11.11);

        LOG.info("after hincrByFloat book pagesize 11.11 value:" + jedis.hget("book", "pagesize"));

        //判定给定域是否存在哈希集中
        LOG.info("book's pagesize is exist:" + jedis.hexists("book", "pagesize"));

        //获取hash里所有字段的数量


        LOG.info("get book pagesize value:" + jedis.hget("book", "pagesise"));

        LOG.info("get hash book's length:" + jedis.hlen("book"));
        //获取hash里的所有字段
        keys = jedis.hkeys("book");

        LOG.info("get hash book's key...begin.");

        for (String key : keys) {
            LOG.info("key:" + key + ",value:" + jedis.hget("book", key));
        }
        LOG.info("get hash book's key...end.");


        LOG.info("get hash book's value:");
        //获取hash集合book的所有值
        List<String> vals = jedis.hvals("book");

        for (String val : vals) {
            LOG.info(val);
        }

    }

    /**
     * 测试list
     */
//    @Test
    public void testLists() {

        //修剪到指定范围内的清单
        jedis.ltrim("citys", 0, 1);

        //从队列的右边入队一个元素
        jedis.rpush("citys", "hangzhou", "qingdao", "beijing");


        LOG.info("rpush citys hangzhou qingdao", "beijing");

        //从列表中获取指定返回的元素
        List<String> citys = jedis.lrange("citys", 0, -1);

        LOG.info("lrange citys 0 -1:");

        for (String city : citys) {
            LOG.info(city);
        }


        LOG.info("lrem citys 1 hangzhou");
        //删除跟value相等的length个值
        jedis.lrem("citys", 1, "hangzhou");


        //获取队列的长度
        LOG.info("citys llen:" + jedis.llen("citys"));

        //从队列的右边出队一个元素
        String rpopValue = jedis.rpop("citys");

        LOG.info("rpop citys:" + rpopValue);

        //从队列的左边出队一个元素
        String lpopValue = jedis.lpop("citys");

        LOG.info("lpop citys:" + lpopValue);

        LOG.info("rpoplpush citys to newcitys");

        jedis.rpoplpush("citys", "newcitys");

        LOG.info("lindex newcitys 0 value:" + jedis.lindex("newcitys", 0));

        //当队列存在时,从队到左边入队一个元素
        jedis.lpushx("newcitys", "hhhhh");
        LOG.info("lindex newcitys 0 value:" + jedis.lindex("newcitys", 0));

        LOG.info("llen newcitys :" + jedis.llen("newcitys"));
    }

    /**
     * 测试集合sets
     */
//    @Test
    public void testSets() {
        //添加一个或多个元素到集合
        jedis.sadd("class102", "小明", "小白");
        jedis.sadd("class102", "小红", "小白");

        LOG.info("sadd class102 小明 小白");
        LOG.info("sadd class102 小红 小白");

        //获取集合里面的元素数量
        //scard
        LOG.info("scard class102 :" + jedis.scard("class102"));

        //添加一个或多个元素到集合
        jedis.sadd("class306", "小明", "小红");

        jedis.sadd("class307", "小明", "小白");

        LOG.info("sadd class306 小明 小红");

        LOG.info("sadd class307 小明 小白");

        //获取集合里面的元素数量
        //scard
        LOG.info("scard class306 :" + jedis.scard("class306"));

        //获取队列中不存在的元素  class102 中 class306和class307的合集中不存在的元素
        Set<String> diffSets = jedis.sdiff("class102", "class306", "class307");

        if (diffSets.size() > 0) {
            LOG.info(diffSets.iterator().next());
        }

        Long diffSize = jedis.sdiffstore("diffclass306and307", "class306", "class307");
        LOG.info("sdiffstore class306 class307 to diffclass306and307 size:" + diffSize);

        LOG.info("sismember diffclass306and307 小红:" + jedis.sismember("diffclass306and307", "小红"));

        Set<String> interClass67 = jedis.sinter("class306", "class307");

        LOG.info("sinter class306 class307:");
        Iterator<String> it = interClass67.iterator();
        while (it.hasNext()) {
            LOG.info(it.next());
        }

        Long sinterSize = jedis.sinterstore("sinterclass306andclass307", "class306", "class307");
        LOG.info("sinterstore class306 class307 size:" + sinterSize);

        LOG.info("sismember sinterclass306andclass307 小明:" + jedis.sismember("sinterclass306andclass307", "小明"));

        jedis.expire("sinterclass306andclass307", 1);

        try {
            Thread.sleep(1001l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Set<String> sst = jedis.smembers("sinterclass306andclass307");

        LOG.info("sinter class306 class307 size:" + sst.size());
        it = sst.iterator();
        while (it.hasNext()) {
            LOG.info(it.next());
        }


        LOG.info("sadd class a c d b");
        jedis.sadd("class", "a", "c", "d", "b");

        Set<String> sets = jedis.smembers("class");
        it = sets.iterator();
        while (it.hasNext()) {
            LOG.info(it.next());
        }

    }

    //    @Test
    public void testSortedSets() {
        jedis.zadd("zset", 1, "a");
        jedis.zadd("zset", 4, "d");
        jedis.zadd("zset", 3, "c");
        jedis.zadd("zset", 2, "b");

        Set<String> zset = jedis.zrange("zset", 0, -1);

        Iterator<String> it = zset.iterator();
        while (it.hasNext()) {
            LOG.info(it.next());
        }

        //返回有序集key中成员member的排名，其中有序集成员按score值从大到小排列。排名以0为底，也就是说，score值最大的成员排名为0。
        //使用ZRANK命令可以获得成员按score值递增(从小到大)排列的排名。
        LOG.info("zrevrank zset a :" + jedis.zrevrank("zset", "a"));
        ;
        LOG.info("zrank zset a :" + jedis.zrank("zset", "a"));
        ;
    }

    //    @After
    public void destroy() {
        JedisUtil.closeJedis(jedis);
    }
}
