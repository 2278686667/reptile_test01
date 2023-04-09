package com.zhkj.reptile_test01;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.Map;

@SpringBootTest
class ReptileTest01ApplicationTests {

    @Test
    void contextLoads() throws Exception {
        String json="http://www.jl.gov.cn/ggzy/zfcg/cggg/202304/t20230407_8690717.html";
        Document document = Jsoup.connect(json).get();
        Elements select = document.select(".ewb-article");
        String text2 = select.text();
        ReptileTest01ApplicationTests test01ApplicationTests=new ReptileTest01ApplicationTests();
        test01ApplicationTests.htmlToWord2(document.text());
        String text = document.body().text();
        System.out.println(text);
//        for (Element element : select) {
//            String text1 = element.select(".MsoNormal").text();
//        }


    }

    @Test
    void test02(){

    }

    public static void main(String[] args) throws Exception {
        ReptileTest01ApplicationTests reptileTest01ApplicationTests=new ReptileTest01ApplicationTests();
        reptileTest01ApplicationTests.htmlToWord2(null);
    }
    public void htmlToWord2(String bodyI) throws Exception {
        InputStream bodyIs = new FileInputStream("C:\\Users\\CAOHAOBO\\Desktop\\新建文本文档 (2).html");

        String body = this.getContent(bodyIs);
        //拼一个标准的HTML格式文档
        String content = "<html><head><style>" + "</style></head><body>" + body + "</body></html>";
        InputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
        OutputStream os = new FileOutputStream("C:\\Users\\CAOHAOBO\\Desktop\\1.doc");
        this.inputStreamToWord(is, os);
    }

    /**
     * 把is写入到对应的word输出流os中
     * 不考虑异常的捕获，直接抛出
     * @param is
     * @param os
     * @throws IOException
     */
    private void inputStreamToWord(InputStream is, OutputStream os) throws IOException {
        POIFSFileSystem fs = new POIFSFileSystem();
        //对应于org.apache.poi.hdf.extractor.WordDocument
        fs.createDocument(is, "WordDocument");
        fs.writeFilesystem(os);
        os.close();
        is.close();
    }

    /**
     * 把输入流里面的内容以UTF-8编码当文本取出。
     * 不考虑异常，直接抛出
     * @param ises
     * @return
     * @throws IOException
     */
    private String getContent(InputStream... ises) throws IOException {
        if (ises != null) {
            StringBuilder result = new StringBuilder();
            BufferedReader br;
            String line;
            for (InputStream is : ises) {
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line=br.readLine()) != null) {
                    result.append(line);
                }
            }
            return result.toString();
        }
        return null;
    }
}
