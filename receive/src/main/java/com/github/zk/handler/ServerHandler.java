package com.github.zk.handler;

import com.github.zk.entity.FileEntity;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author zhaokai
 * @date 2019-09-22 21:15
 */
@ChannelHandler.Sharable
@Component
@Scope("prototype")
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Value("${config.writePath}")
    private String writePath;

    private RandomAccessFile randomAccessFile;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("【服务端】：客户端【{}】已连接", ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("【服务端】：客户端【{}】已断开", ctx.channel().id());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileEntity) {
            FileEntity fileEntity = (FileEntity) msg;
            Path path = Paths.get(writePath + fileEntity.getFileName());
            boolean exists = Files.exists(path);
            if (!exists) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                randomAccessFile = new RandomAccessFile(path.toFile(), "rw");
            }
            //当前文件大小
            long currrentSize = Files.size(path);

            if (currrentSize < fileEntity.getFileSize()) {
                //文件内容
                byte[] bytes = fileEntity.getBytes();
                randomAccessFile.seek(fileEntity.getIndex());
                randomAccessFile.write(bytes);
                //当前文件大小
                currrentSize += bytes.length;
            }
            if (currrentSize >= fileEntity.getFileSize()) {
                logger.info("接收完成时间【" + System.currentTimeMillis() + "】");
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
        }
    }
}
