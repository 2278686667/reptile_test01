package com.zhkj.reptile_test01;

import com.alibaba.fastjson2.JSON;

import java.util.Map;

public class JSONPParser {

    public static Map parseJSONP(String jsonp) {

        int startIndex = jsonp.indexOf("(");

        int endIndex = jsonp.lastIndexOf(")");

        String json = jsonp.substring(startIndex + 1, endIndex);

        System.out.println(json);

        return JSON.parseObject(json);

    }

    public static void main(String[] args) {

        Map map = parseJSONP("jsonp_test({})");

    }

}

