package com.alipay.tools.search.util;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.common.lang.SystemUtil;
import com.alibaba.common.logging.Logger;
import com.alibaba.common.logging.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wangzhe
 * Date: 12/11/13
 * Time: 9:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 重命名文件，如果目标文件已存在，不进行操作。
     * 路径为绝对路径。
     */
    public static boolean rename(String src, String dest) {
        boolean flag = false;
        if (StringUtil.isNotBlank(src) && StringUtil.isNotBlank(dest)) {
            File srcFile = new File(src);
            File destFile = new File(dest);
            if (srcFile.exists() && !destFile.exists()) {
                flag = srcFile.renameTo(destFile);
            }
        }
        return flag;
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String path) {
        boolean flag = false;
        if (StringUtil.isNotBlank(path)) {
            File file = new File(path);
            if (file.exists()) {
                flag = file.delete();
            }
        }
        return flag;
    }

    /**
     * 从路径中获取文件名
     */
    public static String getFileName(String path) {
        String fileName = null;
        if (path != null) {
            fileName = new File(path).getName().trim();
        }
        return fileName;
    }

    /**
     * 从路径中获得文件主名
     */
    public static String getFileNameWithoutExt(String path) {
        String fileName = getFileName(path);
        if (fileName != null && fileName.indexOf('.') > -1) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return fileName;
    }

    /**
     * 加载位于classpath下的文件
     *
     * @param name classpath下的可含目录的文件名
     * @return 返回 InputStream ，未找到会返回null
     * @author jianfeng.zhu
     */
    public static InputStream loadClassPathFile(String name) {
        InputStream in = null;
        // 3种不同的加载方法保证不同环境中加载成功
        in = FileUtil.class.getClassLoader().getResourceAsStream(name);
        if (in == null) {
            in = FileUtil.class.getResourceAsStream(name);
        }
        if (in == null) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            in = loader.getResourceAsStream(name);
        }

        return in;
    }

    /**
     * 加载文件内容到字符串.
     * 1)注意加载过大文件会导致溢出.
     *
     * @param relatedPath 任意加载目录下的相对路径
     * @param encoding    文件编码, 默认GBK
     * @return 文件内容全文
     * @throws java.io.IOException
     */
    public static String loadFileContent(String relatedPath, String encoding) {

        InputStream inputStream = FileUtil.loadClassPathFile(relatedPath);

        if (inputStream == null) {
            logger.error("read file error,path:" + relatedPath);
            return null;
        }
        //TODO: remove this magic number and improve impl/ later.
        byte b[] = new byte[20 * 1000]; //数据的最大值.
        int len = 0;
        int temp; //所有读取的内容都使用temp接收
        try {
            while ((temp = inputStream.read()) != -1) { //当没有读取完时，继续读取
                b[len] = (byte) temp;
                len++;
            }
            if (encoding == null || encoding.trim().length() == 0) {
                encoding = "GBK";
                if (logger.isInfoEnabled()) {
                    logger.info("not define encoding, use default : " + encoding);
                }
            }
            String fileParseTemplateStr = new String(b, 0, len, encoding);
            return fileParseTemplateStr;
        } catch (IOException e) {
            LogUtil.error(logger, "read file error:" + relatedPath, e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e1) {
                LogUtil.info(logger, "close file error:" + relatedPath);
            }
        }
        return null;
    }

    /**
     * 得到文件的Inputstream
     *
     * @param absolutePath
     * @return
     */
    public static InputStream getInputstream(File absolutePath) {
        try {
            return new FileInputStream(absolutePath);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void appendContent(BufferedWriter writer, List<String> content) {
        //清理已有文件
        if (writer == null) {
            throw new IllegalArgumentException("fileWriter is null");
        }
        for (int i = 0; i < content.size(); i++) {
            String record = content.get(i);
            if (StringUtil.isEmpty(record)) {
                continue;
            }
            try {
                writer.append(SystemUtil.getOsInfo().getLineSeparator());
                writer.append(record);
                writer.flush();
            } catch (IOException e) {
                LogUtil.error(logger, "writer error happened!", e);
                throw new RuntimeException("appendContent error!");
            }
        }
    }

    public static BufferedWriter openFile(final String filePath) throws IOException {
        File file = createNewFile(filePath);
        return new BufferedWriter(new FileWriter(file));
    }

    /**
     * 创建新文件，如果文件已存在删除老文件再创建新文件
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static File createNewFile(final String filePath) throws IOException {
        File file = new File(filePath);
        if (!ensureTargetDirectoryExists(file.getParentFile())) {
            throw new IOException("Dir:" + file.getParentFile().getAbsolutePath()
                    + " can't be create or is a file");
        }
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                throw new IOException("delete file failed!" + file.getAbsoluteFile());
            }
        }
        if (!file.createNewFile()) {
            throw new IOException("Create file failed:" + file.getAbsoluteFile());
        }
        return file;
    }

    public static void mergeFiles(String outFile, int slices) {
        int index = outFile.lastIndexOf(".");
        String fileNamePref = outFile.substring(0, index);
        String fileExt = outFile.substring(index, outFile.length());
        List<String> files = new ArrayList<String>();
        for (int i = 0; i < slices; i++) {
            files.add(fileNamePref + "_" + i + fileExt);
        }
        mergeFiles(outFile, files);
    }

    public static void mergeFiles(String outFile, List<String> files) {
        int BUFSIZE = 1024 * 8;
        FileChannel outChannel = null;
        LogUtil.info(logger, "Merge " + files + " into " + outFile);
        try {
            outChannel = new FileOutputStream(outFile).getChannel();
            for (String f : files) {
                FileChannel fc = new FileInputStream(f).getChannel();
                ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
                while (fc.read(bb) != -1) {
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                fc.close();
            }
        } catch (IOException ioe) {
            throw new RuntimeException("merge file error!", ioe);
        } finally {
            try {
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException ignore) {
                if(logger.isDebugEnabled()) {
                    logger.debug(ignore.getMessage(), ignore);
                }
            }
        }
    }

    public static boolean ensureTargetDirectoryExists(File aTargetDir) {
        if (!aTargetDir.exists()) {
            return aTargetDir.mkdirs();
        } else if (aTargetDir.isFile()) {
            return false;
        }
        return true;
    }

    public static boolean ensureTargetFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            if (!destFile.createNewFile()) {
                throw new IOException("createfile fail!");
            }
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            //destination.transferFrom(source, 0, source.size());

            long count = 0;
            long size = sourceFile.length();
            while (count < size) {
                count += destination.transferFrom(source, 0, size - count);
            }
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    /**
     * 按大小切割文件,按大小定位到文件位置,然后读到最近的一个回车
     *
     * @param file  文件
     * @param size 切割大小
     * @return
     */
    public static List<Long[]> splitFile2(final String file, final int size) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(file), "r");
        List<Long[]> fileSplitRangeList = new ArrayList<Long[]>();
        try {
            Long fileLength = raf.length();
            /* if (size >= fileLength) {
                 fileSplitRangeList.add(new Long[]{0L, fileLength});
                 return fileSplitRangeList;
             }*/

            long rangeStart = 0;
            //raf.skipBytes(size);
            //long rangeEnd;

            while (rangeStart < fileLength) {
                raf.seek(rangeStart);
                int realSize = raf.skipBytes(size);
                long rangeEnd = rangeStart + realSize;

                byte curByte;
                try {
                    curByte = raf.readByte();
                } catch (EOFException e) {
                    fileSplitRangeList.add(new Long[] { rangeStart, fileLength });
                    break;
                }
                rangeEnd++;
                //一直读到回车符
                while (curByte != 10 && curByte != 13 && rangeEnd < fileLength) {
                    curByte = raf.readByte();
                    rangeEnd++;
                }
                //读完所有的回车符
                while ((curByte == 10 || curByte == 13) && rangeEnd < fileLength) {
                    curByte = raf.readByte();
                    rangeEnd++;
                }
                if (rangeEnd < fileLength) {
                    fileSplitRangeList.add(new Long[] { rangeStart, rangeEnd - 1 });
                    rangeStart = rangeEnd - 1;
                } else {
                    fileSplitRangeList.add(new Long[] { rangeStart, fileLength });
                    rangeStart = fileLength;
                }
            }
            return fileSplitRangeList;
        } finally {
            raf.close();
        }
    }

    public static List<Long[]> splitFile(final String file, final int size) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(file), "r");
        List<Long[]> fileSplitRangeList = new ArrayList<Long[]>();
        if (raf.length() == 0) {
            return fileSplitRangeList;
        }
        long rangeStart = 0;
        try {
            boolean eof = false;
            //boolean eol;
            raf.seek(rangeStart);
            while (!eof) {
                raf.skipBytes(size);
                boolean eol = false;
                while (!eol && !eof) {
                    switch (raf.read()) {
                        case -1:
                            eof = true;
                            break;
                        case '\n':
                            eol = true;
                            break;
                        case '\r':
                            eol = true;
                            long cur = raf.getFilePointer();
                            if ((raf.read()) != '\n') {
                                raf.seek(cur);
                            }
                            break;
                        default:
                            break;
                    }
                }
                if (rangeStart != raf.getFilePointer()) {
                    fileSplitRangeList.add(new Long[] { rangeStart, raf.getFilePointer() });
                    rangeStart = raf.getFilePointer();
                }
            }
            return fileSplitRangeList;
        } finally {
            raf.close();
        }
    }

    public static String readLine(final RandomAccessFile raf, final Charset charset,
                                        final long endPos, byte[] byteBuffer) throws IOException {
        int inputPos = 0;
        int c;
        boolean eol = false;
        String result=null;

        while (!eol) {
            if (raf.getFilePointer() >= endPos) {
               return result;
            }
            switch (c = raf.read()) {
                case -1:
                    return result;
                case '\n':
                    result=new String(byteBuffer, 0, inputPos, charset);
                    eol = true;
                    break;
                case '\r':
                    eol = true;
                    long cur = raf.getFilePointer();
                    if ((raf.read()) != '\n') {
                        raf.seek(cur);
                    }
                    result=new String(byteBuffer, 0, inputPos, charset);
                    break;
                default:
                    byteBuffer[inputPos++] = ((byte) c);
                    break;
            }
        }
        return result;
    }
}
