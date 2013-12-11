package com.alipay.tools.search;

import com.alipay.tools.search.factor.BaseSearchFactor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alipay.tools.search.util.FileUtil;
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
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private String key;
    private BaseFileSearch<BaseSearchFactor> search=new BaseFileSearch<BaseSearchFactor>();

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
        setup();
        String path1 = temporaryFolder.getRoot().getAbsolutePath();

        List<String> paths=new ArrayList<String>();
        paths.add("file1");
        paths.add("file2");
        paths.add("file3");
        paths.add("file4");
        paths.add("file5");


        key=search.search("test1111",paths,path1,new BaseSearchFactor());

        List<BaseSearchFactor> result=null;
        while(result==null){
            result =search.getResult(key);
            Thread.sleep(1000l);
        }

    }

    @Test
    public void testGetResult() throws Exception {

    }
}
