package cn.chenlizhong.sinno.annotation;

import java.lang.annotation.*;

/**
 * 切面注解
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/12 下午2:51
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 注解
     *
     * @return
     */
    Class<? extends Annotation> value();
}
