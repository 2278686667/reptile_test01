package com.zhkj.reptile_test01;

import java.io.*;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
 
 
 
public class HtmlToWord {
 
 public static boolean writeWordFile() {
 
  boolean w = false;
  String path = "C:\\Users\\CAOHAOBO\\Desktop\\";
  
  try {
   if (!"".equals(path)) {
    
    // 检查目录是否存在
    File fileDir = new File(path);
    if (fileDir.exists()) {
     
     // 生成临时文件名称
     String fileName = "a.doc";
     InputStream bodyIs = new FileInputStream("C:\\Users\\CAOHAOBO\\Desktop\\新建文本文档 (2).html");
     String content1 = HtmlToWord.getContent(bodyIs);
     String content = "<html>" +
           "<head>你好</head>" +
          "<body>" +
            "<table>" +
             "<tr>" +
              "<td>信息1</td>" +              
              "<td>信息2</td>" +              
              "<td>t3</td>" +              
             "<tr>" +
            "</table>" +
            "</body>" +
            "</html>";
     
     byte b[] = content1.getBytes("utf-8");
     ByteArrayInputStream bais = new ByteArrayInputStream(b);
     POIFSFileSystem poifs = new POIFSFileSystem();
     DirectoryEntry directory = poifs.getRoot();
     DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
     FileOutputStream ostream = new FileOutputStream(path+ fileName);
     poifs.writeFilesystem(ostream);
     bais.close();
     ostream.close();
     
    }
   }
 
  } catch (IOException e) {
   e.printStackTrace();
  }
 
  return w;
 }
 
 public static void main(String[] args){
  writeWordFile();
 }
 private static String getContent(InputStream... ises) throws IOException {
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
