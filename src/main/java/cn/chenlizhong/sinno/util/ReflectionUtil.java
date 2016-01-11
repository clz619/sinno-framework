package cn.chenlizhong.sinno.util;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午12:59
 */
public final class ReflectionUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建实例
     *
     * @param clazz
     * @return
     */
    public static Object newInstance(Class<?> clazz) {
        Object instance;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            LOG.error("new instance failure", e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 调用方法
     *
     * @param obj
     * @param method
     * @param args
     * @return
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        Object ret;
        try {
            method.setAccessible(true);
            ret = method.invoke(obj, args);
        } catch (Exception e) {
            LOG.error("invoke method failure", e);
            throw new RuntimeException(e);
        }
        return ret;
    }

    /**
     * 设置成员变量的值
     *
     * @param obj
     * @param field
     * @param value
     */
    public static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            LOG.error("set field failure", e);
            throw new RuntimeException(e);
        }
    }
}
