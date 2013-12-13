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

    private int mode =  0;      //0 �����Ĵ��߽��ȡģʽ, //1 �����߽��ģʽ
    private long remaining;
    private long length;
    private long loc;

    /**
     * ֻ�����ļ���ָ����Χ֮�ڵ�����
     * @param file      �ļ�
     * @param offset    ��ʼ��λ��
     * @param length    ���ݵĳ���
     * @throws java.io.IOException
     */
    public BoundedInputStream(File file, long offset, long length) throws IOException {
        this.remaining = length;
        this.length = length;
        this.loc = offset;

        // ���ļ�, ��ת��ָ����λ��
        this.randomAccessFile = new RandomAccessFile(file, "r");
        this.randomAccessFile.seek(this.loc);
    }

    @Override
    public int read() throws IOException {
        // ���Ա߽�ģʽ
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
        // ���Ա߽�ģʽ
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
     * ���ù�����ģʽ
     * @param mode      0 �߽���ģʽ, 1 ���Ա߽�ģʽ
     * @throws IOException
     */
    void setMode(int mode) throws IOException {
        this.mode = mode;
        if(this.mode == 0) {
            this.randomAccessFile.seek(this.loc);
        }
    }

    /**
     * ����λ��, 2��ģʽ�����÷�ʽ��ͬ
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

        // ���ù���ģʽ
        boundedInputStream.setMode(mode);
        if(mode == 1) {
            // ���õ�����, ���ڶ�ȡ���ܺͱ�������
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
