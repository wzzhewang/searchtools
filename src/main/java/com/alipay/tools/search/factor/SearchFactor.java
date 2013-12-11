package com.alipay.tools.search.factor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * ËÑË÷²ßÂÔ.
 * User: wangzhe
 * Date: 12/11/13
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SearchFactor<T> {

    public  T search(ReadLine reader,String searchcode,String fileName)throws IOException;
}
