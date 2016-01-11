package cn.chenlizhong.sinno.helper;

import cn.chenlizhong.sinno.annotation.Action;
import cn.chenlizhong.sinno.bean.Handler;
import cn.chenlizhong.sinno.bean.Request;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * 控制器助手类
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午2:40
 */
public final class ControllerHelper {
    /**
     * 用于存放请求与处理器的映射关系(Action Map)
     */
    private static final Map<Request, Handler> ACTION_MAP = Maps.newHashMap();


    static {
        //获取所有controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();

        if (CollectionUtils.isNotEmpty(controllerClassSet)) {
            //遍历这些controller类
            for (Class<?> controllerClass : controllerClassSet) {
                //获取controller类中定义的方法
                Method[] methods = controllerClass.getMethods();
                if (ArrayUtils.isNotEmpty(methods)) {
                    //遍历controller类中的方法
                    for (Method method : methods) {
                        //判断当前方法是否带有Action注解
                        if (method.isAnnotationPresent(Action.class)) {
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            if (StringUtils.isNotBlank(mapping)) {
                                //严重url映射规则
                                if (mapping.matches("\\w+:/\\w*")) {
                                    String[] arrays = mapping.split(":");
                                    if (ArrayUtils.isNotEmpty(arrays) && 2 == arrays.length) {
                                        //获取请求方法和请求路径
                                        String requestMethod = arrays[0];
                                        String requestPath = arrays[1];
                                        Request request = new Request(requestMethod, requestPath);
                                        Handler handler = new Handler(controllerClass, method);
                                        //初始化ActionMap
                                        ACTION_MAP.put(request, handler);
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //获取handler
    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }

}
