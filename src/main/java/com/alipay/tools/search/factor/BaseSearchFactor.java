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
public class BaseSearchFactor implements SearchFactor {
    private String key;
    private List<String> result=new ArrayList<String>();
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
    public  BaseSearchFactor search(BufferedReader reader,String searchcode,String fileName) throws IOException {
        BaseSearchFactor factor=new BaseSearchFactor();
        factor.setKey(fileName);
        String line=null;
        while((line=reader.readLine())!=null){
            if(line.indexOf(searchcode)>=0){
                factor.getResult().add(line);
            }
        }
        return factor;
    }
}
