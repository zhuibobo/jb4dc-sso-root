package com.jb4dc.sso.webserver.rest.interceptor;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.sso.client.conf.Conf;
import com.jb4dc.sso.core.ISSOLoginStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BuildJB4DCSessionInterceptor implements HandlerInterceptor
{
    private static Logger logger = LoggerFactory.getLogger(BuildJB4DCSessionInterceptor.class);

    //@Autowired
    ISSOLoginStore ssoLoginStore;

    public BuildJB4DCSessionInterceptor(ISSOLoginStore _ssoLoginStore){
        this.ssoLoginStore=_ssoLoginStore;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("BuildJB4DCSessionInterceptor Get JBuild4DCSSOToken:" + request.getHeader(Conf.SSO_TOKEN_URL_PARA_NAME));
        String jb4dcSSOToken = request.getHeader(Conf.SSO_TOKEN_URL_PARA_NAME);
        if (StringUtility.isNotEmpty(jb4dcSSOToken)) {
            JB4DCSession jb4DCSession = ssoLoginStore.getSession(jb4dcSSOToken);
            JB4DCSessionUtility.addLocationLoginedJB4DCSession(jb4DCSession);
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
