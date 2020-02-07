package com.course.utils;

import com.course.config.TestConfig;
import com.course.model.InterfaceName;

import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigFile {
    private static ResourceBundle bundle = ResourceBundle.getBundle("application", Locale.CHINA);

    public static String getUrl(InterfaceName name){
        String address = bundle.getString("autotest2.url");
        String uri = "";
        String testUrl = "";
        if (name == InterfaceName.LOGIN){
            uri = bundle.getString("login.url");
        }
        if (name == InterfaceName.ADDUSERINFO){
            uri = bundle.getString("addUser.url");
        }
        if (name == InterfaceName.GETUSERINFO){
            uri = bundle.getString("getUserInfo.url");
        }
        if (name == InterfaceName.GETUSERLIST){
            uri = bundle.getString("getUserList.url");
        }
        if (name == InterfaceName.UPDATEUSERUSERINFO){
            uri = bundle.getString("updateUserInfo.url");
        }
        testUrl = address + uri;
        return testUrl;
    }
}
