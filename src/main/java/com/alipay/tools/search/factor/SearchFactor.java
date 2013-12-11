package com.alipay.tools.search.factor;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * ËÑË÷²ßÂÔ.
 * User: wangzhe
 * Date: 12/11/13
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SearchFactor<T> {
    public  T search(BufferedReader reader,String searchcode,String fileName)throws IOException;
}
