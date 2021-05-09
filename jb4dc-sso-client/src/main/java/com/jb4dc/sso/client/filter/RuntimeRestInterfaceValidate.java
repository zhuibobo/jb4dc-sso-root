package com.jb4dc.sso.client.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RuntimeRestInterfaceValidate {
    public static boolean checkEnable(HttpServletRequest req, HttpServletResponse res){
        return true;
    }
}
