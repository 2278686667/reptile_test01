package com.zhkj.reptile_test01;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Test03 {


    public static void main(String[] args) throws IOException {
        String json="http://www.jl.gov.cn/ggzy/zfcg/cggg/202304/t20230407_8690717.html";
        String body = Jsoup.connect(json).execute().body();
        String s = stringFilter(body);
        System.out.println(s);
    }
    public static String stringFilter(String str)throws PatternSyntaxException {

        String regEx = "(?!<(p).*?>)<.*?>";
        Pattern p_html = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(str);
        str = m_html.replaceAll("");

        return str.trim(); // 返回文本字符串
    }
}
