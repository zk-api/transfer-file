package com.github.zk.controller;

import com.github.zk.server.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaokai
 * @date 2019-09-22 14:44
 */
@RestController
public class SendController {
    @Autowired
    private NettyServer nettyServer;
    @RequestMapping("/test")
    public void test() {
        nettyServer.start();
    }
}
