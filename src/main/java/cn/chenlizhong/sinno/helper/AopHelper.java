package cn.chenlizhong.sinno.helper;

import cn.chenlizhong.sinno.annotation.Aspect;
import cn.chenlizhong.sinno.annotation.Service;
import cn.chenlizhong.sinno.annotation.Transaction;
import cn.chenlizhong.sinno.proxy.AspectProxy;
import cn.chenlizhong.sinno.proxy.Proxy;
import cn.chenlizhong.sinno.proxy.ProxyManager;
import cn.chenlizhong.sinno.proxy.TransactionProxy;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * aop工具类
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/12 下午4:45
 */
public final class AopHelper {

    private static final Logger LOG = LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            //代理类和目标类的映射关系
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            //目标类和代理列表的映射关系
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                //获取目标类的代理对象(包含代理列表)
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                //将代理对象放入bean map
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            LOG.error("aop failure", e);
        }
    }

    /**
     * 获取带有 Aspect 注解的所有类
     *
     * @param aspect
     * @return
     * @throws Exception
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = Sets.newHashSet();
        Class<? extends Annotation> annotation = aspect.value();
        if (annotation != null && !annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }

        return targetClassSet;
    }

    /**
     * 代理类和目标类的映射,一个代理类可以对应一个或多个目标类
     *
     * @return
     * @throws Exception
     */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
        Map<Class<?>, Set<Class<?>>> proxyMap = Maps.newHashMap();

        addAspectProxy(proxyMap);
        addTransactionProxy(proxyMap);
        return proxyMap;
    }

    /**
     * 添加aspect代理
     *
     * @param proxyMap
     * @throws Exception
     */
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        //获取所有代理类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);

        for (Class<?> proxyClass : proxyClassSet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                //获取横切点下的所有类
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
    }

    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
        proxyMap.put(TransactionProxy.class, serviceClassSet);
    }

    /**
     * 目标类和代理对象列表的映射关系
     *
     * @param proxyMap
     * @return
     * @throws Exception
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = Maps.newHashMap();
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            Class<?> proxyClass = proxyEntry.getKey();
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            for (Class<?> targetClass : targetClassSet) {
                Proxy proxy = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = Lists.newArrayList(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }
        return targetMap;
    }
}
