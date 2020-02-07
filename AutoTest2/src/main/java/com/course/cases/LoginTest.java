package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.InterfaceName;
import com.course.model.LoginCase;
import com.course.utils.Assertion;
import com.course.utils.ConfigFile;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

@Listeners(com.course.utils.AssertListener.class)
public class LoginTest {

    @BeforeTest(groups = "loginTrue",description = "测试准备工作")
    public void BeforeTest(){
        TestConfig.addUserUrl = ConfigFile.getUrl(InterfaceName.ADDUSERINFO);
        TestConfig.getUserInfoUrl = ConfigFile.getUrl(InterfaceName.GETUSERINFO);
        TestConfig.getUserListUrl= ConfigFile.getUrl(InterfaceName.GETUSERLIST);
        TestConfig.loginUrl = ConfigFile.getUrl(InterfaceName.LOGIN);
        TestConfig.updateUserInfoUrl = ConfigFile.getUrl(InterfaceName.UPDATEUSERUSERINFO);

        TestConfig.client = new DefaultHttpClient();
    }
    @DataProvider(name = "data")
    public Object[] providerData() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        //获取login_case中的数据条目
        int j = sqlSession.selectOne("countLoginCase");
        //System.out.println(j);
        Object[] objects = new Object[j];
        for(int i=1;i<=j;i++){
            objects[i-1] = sqlSession.selectOne("loginCase", i);
        }

        return objects;
    }

    @Test(groups = "loginTrue",description = "用户登录成功接口测试",dataProvider = "data")
    public void LoginTrue(LoginCase loginCase) throws IOException {
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //发起请求，获取实际结果
        String result = getResult(loginCase);
        //验证结果
        Assert.assertEquals(loginCase.getExpected(),result);
    }

    @Test(groups = "loginFalse",description = "用户登录失败接口测试")
    public void loginFalse() throws IOException{
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        LoginCase loginCase = sqlSession.selectOne("loginCase",3);

        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //发起请求，获取实际结果
        String result = getResult(loginCase);

        //验证结果
        Assert.assertEquals(loginCase.getExpected(),result);
        Assertion.verifyEquals(loginCase.getExpected(),result);
    }

    private String getResult(LoginCase loginCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.loginUrl);
        JSONObject param = new JSONObject();
        param.put("userName",loginCase.getUserName());
        param.put("password",loginCase.getPassword());
        param.put("expected",loginCase.getExpected());
        post.setHeader("content-type","application/json");
        StringEntity entity = new StringEntity(param.toString());
        post.setEntity(entity);

        HttpResponse response = TestConfig.client.execute(post);
        TestConfig.store = TestConfig.client.getCookieStore();
        String result = EntityUtils.toString(response.getEntity(),"utf-8");

        return result;
    }

}
