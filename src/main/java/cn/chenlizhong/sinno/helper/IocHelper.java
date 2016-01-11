package cn.chenlizhong.sinno.helper;

import cn.chenlizhong.sinno.annotation.Inject;
import cn.chenlizhong.sinno.util.ReflectionUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

/**
 * 依赖注入助手类
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午1:46
 */
public final class IocHelper {
    static {
        //获取所有的bean类与bean实例之间的映射关系(简称Bean Map)
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (MapUtils.isNotEmpty(beanMap)) {
            //遍历 bean map
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                //从bean map中获取bean类和bean实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();

                //获取bean定义的所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtils.isNotEmpty(beanFields)) {
                    //遍历 bean field
                    for (Field beanField : beanFields) {
                        //判断当前bean field是否带有Inject注解
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            //在bean map中获取bean field对应的实例
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (null != beanFieldInstance) {
                                //通过反射初始化bean field的值
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }

            }
        }
    }
}
