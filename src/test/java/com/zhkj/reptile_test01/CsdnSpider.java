package com.zhkj.reptile_test01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CsdnSpider {

    // CSDN回答列表 URL 案例
    private static final String URL = "http://www.jl.gov.cn/ggzy/zfcg/cggg/";

    public static void main(String[] args) throws IOException {
        CsdnSpider spider=new CsdnSpider();
        Document doc = spider.accordingToURLGetBrowserHtml("http://was.jl.gov.cn/was5/web/search?channelid=237687&page=1&prepage=17&searchword=modal%3C%3E3%20and%20gtitle%3C%3E%27%27%20and%20gtitle%3C%3E%27null%27%20and%20tType=%27%E6%94%BF%E5%BA%9C%E9%87%87%E8%B4%AD%27%20and%20iType=%27%E9%87%87%E8%B4%AD%E5%85%AC%E5%91%8A%27%20&callback&callback=result&_=1681035100998");
//        //1. 获取页面文档
//        Document doc = Jsoup.connect(URL).get();
//
        //2. 获取回答列表  根据 class: 类选择器 查找
        Elements elements = doc.select(".ewb-listjyxx");

        //3. 解析列表并存储到列表中
        List<String> list = new ArrayList<>();
        Elements select = elements.select(".ewb-list-node");
        for (Element element : select) {
            //查找 a 标签下的 herf 属性
            String url = element.select("a").attr("href");
            list.add(url);
        }
        list.forEach(item->{
            System.out.println(item);
        });

    }

    /**
     * 设置一个无头浏览器,抓取动态渲染页面
     * @param requestUrl 要解析页面URL地址
     * @return 返回Document对象
     */
    public Document accordingToURLGetBrowserHtml(String requestUrl) {
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

}

