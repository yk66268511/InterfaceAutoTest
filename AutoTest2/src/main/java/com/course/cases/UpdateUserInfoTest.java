package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.UpdateUserInfoCase;
import com.course.utils.DatabaseUtil;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.Test;

import java.io.IOException;

public class UpdateUserInfoTest {

    @Test(groups = "loginTrue",description = "更新用户接口测试")
    public void updateUserInfo() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = sqlSession.selectOne("updateUserInfoCase",1);
        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);
    }

    @Test(groups = "loginTrue",description = "删除用户接口测试")
    public void deleteUserInfo() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = sqlSession.selectOne("updateUserInfoCase",2);
        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);
    }
}
