package cn.chenlizhong.sinno.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 切面代理
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/12 下午4:15
 */
public abstract class AspectProxy implements Proxy {

    private static final Logger LOG = LoggerFactory.getLogger(AspectProxy.class);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object ret = null;
        Class<?> clazz = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();

        try {
            if (intercept(clazz, method, params)) {
                before(clazz, method, params);
                ret = proxyChain.doProxyChain();
                after(clazz, method, params, ret);
            } else {
                ret = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            LOG.error("proxy failure", e);
            error(clazz, method, params, e);
            throw e;
        } finally {
            end();
        }

        return ret;
    }

    public void begin() {
    }

    public boolean intercept(Class<?> clazz, Method method, Object[] params) throws Throwable {
        return true;
    }

    public void before(Class<?> clazz, Method method, Object[] params) throws Throwable {

    }

    public void after(Class<?> clazz, Method method, Object[] params, Object result) throws Throwable {
    }

    public void error(Class<?> clazz, Method method, Object[] params, Throwable e) {
    }

    public void end() {
    }
}
