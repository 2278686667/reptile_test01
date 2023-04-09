package com.zhkj.reptile_test01;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
/**
 * java实现爬虫
 */
public class SpiderDemo1 {
 
    //网站主网页链接
    private final static String theURL = "http://www.jl.gov.cn/ggzy/zfcg/cggg/";
    //今日日期，用于标记日志
    private final static String theTIME = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    //网页链接文件路径
    private final static String theFILE = "D:/html/jy1/" + theTIME + "/URL.txt";
    //网页源码路径
    private final static String thePATH = "D:/html/jy1/" + theTIME + "/code";
    //正则表达式，用于判断是否是一个网址
    private final static String theREGEX= "(http|https)://[\\w+\\.?/?]+\\.[A-Za-z]+";
 
    /**
     * 启动类
     * @param args
     */
    public static void main(String[] args) {
        found();
        System.out.println("网站爬取完成");
    }
 
 
    public static void found() {
        PrintWriter pw = null;
        try{
            //创建文件目录
            File fileDir = new File(thePATH);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
 
            //创建网站网页链接文件
            pw = new PrintWriter(new FileWriter(theFILE),true);
 
            //使用递归遍历网站的每个网页
            spiderURL(theURL, pw);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(pw != null) {
                    pw.close();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
 
 
    /**
     * 爬取该网页源码和网页内连接
     * @param url 该网页网址
     * @param tpw 对网站网页网址文件连接的io流
     */
    public static void spiderURL(String url, PrintWriter tpw){
        URL realURL=null;
        URLConnection connection=null;
        BufferedReader br=null;
        PrintWriter pw=null;
        PrintWriter pw1=null;
 
        Pattern pattern=Pattern.compile(theREGEX);
        try{
            realURL=new URL(url);
            connection=realURL.openConnection();
 
            //生成文件夹
            String src = thePATH + url.substring(theURL.length());
            File fileDir = new File(src);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
 
            //生成源代码文件
            pw = new PrintWriter(new FileWriter(src + "/.Testtxt"),true);
            pw1 = tpw;
 
            //爬取网页文件
            br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line=null;
            while((line=br.readLine())!=null){
                //把爬取的源码写入文件
                pw.println(line);
                System.out.println("爬取网页" + url + "成功");
                Matcher matcher=pattern.matcher(line);
                //判断是否是一个网址
                while(matcher.find()){
                    //判断网址是否以网站主网址为前缀，防止爬到其他网站,并判断是否和原先网址重复
                    if(matcher.group().startsWith(theURL) && examine(matcher.group())) {
                        //把爬取的网址写入文件
                        pw1.println(matcher.group());
                        spiderURL(matcher.group(), pw1);
                    }
                }
                System.out.println("网页" + url + "内链接爬取完成");
            }
    }catch(Exception e){
        e.printStackTrace();
    }finally {
            try {
                if(br != null) {
                    br.close();
                }
                if(pw != null) {
                    pw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
 
    /**
     * 判断是否和以储存网址相同
     * @param str 需判断的网址
     * @return 是否重复
     */
    public static boolean examine(String str) {
        BufferedReader br = null;
        String str1;
        try {
            br = new BufferedReader(new FileReader(theFILE));
 
//            //针对该网站无用网页的屏蔽
//            if(str.startsWith("http://www.jyyishu.cn/artnews/")) {
//                return false;
//            }
            
            //循环文件中每一行的网址，判断是否重复，重复则退出
            while((str1 = br.readLine()) != null) {
                if(str.equals(str1)) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}