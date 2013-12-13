package com.alipay.tools.search.factor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * »ù´¡ËÑË÷²ßÂÔ.
 * User: wangzhe
 * Date: 12/11/13
 * Time: 6:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseSearchFactor implements SearchFactor<BaseSearchFactor> {
    private String key;
    private List<String> result = new ArrayList<String>();

    String getKey() {
        return key;
    }

    List<String> getResult() {
        return result;
    }

    public void setKey(String key) {
        this.key = key;
    }

    void setResult(List<String> result) {
        this.result = result;
    }

    public String searchFilter(String line, String searchcode) {
        if (line != null && line.indexOf(searchcode) >= 0) {
            return line;
        } else {
            return null;
        }
    }
    public BaseSearchFactor consultResult(List<String> result,String key){
        BaseSearchFactor factor=new BaseSearchFactor();
        factor.setResult(result);
        factor.setKey(key);
        return factor;
    }
}
