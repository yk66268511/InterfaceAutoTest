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
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class AddUserTest {

    @Test(groups = "loginTrue",description = "添加用户接口测试")
    public void addUser() throws IOException, InterruptedException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        AddUserCase addUserCase = sqlSession.selectOne("addUserCase",10);
        System.out.println(addUserCase.toString());
        System.out.println(TestConfig.addUserUrl);

        //发起请求，获取实际结果
        String result = getResult(addUserCase);
        User user = sqlSession.selectOne("addUserTest",addUserCase);
        System.out.println(user.toString());

        //验证结果
        Assert.assertEquals(addUserCase.getExpected(),result);
    }

    private String getResult(AddUserCase addUserCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.addUserUrl);
        JSONObject parm = new JSONObject();
        parm.put("id",addUserCase.getId());
        parm.put("userName",addUserCase.getUserName());
        parm.put("password",addUserCase.getPassword());
        parm.put("age",addUserCase.getAge());
        parm.put("sex",addUserCase.getSex());
        parm.put("permission",addUserCase.getPermission());
        parm.put("isDelete",addUserCase.getIsDelete());
        post.setHeader("content-type","application/json");
        StringEntity entity = new StringEntity(parm.toString(),"utf-8");
        post.setEntity(entity);

        TestConfig.client.setCookieStore(TestConfig.store);

        HttpResponse response = TestConfig.client.execute(post);

        String result;
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        return result;
    }


}
