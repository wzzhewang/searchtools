package com.alipay.tools.search.thread;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * ���߳��������.
 * User: wangzhe
 * Date: 12/11/13
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MutiThreadSearch<T> {
    /**
     * ��������.
     * @param key
     * @param callable
     * @return
     * @throws java.io.IOException
     */
    public void searchContent(final String key,final Callable<T> callable) throws IOException;

    /**
     * �ϲ����.
     * @param key
     */
    public void reduceResult(String key);

    /**
     * ��ý��.
     * @param key
     * @return
     */
    public List<T> getResult(String key);
}
