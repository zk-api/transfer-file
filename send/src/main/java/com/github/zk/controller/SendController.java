package com.github.zk.controller;

import com.github.zk.client.ISendHelper;
import com.github.zk.entity.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaokai
 * @date 2019-09-22 23:13
 */
@RestController
public class SendController {
    @Autowired
    private ISendHelper iSendHelper;
    @RequestMapping("test")
    public void MethodOne() {
        System.out.println("发送开始时间【" + System.currentTimeMillis() + "】");
        String filePath = "/Users/zhaokai/program/ideaIU-2019.2.dmg";
        iSendHelper.sendFile(filePath);
    }
}
