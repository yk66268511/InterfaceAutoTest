package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserListCase;
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
import java.util.List;

public class GetUserListTest {
    @DataProvider(name = "data")
    public Object[] providerData() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        Object[] objects = new Object[3];
        for(int i=1;i<=3;i++){
            objects[i-1] = sqlSession.selectOne("getUserListCase", i);
        }
        return objects;
    }


    @Test(groups = "loginTrue",description = "获取用户列表接口测试",dataProvider = "data")
    public void getUserList(GetUserListCase getUserListCase) throws IOException {

        System.out.println(getUserListCase.toString());
        System.out.println(TestConfig.getUserListUrl);
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        List<User> userList = sqlSession.selectList(getUserListCase.getExpected(),getUserListCase);

        JSONArray userListJson = new JSONArray(userList);

        //发送请求，获取实际结果
        JSONArray resultJsonArray = getResultJsonArray(getUserListCase);

        //验证结果
        Assert.assertEquals(userListJson.length(),resultJsonArray.length());
        for (int i=0;i<resultJsonArray.length();i++){
            JSONObject expect = (JSONObject) userListJson.get(i);
            JSONObject actual = (JSONObject) resultJsonArray.get(i);
            System.out.println("预期"+expect.toString());
            System.out.println("实际"+actual.toString());
            Assert.assertEquals(expect.toString(),actual.toString());
        }

    }

    private JSONArray getResultJsonArray(GetUserListCase getUserListCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.getUserListUrl);
        JSONObject param = new JSONObject();
        param.put("sex",getUserListCase.getSex());
        param.put("userName",getUserListCase.getUserName());
        param.put("age",getUserListCase.getAge());
        StringEntity entity = new StringEntity(param.toString());
        post.setHeader("content-type","application/json");
        post.setEntity(entity);

        TestConfig.client.setCookieStore(TestConfig.store);
        HttpResponse response = TestConfig.client.execute(post);
        String result = EntityUtils.toString(response.getEntity(),"utf-8");

        JSONArray resultJsonArray = new JSONArray(result);

        return resultJsonArray;
    }
}
