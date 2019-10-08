package com.github.zk.client;

import com.github.zk.entity.FileEntity;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;


/**
 * @author zhaokai
 * @date 2019-09-22 22:54
 */
@Component
public class NettyClient {
    private Logger logger = LoggerFactory.getLogger(NettyClient.class);

    @Value("${netty.ip}")
    private String ip;
    @Value("${netty.port}")
    private int port;

    private EventLoopGroup worker = new NioEventLoopGroup();

    private Channel channel;

    public void start() {
        Bootstrap b = new Bootstrap();
        b.group(worker)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(ip, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new ObjectEncoder());
                    }
                });
        while(true) {
            try {
                ChannelFuture future = b.connect().sync();
                future.addListener((ChannelFutureListener) channelFuture -> {
                    this.channel = channelFuture.channel();
                    logger.info("【客户端】：已连接服务器", channel.id());
                });
                future.channel().closeFuture().sync();
                logger.info("【客户端】：连接断开,5秒后重连。。。", channel.id());
                Thread.sleep(5000);
            } catch (Exception e) {
                try {
                    logger.info("【客户端】：连接失败,5秒后重连。。。");
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    public void sendMsg(Object obj) {
        channel.writeAndFlush(obj);
    }
}
