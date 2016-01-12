package cn.chenlizhong.sinno.helper;

import cn.chenlizhong.sinno.util.ReflectionUtil;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

/**
 * bean 助手类
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午1:38
 */
public final class BeanHelper {
    private static final Map<Class<?>, Object> BEAN_MAP = Maps.newHashMap();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> clazz : beanClassSet) {
            Object obj = ReflectionUtil.newInstance(clazz);
            BEAN_MAP.put(clazz, obj);
        }
    }

    /**
     * 获取bean映射
     *
     * @return
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }


    /**
     * 设置bean实例
     *
     * @param clazz
     * @param obj
     */
    public static void setBean(Class<?> clazz, Object obj) {
        BEAN_MAP.put(clazz, obj);
    }

    /**
     * 获取bean实例
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        if (!BEAN_MAP.containsKey(clazz)) {
            throw new RuntimeException("can not get bean by class:" + clazz);
        }
        return (T) BEAN_MAP.get(clazz);
    }


}
