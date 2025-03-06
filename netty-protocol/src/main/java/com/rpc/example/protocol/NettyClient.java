package com.rpc.example.protocol;

import com.rpc.example.core.RpcProtocol;
import com.rpc.example.core.RpcRequest;
import com.rpc.example.handler.RpcClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author 周贵龙
 * @version 1.0.0
 * @ClassName NettyClient.java
 * @Description netty客户端
 * @createTime 2022年04月23日 21:13:00
 */
public class NettyClient {

    private final Bootstrap bootstrap;

    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    private String serviceAddress;

    private int servicePort;

    public NettyClient(String serviceAddress, int servicePort) {
        System.out.printf("begin init Netty Client,{},{}", serviceAddress, servicePort);
        bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());

        this.serviceAddress = serviceAddress;
        this.servicePort = servicePort;
    }

    // 发送数据包
    public void sendRequest(RpcProtocol<RpcRequest> protocol) throws InterruptedException {
        final ChannelFuture future = bootstrap.connect(this.serviceAddress, this.servicePort).sync();
        //监听是否连接成功
        future.addListener(listener -> {
            if (future.isSuccess()) {
                System.out.printf("connect rpc server {} success.", this.serviceAddress);
            } else {
                System.out.printf("connect rpc server {} failed. ", this.serviceAddress);
                future.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        System.out.println("begin transfer data");
        future.channel().writeAndFlush(protocol);
    }
}
