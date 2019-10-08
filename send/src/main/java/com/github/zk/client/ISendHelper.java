package com.github.zk.client;

import java.nio.file.Path;

/**
 * @author zhaokai
 * @date 2019-09-28 11:56
 */
public interface ISendHelper {

    /**
     * 文件发送
     * @param filePath
     */
    void sendFile(String filePath);

    /**
     * 发送数据流
     * @param bytes
     */
    void sendByte(Byte[] bytes);

    /**
     * 发送字符串
     * @param buffer
     */
    void sendBuffer(String buffer);
}
