package com.github.zk.entity;

import java.io.Serializable;

/**
 * @author zhaokai
 * @date 2019-09-22 21:22
 */
public class FileEntity implements Serializable {
    private static final long serialVersionUID = 4927560495702974891L;

    private String fileName;
    private long fileSize;
    private long index;
    private byte[] bytes;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
