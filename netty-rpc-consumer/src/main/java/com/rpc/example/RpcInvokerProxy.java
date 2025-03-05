package com.rpc.example;

import com.rpc.example.constant.ReqType;
import com.rpc.example.constant.RpcConstant;
import com.rpc.example.constant.SerialType;
import com.rpc.example.core.*;
import com.rpc.example.protocol.NettyClient;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcInvokerProxy.java
 * @Description TODO
 * @createTime 2022年04月23日 21:59:00
 */
public class RpcInvokerProxy implements InvocationHandler {

    private String host;

    private int port;

    public RpcInvokerProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("begin invoke target server");
        RpcProtocol<RpcRequest> reqProtocol = new RpcProtocol<>();
        long requestId = RequestHolder.REQUEST_ID.incrementAndGet();
        Header header = new Header(RpcConstant.MAGIC, SerialType.JSON_SERIAL.code(),
                ReqType.REQUEST.code(), requestId, 0);
        reqProtocol.setHeader(header);

        RpcRequest request = new RpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParamsTypes(method.getParameterTypes());
        request.setParams(args);
        reqProtocol.setContent(request);

        //短链接
        NettyClient nettyClient = new NettyClient(host, port);
        // 通过设置DefaultEventLoop进行轮询获取结果
        RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<RpcResponse>(new DefaultEventLoop()));
        // 保存请求ID和返回数据的对应关系
        RequestHolder.REQUEST_MAP.put(requestId, future);
        nettyClient.sendRequest(reqProtocol);
        // 返回异步回调的数据
        return future.getPromise().get().getData();
    }
}
