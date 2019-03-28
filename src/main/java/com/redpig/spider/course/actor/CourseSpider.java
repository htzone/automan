package com.redpig.spider.course.actor;

import com.redpig.common.http.HttpClientManager;

/**
 * Created by hetao on 2018/12/2.
 */
public class CourseSpider {
    private static final String URL = "http://www.2-class.com/api/user/login";

    public static void main(String[] args) throws Exception
    {
        String content = HttpClientManager.instance(URL, HttpClientManager.METHOD_POST)
                .addParam("account", "weixin980")
                .addParam("password", "11111111")
                .addParam("reqtoken", "f0861ee8-8b94-4629-9352-4b4b37b5676d")
                .getContent();
        System.out.println(content);
    }
}
