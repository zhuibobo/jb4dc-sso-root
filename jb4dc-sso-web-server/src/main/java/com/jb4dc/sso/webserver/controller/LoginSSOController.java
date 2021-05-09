package com.jb4dc.sso.webserver.controller;

import com.jb4dc.base.service.general.JB4DCSessionCenter;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.core.ISSOLogin;
import com.jb4dc.sso.core.ISSOLoginStore;
import com.jb4dc.sso.core.SSOTokenPO;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/17
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/Controller")
public class LoginSSOController {

    //@Autowired
    //ISSOLoginStore ssoLoginStore;

    @Autowired
    ISSOLogin ssoLogin;

    @RequestMapping(value = "/LoginSSOView", method = RequestMethod.GET)
    public ModelAndView loginSSOView(HttpServletRequest request) {

        ModelAndView modelAndView=new ModelAndView("LoginSSO");
        return modelAndView;
    }

    @RequestMapping(value = "/LogoutSSOView", method = RequestMethod.GET)
    public void logoutSSOView(HttpServletRequest request, HttpServletResponse response, String JBuild4DCSSOLogoutFromUrl) {
        JB4DCSession session= JB4DCSessionUtility.getSession();
        //ssoLoginStore.removeSSOSession(session.getSsoSessionToken());
        JB4DCSessionCenter.removeSession(session.getCookieSessionId());
        JB4DCSessionUtility.clearMyLocationLoginedJB4DCSession();
        try {
            String appName=request.getContextPath();
            response.sendRedirect(appName+"/HTML/LoginSSO.html?JBuild4DCSSORedirectUrl="+ URLEncoder.encode(JBuild4DCSSOLogoutFromUrl,"utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/CheckSSOSession", method = RequestMethod.GET)
    public void checkSSOSession(String JBuild4DCSSORedirectUrl,HttpServletRequest req, HttpServletResponse res) throws IOException, ParseException, JBuild4DCGenerallyException {
        JB4DCSession jb4DSession = JB4DCSessionUtility.getSessionAndCheck();
        JB4DCSessionUtility.sendJSessionIdToClient(req);
        if(jb4DSession!=null){
            //返回原始页面,并提供一个临时Token用于获取Session
            SSOTokenPO code = ssoLogin.createTempSessionForClient(jb4DSession);
            String url=JBuild4DCSSORedirectUrl.contains("?")?JBuild4DCSSORedirectUrl+"&JBuild4DCSSOToken="+code.getTempToken():JBuild4DCSSORedirectUrl+"?JBuild4DCSSOToken="+code.getTempToken();
            res.sendRedirect(url);
        }
        else{
            //进入登录页面
            res.sendRedirect("../HTML/LoginSSO.html?JBuild4DCSSORedirectUrl="+JBuild4DCSSORedirectUrl);
        }
    }
}
