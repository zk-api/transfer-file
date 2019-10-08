package com.github.zk.server;

import com.github.zk.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author zhaokai
 * @date 2019-09-22 10:03
 */
@Component
public class NettyServer {
    private Logger logger = LoggerFactory.getLogger(NettyServer.class);
    @Value("${netty.ip}")
    private String ip;
    @Value("${netty.port}")
    private int port;
    @Autowired
    private ServerHandler serverHandler;
    /**
     * 控制连接线程池
     */
    EventLoopGroup boss = new NioEventLoopGroup();
    /**
     * 控制接收数据线程池
     */
    EventLoopGroup worker = new NioEventLoopGroup();

    public void start() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(ip,port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();

                        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                        pipeline.addLast(serverHandler);
                    }
                });
        try {
            //绑定连接
            ChannelFuture future = b.bind().sync();
            logger.info("服务端已启动，端口号【{}】",port);
            //监听连接关闭
            future.channel().closeFuture().sync();
            logger.info("已断开连接");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
