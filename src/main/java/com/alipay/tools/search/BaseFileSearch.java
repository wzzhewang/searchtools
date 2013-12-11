package com.alipay.tools.search;

import com.alibaba.common.logging.Logger;
import com.alibaba.common.logging.LoggerFactory;
import com.alipay.tools.search.factor.ReadLine;
import com.alipay.tools.search.factor.SearchFactor;
import com.alipay.tools.search.thread.MutiThreadSearch;
import com.alipay.tools.search.thread.MutiThreadSearchImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: wangzhe
 * Date: 12/11/13
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseFileSearch<T> implements IFileSearch<T> {
    private static final Logger logger = LoggerFactory.getLogger(BaseFileSearch.class);
    /**
     * 写缓存
     */
    protected static final int BUFFER_SIZE = 1024 * 64;
    protected MutiThreadSearch<T> search = new MutiThreadSearchImpl<T>();

    //线程池
    protected ExecutorService executorService;

    public BaseFileSearch() {
        this.executorService = Executors.newFixedThreadPool(100);

    }


    /**
     * 搜索文件内容.
     *
     * @param searchContent
     * @param filePaths
     * @param rootPrefix
     * @return
     */
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
                        search(key, searchContent, fileName);
                    }
                    search.reduceResult(key);
                } catch (Exception e) {
                    logger.error("serach exception:", e);
                }
            }

            private void search(final String key, final String searchContent, final String fileName) {
                try {
                    search.searchContent(key, new Callable<T>() {
                        private T search(final BufferedReader reader)throws IOException {
                            return searchFactor.search(new ReadLine() {
                                @Override
                                public String readLine() throws IOException{
                                    return reader.readLine();
                                }
                            }, searchContent, fileName);
                        }

                        public T call() {
                            BufferedReader reader = null;
                            try {
                                reader = new BufferedReader(new FileReader(fileName), BUFFER_SIZE);
                                logger.info("文件搜索：" + fileName);
                                return search(reader);

                            } catch (IOException e) {
                                logger.error("文件搜索是发生错误：" + e.getMessage(), e);
                            } finally {
                                try {
                                    reader.close();
                                } catch (IOException e) {
                                    logger.error("文件关闭是发生错误：" + e.getMessage(), e);
                                } finally {
                                    reader = null;
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

    /**
     * 获取结果.
     *
     * @param id
     * @return
     */

    public List<T> getResult(String id) {
        return search.getResult(id);
    }
}
