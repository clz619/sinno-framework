package cn.chenlizhong.util;

import com.google.common.collect.Maps;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

import java.util.Map;

/**
 * @author : lizhong.chen
 * @version : 1.0
 *          Descr : redis utils
 * @since : 15/12/21 下午10:42
 */
public final class JedisUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JedisUtil.class);

    private static Map<String, JedisPool> jedisPoolMap = Maps.newTreeMap();

    private static final int RETRY_NUM = 3;

    private static JedisPool getPool(String host, int port, String password) {
        String key = host + ":" + port;
        JedisPool pool = null;

        if (jedisPoolMap.containsKey(key)) {
            pool = jedisPoolMap.get(key);
        } else {
            try {
                pool = new JedisPool(new GenericObjectPoolConfig(), host, port, Protocol.DEFAULT_DATABASE, password);
                jedisPoolMap.put(key, pool);
                LOG.info("get (host:" + host + ",port:" + port + ") redis pool success");
            } catch (Exception e) {
                LOG.error("get (host:" + host + ",port:" + port + ") redis pool err", e);
            }
        }
        return pool;
    }

    public static void destroyPool(String host, int port) {
        String key = host + ":" + port;
        if (jedisPoolMap.containsKey(key)) {
            try {
                JedisPool pool = jedisPoolMap.get(key);
                jedisPoolMap.remove(key);
                pool.destroy();
            } catch (Exception e) {
                LOG.error("destroy (host:" + host + ",port:" + port + ") redis pool err", e);
            }
        }
    }

    public static Jedis getJedis(String host, int port, String password) {
        Jedis jedis = null;
        int count = 0;
        do {
            try {
                jedis = getPool(host, port, password).getResource();
            } catch (Exception e) {
                LOG.error("get (host:" + host + ",port:" + port + ") redis pool err", e);
            }
            count++;
        } while (null == jedis && count < RETRY_NUM);
        return jedis;
    }

    public static void closeJedis(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }
}
