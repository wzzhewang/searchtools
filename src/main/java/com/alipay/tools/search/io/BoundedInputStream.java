package com.alipay.tools.search.io;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: wangzhe
 * Date: 12/13/13
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class BoundedInputStream extends InputStream {
    private RandomAccessFile randomAccessFile;

    private int mode =  0;      //0 正常的带边界读取模式, //1 不检测边界的模式
    private long remaining;
    private long length;
    private long loc;

    /**
     * 只访问文件种指定范围之内的数据
     * @param file      文件
     * @param offset    起始的位置
     * @param length    数据的长度
     * @throws java.io.IOException
     */
    public BoundedInputStream(File file, long offset, long length) throws IOException {
        this.remaining = length;
        this.length = length;
        this.loc = offset;

        // 打开文件, 跳转到指定的位置
        this.randomAccessFile = new RandomAccessFile(file, "r");
        this.randomAccessFile.seek(this.loc);
    }

    @Override
    public int read() throws IOException {
        // 忽略边界模式
        if(mode == 1) {
            return this.randomAccessFile.read();
        }

        if (this.remaining <= 0) {
            return -1;
        }

        this.remaining --;
        this.loc ++;
        return this.randomAccessFile.readByte();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        // 忽略边界模式
        if(mode == 1) {
            return this.randomAccessFile.read(b, off, len);
        }

        if (this.remaining <= 0) {
            return -1;
        }

        if (len <= 0) {
            return 0;
        }

        if (len > this.remaining) {
            len = (int) this.remaining;
        }
        int ret = this.randomAccessFile.read(b, off, len);

        if (ret > 0) {
            this.loc += ret;
            this.remaining -= ret;
        }
        return ret;
    }

    @Override
    public void close() throws IOException {
        this.randomAccessFile.close();
    }

    @Override
    public int available() throws IOException {
        return (int)this.remaining;
    }

    /**
     * 设置工作的模式
     * @param mode      0 边界检测模式, 1 忽略边界模式
     * @throws IOException
     */
    void setMode(int mode) throws IOException {
        this.mode = mode;
        if(this.mode == 0) {
            this.randomAccessFile.seek(this.loc);
        }
    }

    /**
     * 重置位置, 2种模式的重置方式不同
     */
    void rewind() throws IOException {
        if(this.mode == 0) {
            this.randomAccessFile.seek(this.loc);
            this.remaining = this.length;
        }
        else {
            this.randomAccessFile.seek(0);
        }
    }
    public static  BufferedReader createReader(File file,long offset,long length,int mode,String encoding) throws IOException {


        BoundedInputStream boundedInputStream = new BoundedInputStream(
                file,
                offset,
                length
        );

        // 设置工作模式
        boundedInputStream.setMode(mode);
        if(mode == 1) {
            // 重置到行首, 用于读取汇总和标题数据
            boundedInputStream.rewind();
        }

        return new BufferedReader(
                new InputStreamReader(
                        boundedInputStream,
                        encoding
                )
        );
    }
}
