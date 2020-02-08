package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.AddUserCase;
import com.course.model.User;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class AddUserTest {

    @Test(groups = "loginTrue",description = "添加用户接口测试",dataProvider = "data")
    public void addUser(AddUserCase addUserCase) throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();

        System.out.println(addUserCase.toString());
        System.out.println(TestConfig.addUserUrl);

        //发起请求，获取实际结果
        String result = getResult(addUserCase);
        User user = sqlSession.selectOne("addUserTest",addUserCase);
        System.out.println(user.toString());

        //验证结果
        Assert.assertEquals(addUserCase.getExpected(),result);
    }

    @DataProvider(name = "data")
    public Object[] ProviderData() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();

        Object[] objects = new Object[2];
        for (int i = 10;i<=11;i++){
            objects[i-10] = sqlSession.selectOne("addUserCase",i);
        }

        return objects;
    }

    private String getResult(AddUserCase addUserCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.addUserUrl);
        JSONObject param = new JSONObject();
        param.put("id",addUserCase.getId());
        param.put("userName",addUserCase.getUserName());
        param.put("password",addUserCase.getPassword());
        param.put("age",addUserCase.getAge());
        param.put("sex",addUserCase.getSex());
        param.put("permission",addUserCase.getPermission());
        param.put("isDelete",addUserCase.getIsDelete());
        post.setHeader("content-type","application/json");
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);

        TestConfig.client.setCookieStore(TestConfig.store);

        HttpResponse response = TestConfig.client.execute(post);

        String result;
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        return result;
    }


}
