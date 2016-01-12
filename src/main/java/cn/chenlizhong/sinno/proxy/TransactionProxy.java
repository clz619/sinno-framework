package cn.chenlizhong.sinno.proxy;

import cn.chenlizhong.sinno.annotation.Transaction;
import cn.chenlizhong.sinno.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 事务代理
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/12 下午8:17
 */
public class TransactionProxy implements Proxy {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionProxy.class);

    /**
     * 线程事务开启标示
     */
    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object ret = null;
        boolean flag = FLAG_HOLDER.get();
        Method method = proxyChain.getTargetMethod();
        if (!flag && method.isAnnotationPresent(Transaction.class)) {
            FLAG_HOLDER.set(true);
            try {
                DatabaseHelper.beginTransaction();
                LOG.debug("begin transaction");
                ret = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                LOG.debug("commit transaction");
            } catch (Exception e) {
                DatabaseHelper.rollbackTransaction();
                LOG.debug("rollback transaction");
                throw e;
            } finally {
                FLAG_HOLDER.remove();
            }
        } else {
            ret = proxyChain.doProxyChain();
        }

        return ret;
    }
}
