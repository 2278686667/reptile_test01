package com.zhkj.reptile_test01;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Test02 {
    public static void main(String[] args) {
//        createWordForHtml();
    }
    public static void createWordForHtml(String html,String fileName) {
        try {
            String savePath = "文件路径"+fileName+".doc";

            html = html.replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&amp;", "&");
            String content="<html><body>"+html+"</body></html>";

            byte b[] = content.getBytes("GBK");  //这里是必须要设置编码的，不然导出中文就会乱码。
            ByteArrayInputStream bais = new ByteArrayInputStream(b);//将字节数组包装到流中
            /*
             * 关键地方
             * 生成word格式 */
            POIFSFileSystem poifs = new POIFSFileSystem();
            DirectoryEntry directory = poifs.getRoot();
            DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
            OutputStream ostream = new FileOutputStream(savePath);

            poifs.writeFilesystem(ostream);  	//写入内容
            bais.close();
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
