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
     * �������ļ������Ŀ���ļ��Ѵ��ڣ������в�����
     * ·��Ϊ����·����
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
     * ɾ���ļ�
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
     * ��·���л�ȡ�ļ���
     */
    public static String getFileName(String path) {
        String fileName = null;
        if (path != null) {
            fileName = new File(path).getName().trim();
        }
        return fileName;
    }

    /**
     * ��·���л���ļ�����
     */
    public static String getFileNameWithoutExt(String path) {
        String fileName = getFileName(path);
        if (fileName != null && fileName.indexOf('.') > -1) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return fileName;
    }

    /**
     * ����λ��classpath�µ��ļ�
     *
     * @param name classpath�µĿɺ�Ŀ¼���ļ���
     * @return ���� InputStream ��δ�ҵ��᷵��null
     * @author jianfeng.zhu
     */
    public static InputStream loadClassPathFile(String name) {
        InputStream in = null;
        // 3�ֲ�ͬ�ļ��ط�����֤��ͬ�����м��سɹ�
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
     * �����ļ����ݵ��ַ���.
     * 1)ע����ع����ļ��ᵼ�����.
     *
     * @param relatedPath �������Ŀ¼�µ����·��
     * @param encoding    �ļ�����, Ĭ��GBK
     * @return �ļ�����ȫ��
     * @throws java.io.IOException
     */
    public static String loadFileContent(String relatedPath, String encoding) {

        InputStream inputStream = FileUtil.loadClassPathFile(relatedPath);

        if (inputStream == null) {
            logger.error("read file error,path:" + relatedPath);
            return null;
        }
        //TODO: remove this magic number and improve impl/ later.
        byte b[] = new byte[20 * 1000]; //���ݵ����ֵ.
        int len = 0;
        int temp; //���ж�ȡ�����ݶ�ʹ��temp����
        try {
            while ((temp = inputStream.read()) != -1) { //��û�ж�ȡ��ʱ��������ȡ
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
     * �õ��ļ���Inputstream
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
        //���������ļ�
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
     * �������ļ�������ļ��Ѵ���ɾ�����ļ��ٴ������ļ�
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
     * ����С�и��ļ�,����С��λ���ļ�λ��,Ȼ����������һ���س�
     *
     * @param file  �ļ�
     * @param size �и��С
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
                //һֱ�����س���
                while (curByte != 10 && curByte != 13 && rangeEnd < fileLength) {
                    curByte = raf.readByte();
                    rangeEnd++;
                }
                //�������еĻس���
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
