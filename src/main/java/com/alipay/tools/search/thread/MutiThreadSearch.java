package com.alipay.tools.search.thread;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 多线程搜索框架.
 * User: wangzhe
 * Date: 12/11/13
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MutiThreadSearch<T> {
    /**
     * 搜索内容.
     * @param key
     * @param callable
     * @return
     * @throws java.io.IOException
     */
    public void searchContent(final String key,final Callable<T> callable) throws IOException;

    /**
     * 合并结果.
     * @param key
     */
    public void reduceResult(String key);

    /**
     * 获得结果.
     * @param key
     * @return
     */
    public List<T> getResult(String key);
}
