package com.course.config;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;


public class TestConfig {
    //登陆接口uri
    public static String loginUrl;
    //更新用户信息接口uri
    public static String updateUserInfoUrl;
    //获取用户列表接口uri
    public static String getUserListUrl;
    //获取用户信息接口uri
    public static String getUserInfoUrl;
    //添加用户信息接口
    public static String addUserUrl;

    public static DefaultHttpClient client;
    public static CookieStore store;
}
