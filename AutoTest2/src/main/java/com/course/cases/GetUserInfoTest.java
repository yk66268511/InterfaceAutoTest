package com.course.cases;



import com.course.config.TestConfig;
import com.course.model.GetUserInfoCase;
import com.course.model.User;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class GetUserInfoTest {
    @Test(groups = "loginTrue",description = "获取用户信息接口测试",dataProvider = "data")
    public void getUserInfo(GetUserInfoCase getUserInfoCase) throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        System.out.println(getUserInfoCase.toString());
        System.out.println(TestConfig.getUserInfoUrl);

        User user = sqlSession.selectOne(getUserInfoCase.getExpected(),getUserInfoCase);
        List userList = Arrays.asList(user);
        JSONArray jsonArray = new JSONArray(userList);

        //发送请求，实际结果
        JSONArray resultJson = getResultJson(getUserInfoCase);
        JSONArray jsonArray1 = new JSONArray(resultJson.getString(0));

        //验证结果
        System.out.println("预期"+jsonArray.toString());
        System.out.println("实际"+resultJson.toString());
        System.out.println("实际修正"+jsonArray1.toString());

        Assert.assertEquals(jsonArray.toString(),jsonArray1.toString());
    }

    @DataProvider(name = "data")
    public Object[] ProviderData() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        Object[] objects = new Object[2];
        for (int i=1;i<=2;i++){
            objects[i-1] = sqlSession.selectOne("getUserInfoCase",i);
        }
        return objects;
    }

    private JSONArray getResultJson(GetUserInfoCase getUserInfoCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.getUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id",getUserInfoCase.getUserId());
        StringEntity entity = new StringEntity(param.toString());
        post.setEntity(entity);
        post.setHeader("content-type","application/json");
        TestConfig.client.setCookieStore(TestConfig.store);

        HttpResponse response = TestConfig.client.execute(post);
        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        List resultList = Arrays.asList(result);
        JSONArray jsonArray = new JSONArray(resultList);

        return jsonArray;
    }


}
