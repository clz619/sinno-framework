package cn.chenlizhong.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author : lizhong.chen
 * @version : 1.0
 *          Descr : 属性文件工具类
 * @since : 15/12/20 下午5:12
 */
public final class PropsUtil {
    private static final Logger LOG = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     *
     * @param fileName
     * @return
     */
    public static Properties loadProps(String fileName) {
        Properties props = null;
        InputStream is = null;

        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (null == is) {
                throw new FileNotFoundException(fileName + " file is not found");
            }
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return props;
    }

    /**
     * 获取字符型属性(默认值为空字符串)
     *
     * @param props
     * @param key
     * @return
     */
    public static String getString(Properties props, String key) {
        return getString(props, key, "");
    }

    /**
     * 获取字符型属性(可指定默认值)
     *
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Properties props, String key, String defaultValue) {
        String val = defaultValue;
        if (props.contains(key)) {
            val = props.getProperty(key);
        }
        return val;
    }

    /**
     * 获取数值型属性(默认值为0)
     *
     * @param props
     * @param key
     * @return
     */
    public static int getInt(Properties props, String key) {
        return getInt(props, key, 0);
    }

    /**
     * 获取数值型属性(可指定默认值)
     *
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Properties props, String key, int defaultValue) {
        int val = defaultValue;
        if (props.contains(key)) {
            val = CaseUtil.caseInt(props.getProperty(key));
        }
        return val;
    }

    /**
     * 获取布尔型属性(默认值为false)
     *
     * @param props
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties props, String key) {
        return getBoolean(props, key, false);
    }

    /**
     * 获取布尔型属性(可指定默认值)
     *
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Properties props, String key, boolean defaultValue) {
        boolean val = defaultValue;
        if (props.containsKey(key)) {
            val = CaseUtil.caseBoolean(props.getProperty(key));
        }
        return val;
    }
}
