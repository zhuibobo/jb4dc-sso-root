package com.jb4dc.sso.webserver.interceptor;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.session.JB4DCSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/26
 * To change this template use File | Settings | File Templates.
 */
public class SSOLoginedInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果是来自SSO客户端的请求,判断是否登录过,是的话直接返回SSO_CODE;
        if(false) {
            if (request.getParameter("IsJBuild4DCSSOIntegrateSystem") != null && request.getParameter("IsJBuild4DCSSOIntegrateSystem").toLowerCase().equals("true")) {
                String JBuild4DCSSORedirectUrl = request.getParameter("JBuild4DCSSORedirectUrl");
                JB4DCSession session = JB4DCSessionUtility.getSessionNotException();
                if (session != null) {
                    response.sendRedirect(JBuild4DCSSORedirectUrl + "?JBuild4DCSSOToken=" + session.getSsoSessionToken());
                }
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