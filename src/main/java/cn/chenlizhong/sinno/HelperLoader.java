package cn.chenlizhong.sinno;

import cn.chenlizhong.sinno.helper.*;
import cn.chenlizhong.sinno.util.ClassUtil;

/**
 * 加载相应的helper类
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午4:40
 */
public final class HelperLoader {
    /**
     * AopHelper一定要先与IocHelper,因为首先需要通过AopHelper获取代理对象,然后才能通过IocHelper进行依赖注入
     */
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };

        for (Class<?> clazz : classList) {
            ClassUtil.loadClass(clazz.getName());
        }
    }
}
