package cn.chenlizhong.sinno.proxy;

/**
 * 代理
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/12 下午3:55
 */
public interface Proxy {

    /**
     * 执行链式代理
     *
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    public Object doProxy(ProxyChain proxyChain) throws Throwable;

}
