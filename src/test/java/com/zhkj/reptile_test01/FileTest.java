package com.zhkj.reptile_test01;

import java.io.*;

public class FileTest {
    static final String src="D:\\ZHKJ\\file\\";

    public static void main(String[] args) throws IOException {
//        createFile();
        redaFile();
    }

    private static void redaFile() throws IOException {
        File file=new File(src+"url.txt");
        FileReader  fileReader=new FileReader (file);
        BufferedReader br=new BufferedReader(fileReader);
        String line="";
        while ((line=br.readLine())!=null){
            System.out.println(line);
        }
        br.close();
        fileReader.close();
    }

    private static void createFile() throws IOException {
        File file=new File(src+"url.txt");
        OutputStream outputStream=new FileOutputStream(file);
        String url="http://www.jl.gov.cn/ggzy/zfcg/cggg/202304/t20230407_8690736.html";
        outputStream.write(url.getBytes());
        outputStream.close();
    }
}
