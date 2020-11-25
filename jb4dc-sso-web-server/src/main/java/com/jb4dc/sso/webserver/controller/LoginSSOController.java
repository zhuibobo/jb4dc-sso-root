package com.jb4dc.sso.webserver.controller;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.core.ISSOLogin;
import com.jb4dc.sso.core.ISSOLoginStore;
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

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/17
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/Controller")
public class LoginSSOController {

    @Autowired
    ISSOLoginStore ssoLoginStore;

    @RequestMapping(value = "/LoginSSOView", method = RequestMethod.GET)
    public ModelAndView loginSSOView(HttpServletRequest request) {

        ModelAndView modelAndView=new ModelAndView("LoginSSO");
        return modelAndView;
    }

    @RequestMapping(value = "/LogoutSSOView", method = RequestMethod.GET)
    public void logoutSSOView(HttpServletRequest request, HttpServletResponse response, String JBuild4DCSSOLogoutFromUrl) {
        JB4DCSession session= JB4DCSessionUtility.getSession();
        ssoLoginStore.removeSSOSession(session.getSsoSessionToken());
        JB4DCSessionUtility.clearMyLocationLoginedJB4DCSession();
        try {
            String appName=request.getContextPath();
            response.sendRedirect(appName+"/HTML/LoginSSO.html?JBuild4DCSSORedirectUrl="+ URLEncoder.encode(JBuild4DCSSOLogoutFromUrl,"utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ModelAndView modelAndView=new ModelAndView("LoginSSO");
        //return modelAndView;
    }
}
