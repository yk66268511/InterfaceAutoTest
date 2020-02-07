package com.courese.httpclient.cookies;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForGet {
    private ResourceBundle bundle;
    private String url;
    private CookieStore store;

    @BeforeTest
    public void BeforeTest(){
       bundle = ResourceBundle.getBundle("application",Locale.CHINA);
       url = bundle.getString("test.url");
    }

    @Test
    public void testGetCookies() throws IOException {
        String uri = bundle.getString("getCookies");
        String testUrl = this.url + uri;
        HttpGet get = new HttpGet(testUrl);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);
        this.store = client.getCookieStore();
        List<Cookie> cookieList = store.getCookies();
        for (Cookie cookie : cookieList){
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println(name+":"+value);
        }

    }

    @Test(dependsOnMethods = {"testGetCookies"})
    public void testGetwithCookies() throws IOException {
        String uri = bundle.getString("getWithCookies");
        String testUrl = this.url + uri;
        HttpGet get = new HttpGet(testUrl);
        DefaultHttpClient client = new DefaultHttpClient();
        client.setCookieStore(this.store);
        HttpResponse response = client.execute(get);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);
    }
}