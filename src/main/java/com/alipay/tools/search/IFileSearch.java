package com.alipay.tools.search;

import com.alipay.tools.search.factor.SearchFactor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wangzhe
 * Date: 12/11/13
 * Time: 8:08 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IFileSearch<T> {
    /**
     * ËÑË÷ÎÄ¼þÄÚÈÝ.
     * @param searchContent
     * @param filePaths
     * @param rootPrefix
     * @return
     */
    public String search(String searchContent, List<String> filePaths,String rootPrefix,SearchFactor<T> searchFactor);

    /**
     *
     * @param id
     * @return
     */

    public  List<T> getResult(String id);
}
