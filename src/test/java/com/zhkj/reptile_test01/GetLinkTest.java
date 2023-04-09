package com.zhkj.reptile_test01;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GetLinkTest {
    static final String src="D:\\ZHKJ\\file\\";
    public static void main(String[] args) throws IOException {
        String url = createUrl("http://was.jl.gov.cn/was5/web/search?channelid=237687&page=#{[code]}&prepage=17&searchword=modal%3C%3E3%20and%20gtitle%3C%3E%27%27%20and%20gtitle%3C%3E%27null%27%20and%20tType=%27%E6%94%BF%E5%BA%9C%E9%87%87%E8%B4%AD%27%20and%20iType=%27%E9%87%87%E8%B4%AD%E5%85%AC%E5%91%8A%27%20&callback&callback=result&_=1681035100998", 1);
        Document document = accordingToURLGetBrowserHtml(url);
        Elements body = document.select("body");
        String text = body.text();
        Map map = JSONPParser.parseJSONP(text);
        JSONArray datas = (JSONArray) map.get("datas");
        System.out.println(datas);
        List<JSONObject> collect = datas.stream().map(new Function<Object, JSONObject>() {
            @Override
            public JSONObject apply(Object source) {
                return new JSONObject(source);
            }
        }).collect(Collectors.toList());
        for (JSONObject jsonObject : collect) {
            String docpuburl = (String) jsonObject.get("docpuburl")+"\n";
            System.out.println(docpuburl);
            writeTxt(src+"url.txt",docpuburl);

        }

    }
    /**
     * 设置一个无头浏览器,抓取动态渲染页面
     * @param requestUrl 要解析页面URL地址
     * @return 返回Document对象
     */
    public static Document accordingToURLGetBrowserHtml(String requestUrl) {
        System.out.println("正在加载页面: " + requestUrl);
        WebClient webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);//本地ActiveX
        webClient.getOptions().setTimeout(3 * 1000);//设置连接超时时间

        HtmlPage page = null;
        String pageXml = "";
        try {
            page = webClient.getPage(requestUrl);//加载异步ajax网页
            webClient.waitForBackgroundJavaScript(3 * 1000);//异步JS执行需要耗时,所以这里线程要阻塞多少秒,看情况决定,等待异步JS执行结束
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
            pageXml = page.asXml();//直接将加载完成的页面转换成xml格式的字符串

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webClient.close();
        }
        Document document = Jsoup.parse(pageXml);//Jsoup获取document对象
        return document;
    }
    //保存url路径文件
    public static void writeTxt(String txtPath,String content){
        FileOutputStream fileOutputStream = null;
        File file = new File(txtPath);
        try {
            if(file.exists()){
                //判断文件是否存在，如果不存在就新建一个txt
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file,true);//多个true就是追加
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //字符串替换，url分页参数相关
    public static String createUrl(String url,int page){
        //String url="http://was.jl.gov.cn/was5/web/search?channelid=237687&page=#{[code]}&prepage=17&searchword=modal%3C%3E3%20and%20gtitle%3C%3E%27%27%20and%20gtitle%3C%3E%27null%27%20and%20tType=%27%E6%94%BF%E5%BA%9C%E9%87%87%E8%B4%AD%27%20and%20iType=%27%E9%87%87%E8%B4%AD%E5%85%AC%E5%91%8A%27%20&callback&callback=result&_=1681035100998";
        Map<String,Object> map=new HashMap<>();
        map.put("code",page);
        ExpressionParser parser = new SpelExpressionParser();
        TemplateParserContext parserContext = new TemplateParserContext();
        String content = parser.parseExpression(url,parserContext).getValue(map, String.class);
        writeTxt(src+"page.txt",content);
        return content;

    }
}
