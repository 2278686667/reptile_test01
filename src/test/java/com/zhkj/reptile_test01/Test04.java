package com.zhkj.reptile_test01;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;

public class Test04 {


    public static void main(String[] args) throws Exception {
        String json="http://www.jl.gov.cn/ggzy/zfcg/cggg/201804/t20180410_3749844.html";
        Document document = Jsoup.connect(json).get();
        Elements select = document.select(".ewb-article");
        String html = select.html();
        String text = select.select(".ewb-article-tt").text();
        System.out.println(html);
        Test04 test04=new Test04();
        test04.htmlToWord2(html,text);
    }
    public void htmlToWord2(String html,String textName) throws Exception {
        //拼一个标准的HTML格式文档
        String content = "<html><head>" + "</head><body>" + html + "</body></html>";
        InputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
        OutputStream os = new FileOutputStream("C:\\Users\\CAOHAOBO\\Desktop\\"+textName+".doc");
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
