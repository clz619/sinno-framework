package cn.chenlizhong.sinno.helper;

import cn.chenlizhong.sinno.annotation.Controller;
import cn.chenlizhong.sinno.annotation.Service;
import cn.chenlizhong.sinno.util.ClassUtil;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 类操作助手
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午12:35
 */
public final class ClassHelper {
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    /**
     * @return 应用包名下的所有类
     */
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * @return 应用包名下所有Service 类
     */
    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> classSet = Sets.newHashSet();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(Service.class)) {
                classSet.add(clazz);
            }
        }
        return classSet;
    }

    /**
     * @return 应用包名下所有Controller 类
     */
    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> classSet = Sets.newHashSet();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                classSet.add(clazz);
            }
        }
        return classSet;
    }

    /**
     * @return 应用包名下所有Bean类(包括:Service, Controller类)
     */
    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> beanClassSet = Sets.newHashSet();
        beanClassSet.addAll(getServiceClassSet());
        beanClassSet.addAll(getControllerClassSet());
        return beanClassSet;
    }
}
