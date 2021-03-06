package com.github.zk.listener;

import com.github.zk.server.NettyServer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaokai
 * @date 2019-09-22 22:34
 */
@Component
public class Listener implements ServletContextListener {
    private ThreadFactory nameFactory = new ThreadFactoryBuilder()
            .setNameFormat("server-pool")
            .build();
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1,2,
            1000, TimeUnit.SECONDS,new LinkedBlockingDeque<>(), nameFactory);
    @Autowired
    private NettyServer nettyServer;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        executor.execute(() -> {
            nettyServer.start();
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
