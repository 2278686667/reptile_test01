package com.zhkj.reptile_test01;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;

public class CreateFile {

    static final String src = "D:\\ZHKJ\\file\\";

    public static void main(String[] args) throws Exception {
        File file = new File(src + "url.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        String line = "";
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            Document document = Jsoup.connect(line).get();
            Elements select = document.select(".ewb-article");
            String html = select.html();
            String text = select.select(".ewb-article-tt").text();
            System.out.println(html);
            CreateFile createFile = new CreateFile();
            createFile.htmlToWord2(html, text);
        }
        br.close();
        fileReader.close();
    }

    public void htmlToWord2(String html, String textName) throws Exception {
        //拼一个标准的HTML格式文档
        String content = "<html><head>" + "</head><body>" + html + "</body></html>";
        InputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
        OutputStream os = new FileOutputStream(src+"word\\" + textName + ".doc");
        this.inputStreamToWord(is, os);
    }

    /**
     * 把is写入到对应的word输出流os中
     * 不考虑异常的捕获，直接抛出
     *
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




}
