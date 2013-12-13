package com.alipay.tools.search.factor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * ��������.
 * User: wangzhe
 * Date: 12/11/13
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SearchFactor<T> {

    public  String searchFilter(String content,String searchCode);

    public  T  consultResult(List<String> result,String key);
}
