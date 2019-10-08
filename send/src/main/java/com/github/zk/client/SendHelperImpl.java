package com.github.zk.client;

import com.github.zk.entity.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author zhaokai
 * @date 2019-09-28 12:03
 */
@Service
public class SendHelperImpl implements ISendHelper {
    @Autowired
    private NettyClient nettyClient;
    @Value("${config.shard}")
    private int shard;
    @Override
    public void sendFile(String filePath) {
        Path path = Paths.get(filePath);
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(path.toFile(), "r");
            //文件名称
            String fileName = path.getFileName().toString();
            //文件长度
            long fileSize = 0L;
            try {
                fileSize = Files.size(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytes;
            if (fileSize < shard) {
                bytes = new byte[(int) fileSize];
            } else {
                bytes = new byte[shard];
            }

            // 读到的索引位置
            long index = 0;
            // 剩余文件长度
            long residue = fileSize;
            randomAccessFile.seek(index);
            int readSize = 0;
            while((readSize = randomAccessFile.read(bytes)) > 0) {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileName(fileName);
                fileEntity.setFileSize(fileSize);
                fileEntity.setBytes(bytes);
                fileEntity.setIndex(index);
                nettyClient.sendMsg(fileEntity);
//                Thread.sleep(1000);
                index += shard;
                residue -= readSize;
                if (residue < shard) {
                    bytes = new byte[(int) residue];
                } else {
                    bytes = new byte[shard];
                }

                randomAccessFile.seek(index);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendByte(Byte[] bytes) {

    }

    @Override
    public void sendBuffer(String buffer) {

    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/Users/zhaokai/work/1.JPG");
        System.out.println(Files.size(path));
    }
}
