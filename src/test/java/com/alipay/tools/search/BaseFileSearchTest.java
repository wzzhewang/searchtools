package com.alipay.tools.search;

import com.alibaba.common.logging.Logger;
import com.alibaba.common.logging.LoggerFactory;
import com.alipay.tools.search.factor.BaseSearchFactor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alipay.tools.search.util.FileUtil;
import com.sun.deploy.util.SystemUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wangzhe
 * Date: 12/11/13
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseFileSearchTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseFileSearchTest.class);
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private String key;


    public void setup() {
        String root = temporaryFolder.getRoot().getAbsolutePath();
        String path1 = root + "/file1";
        List<String> content=new ArrayList<String>();
        content.add("test1111,deede,eeedee");
        content.add("test1112,deede,eeedee");
        content.add("test1113,deede,eeedee");
        content.add("test1114,deede,eeedee");
        content.add("test1115,deede,eeedee");
        content.add("test1116,deede,eeedee");
        content.add("test1117,deede,eeedee");
        content.add("test1118,deede,eeedee");
        content.add("test1119,deede,eeedee");
        content.add("test1121,deede,eeedee");
        content.add("test1122,deede,eeedee");
        content.add("test1123,deede,eeedee");
        content.add("test1124,deede,eeedee");
        content.add("test1125,deede,eeedee");
        content.add("test1125,deede,eeedee");

        writerContent(path1, content);
        writerContent(root + "/file2", content);
        writerContent(root + "/file3", content);
        writerContent(root + "/file4", content);
        writerContent(root + "/file5", content);




    }

    private void writerContent(String path1, List<String> content) {
        BufferedWriter writer=null;
        try {
            writer = new BufferedWriter(new FileWriter(path1));
            FileUtil.appendContent(writer, content);

        } catch (IOException e) {

        }finally{
            try{
                writer.close();
            }catch(IOException e){

            }
        }
    }
    @Test
    public void testSearch() throws Exception {

        BaseFileSearch<BaseSearchFactor> search=new BaseFileSearch<BaseSearchFactor>();
        String file="/Users/wangzhe/project/git";
        List<String> paths=new ArrayList<String>();
        paths.add("allocation_test.txt");
        key=search.search("201311060002300002300004006082",paths,file,new BaseSearchFactor());
        long startTime = System.currentTimeMillis();
        List<BaseSearchFactor> result=null;
        while(result==null){
            result =search.getResult(key);
            Thread.sleep(1000l);
        }
        long endTime = System.currentTimeMillis();
        logger.info("cost time: "+(endTime-startTime));
    }

    @Test
    public void testGetResult() throws Exception {
        RandFileSearch<BaseSearchFactor> search=new RandFileSearch<BaseSearchFactor>();
        String file="/Users/wangzhe/project/git";
        List<String> paths=new ArrayList<String>();
        paths.add("allocation_test.txt");
        key=search.search("201311060002300002300004006082",paths,file,new BaseSearchFactor());
        long startTime = System.currentTimeMillis();
        List<BaseSearchFactor> result=null;
        while(result==null){
            result =search.getResult(key);
            Thread.sleep(1000l);
        }

        long endTime = System.currentTimeMillis();
        logger.info("cost time: "+(endTime-startTime));

    }
}
