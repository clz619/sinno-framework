package cn.chenlizhong.sinno.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * action 方法注解
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午12:19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    String value();
}
