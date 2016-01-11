package cn.chenlizhong.sinno;

import cn.chenlizhong.sinno.helper.BeanHelper;
import cn.chenlizhong.sinno.helper.ClassHelper;
import cn.chenlizhong.sinno.helper.ControllerHelper;
import cn.chenlizhong.sinno.helper.IocHelper;
import cn.chenlizhong.sinno.util.ClassUtil;

/**
 * 加载相应的helper类
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午4:40
 */
public final class HelperLoader {
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };

        for (Class<?> clazz : classList) {
            ClassUtil.loadClass(clazz.getName());
        }
    }
}
