package com.rpc.example;

import java.lang.reflect.Proxy;

/**
 * @author 周贵龙
 * @version 1.0.0
 * @ClassName RpcClientProxy.java
 * @Description TODO
 * @createTime 2022年04月23日 21:59:00
 */
public class RpcClientProxy {

    public <T> T clientProxy(final Class<T> interfaceCls, final String host, int port) {
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(),
                new Class<?>[]{interfaceCls}, new RpcInvokerProxy(host, port));
    }
}
