package com.jb4dc.sso.webserver.interceptor;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.exception.JBuild4DCSessionTimeoutException;
import com.jb4dc.core.base.session.JB4DCSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2018/7/24
 * To change this template use File | Settings | File Templates.
 */
public class LoginedInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(LoginedInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //response.setContentType("text/html;charset=UTF-8");
        Map<String,String> igUrl=new HashMap<>();
        igUrl.put("/Rest/Login/ValidateAccount","");
        igUrl.put("/Rest/Login/ValidateAccountSSO","");
        igUrl.put("/Rest/JBuild4DCYaml/GetClientSystemTitle","");
        igUrl.put("/Rest/SSO/Session/GetSession","");
        igUrl.put("/Rest/SSO/Runtime/","");
        igUrl.put("/Rest/SSO/Session/LoginForRest","");

        igUrl.put("/HTML/Login.html","");
        igUrl.put("/HTML/LoginSSO.html","");

        igUrl.put("/Controller/LoginSSOView","");


        String absPath=request.getRequestURI();
        String appName=request.getContextPath();
        String url=absPath.replaceAll(appName,"");

        if(url.indexOf("Runtime")>0){
            return true;
        }
        if(igUrl.containsKey(url)){
            return true;
        }
        else
        {
            try {
                JB4DCSession session = JB4DCSessionUtility.getSession();
                if (session == null) {
                    response.sendRedirect(appName+"/HTML/LoginSSO.html");
                    return false;
                }
            }
            catch (JBuild4DCSessionTimeoutException ex){
                response.sendRedirect(appName+"/HTML/LoginSSO.html");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
