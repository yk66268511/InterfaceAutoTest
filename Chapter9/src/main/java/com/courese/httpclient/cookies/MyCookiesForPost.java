package com.courese.httpclient.cookies;


import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForPost {
    private ResourceBundle bundle;
    private CookieStore store;
    private String url;

    @BeforeTest
    private void BeforeTest(){
        bundle = ResourceBundle.getBundle("application", Locale.CHINA);
        url = bundle.getString("test.url");
    }

    //获取cookies
    @Test
    public void GetCookies() throws IOException {
        String uri = bundle.getString("getCookies");
        String testUrl = this.url + uri;
        HttpGet get = new HttpGet(testUrl);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);
        this.store = client.getCookieStore();
        List<Cookie> cookieList = store.getCookies();
        for(Cookie cookie : cookieList){
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println(name+":"+value);
        }
    }

    //通过cookies访问post请求
    @Test
    public void PostWithCookies() throws IOException {
        String uri = bundle.getString("postWithCookies");
        String testUrl = this.url + uri;
        System.out.println(testUrl);
        HttpPost post = new HttpPost(testUrl);
        DefaultHttpClient client = new DefaultHttpClient();
        JSONObject object = new JSONObject();
        object.put("name","yang");
        object.put("id","310");
        StringEntity entity = new StringEntity(object.toString(),"utf-8");
        post.setEntity(entity);
        post.setHeader("content-type","application/json");
        client.setCookieStore(this.store);
        HttpResponse response = client.execute(post);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);

    }

}