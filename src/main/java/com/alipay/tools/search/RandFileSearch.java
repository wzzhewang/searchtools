package com.alipay.tools.search;

import com.alibaba.common.logging.Logger;
import com.alibaba.common.logging.LoggerFactory;
import com.alipay.tools.search.factor.ReadLine;
import com.alipay.tools.search.factor.SearchFactor;
import com.alipay.tools.search.util.FileUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: wangzhe
 * Date: 12/12/13
 * Time: 12:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class RandFileSearch<T> extends BaseFileSearch<T> {
    private static final Logger logger = LoggerFactory.getLogger(RandFileSearch.class);
    private int fileSplitSize = 1024 * 1024 * 1000;

    public String search(final String searchContent, final List<String> filePaths, final String rootPrefix, final SearchFactor<T> searchFactor) {
        StringBuffer paramKey = new StringBuffer();
        paramKey.append(searchContent);
        for (String path : filePaths) {
            paramKey.append(path);
        }
        paramKey.append(rootPrefix);

        final String key = EncryptionByMD5.getMD5(paramKey.toString().getBytes());
        executorService.execute(new Runnable() {
            public void run() {

                try {

                    String fileName = null;
                    for (String filePath : filePaths) {
                        fileName = rootPrefix + "/" + filePath;
                        List<Long[]> positions = split(fileName);
                        for (Long[] pos : positions) {
                            search(key, searchContent, fileName, pos);
                        }
                    }
                    search.reduceResult(key);
                } catch (Exception e) {
                    logger.error("serach exception:", e);
                }
            }

            private void search(final String key, final String searchContent, final String fileName, final Long[] pos) {
                try {
                    search.searchContent(key, new Callable<T>() {
                        public T call() {

                            RandomAccessFile raf = null;
                            final byte[] byteBuffer = new byte[BUFFER_SIZE];
                            try {
                                raf = new RandomAccessFile(fileName, "r");
                                String columnList = null;
                                raf.seek(pos[0]);
                                List<String> result = new ArrayList<String>();
                                while ((columnList = FileUtil.readLine(raf, Charset.forName("GBK"), pos[1], byteBuffer)) != null) {
                                    logger.info("文件搜索：" + fileName);

                                    columnList = searchFactor.searchFilter(columnList, searchContent);
                                    if (columnList != null) {
                                        result.add(columnList);
                                    }

                                }
                                return searchFactor.consultResult(result, fileName);
                            } catch (IOException e) {
                                logger.error("文件搜索是发生错误：" + e.getMessage(), e);
                            } finally {
                                try {
                                    raf.close();
                                } catch (IOException e) {
                                    logger.error("文件关闭是发生错误：" + e.getMessage(), e);
                                } finally {
                                    raf = null;
                                }
                            }
                            return null;
                        }
                    });
                } catch (IOException e) {
                    logger.error("serach exception:", e);
                }
            }
        }

        );

        return key;
    }

    private List<Long[]> split(String fileName) {
        try {
            List<Long[]> splitResult = FileUtil
                    .splitFile(fileName, fileSplitSize);
            List<String> resultList = new ArrayList<String>(splitResult.size());
            for (int i = 0; i < splitResult.size(); i++) {
                Long[] oneSplit = splitResult.get(i);
                if (oneSplit == null || oneSplit.length != 2 || oneSplit[0] == null
                        || oneSplit[1] == null) {
                    throw new RuntimeException("文件格式有误,切割操作出错!");
                }
            }
            return splitResult;
        } catch (IOException e) {
            throw new RuntimeException("文件切割操作出错!", e);
        }
    }
}
