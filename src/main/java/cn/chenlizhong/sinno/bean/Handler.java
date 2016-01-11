package cn.chenlizhong.sinno.bean;

import java.lang.reflect.Method;

/**
 * 封装Action信息
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午2:37
 */
public class Handler {

    /**
     * Controller类
     */
    private Class<?> controllerClass;

    /**
     * Action方法
     */
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
