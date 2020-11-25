package com.jb4dc.sso.client.filter;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.tools.BeanUtility;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.sso.client.conf.Conf;
import com.jb4dc.sso.client.extend.ICheckSessionSuccess;
import com.jb4dc.sso.client.proxy.LoginProxyUtility;
import com.jb4dc.sso.client.store.SessionClientStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SsoWebFilter extends HttpServlet implements Filter {

    private static Logger logger = LoggerFactory.getLogger(SsoWebFilter.class);

    private ICheckSessionSuccess checkSessionSuccess;

    SessionClientStore sessionClientStore;

    private String excludedPaths;
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public void setCheckSessionSuccess(ICheckSessionSuccess checkSessionSuccess) {
        this.checkSessionSuccess = checkSessionSuccess;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        Conf.JB4DC_SSO_SERVER_ADDRESS = filterConfig.getInitParameter(Conf.KEY_JB4DC_SSO_SERVER_ADDRESS);
        Conf.JB4DC_SSO_SERVER_CONTEXT_PATH = filterConfig.getInitParameter(Conf.KEY_JB4DC_SSO_SERVER_CONTEXT_PATH);
        Conf.JB4DC_SSO_SERVER_VIEW_LOGIN = filterConfig.getInitParameter(Conf.KEY_JB4DC_SSO_SERVER_VIEW_LOGIN);
        Conf.JB4DC_SSO_SERVER_VIEW_LOGOUT = filterConfig.getInitParameter(Conf.KEY_JB4DC_SSO_SERVER_VIEW_LOGOUT);
        Conf.JB4DC_SSO_SERVER_EXCLUDED = filterConfig.getInitParameter(Conf.KEY_JB4DC_SSO_SERVER_EXCLUDED);

        excludedPaths = filterConfig.getInitParameter(Conf.KEY_JB4DC_SSO_SERVER_EXCLUDED);
        //Conf.SSO_SERVER_ADDRESS=ssoServerAddress;
        //Conf.SSO_REST_BASE=restBasePath;

        logger.info("SsoWebFilter init.");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if(sessionClientStore==null){
            sessionClientStore= BeanUtility.getBean(SessionClientStore.class);
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String absPath=req.getRequestURI();
        logger.debug("SsoWebFilter-URL:"+absPath);
        //excluded path check
        if (excludedPaths!=null && excludedPaths.trim().length()>0) {
            for (String excludedPath:excludedPaths.split(",")) {
                String uriPattern = excludedPath.trim();
                // 支持ANT表达式
                if (antPathMatcher.match(uriPattern, absPath)) {
                    // excluded path, allow
                    chain.doFilter(request, response);
                    return;
                }
            }
        }
        // make url
        String servletPath = req.getServletPath();

        JB4DCSession jb4DSession= null;

        String forceClearClientLocationSession=req.getParameter("ForceClearClientLocationSession");
        if(StringUtility.isNotEmpty(forceClearClientLocationSession)&&forceClearClientLocationSession.toLowerCase().equals("true")){
            JB4DCSessionUtility.clearMyLocationLoginedJB4DCSession();
        }
        //判断是否存在本地Session
        //jb4DSession= JB4DClientSessionUtil.getSession(req);
        jb4DSession= JB4DCSessionUtility.getSessionNotException();

        String reValidationUserId=req.getParameter(Conf.SSO_RE_VALIDATION_CLIENT_LOCATION_USER_ID_URL_PARA_NAME);
        if(StringUtility.isNotEmpty(reValidationUserId)&jb4DSession!=null){
            if(!reValidationUserId.equals(jb4DSession.getUserId())){
                // total link
                String link = req.getRequestURL().toString();
                // 重定向到登录页面,带上原始的地址,登录后返回原始页面.
                String loginPageUrl = Conf.JB4DC_SSO_SERVER_ADDRESS .concat(Conf.JB4DC_SSO_SERVER_CONTEXT_PATH)
                        .concat(Conf.JB4DC_SSO_SERVER_VIEW_LOGIN)
                        + "?" + Conf.SSO_REDIRECT_URL_PARA_NAME + "=" + link+"&"+Conf.SSO_IS_INT_SYSTEM_URL_PARA_NAME+"=true";
                //CookieUtility.set(res, "JB4DC_SSO_CLIENT_COOKIE_STORE_KEY", "", false);
                res.sendRedirect(loginPageUrl);
                JB4DCSessionUtility.clearMyLocationLoginedJB4DCSession();
                return;
            }
        }

        if(jb4DSession==null) {
            try {
                jb4DSession = LoginProxyUtility.loginCheck(req, res);
            } catch (Exception e) {
                throw new ServletException(e.getMessage(), e.getCause());
            }

            if (jb4DSession == null) {
                String header = req.getHeader("content-type");
                boolean isJson = header != null && header.contains("json");
                if (isJson) {
                    //如果是Ajax请求?
                    //json msg
                    res.setContentType("application/json;charset=utf-8");
                    res.getWriter().println("{\"code\":" + Conf.SSO_LOGIN_FAIL_RESULT.getErrorCode() + ", \"msg\":\"" + Conf.SSO_LOGIN_FAIL_RESULT.getMessage() + "\"}");
                    return;
                } else {
                    // total link
                    String link = req.getRequestURL().toString();
                    // 重定向到登录页面,带上原始的地址,登录后返回原始页面.
                    String loginPageUrl = Conf.JB4DC_SSO_SERVER_ADDRESS .concat(Conf.JB4DC_SSO_SERVER_CONTEXT_PATH)
                            .concat(Conf.JB4DC_SSO_SERVER_VIEW_LOGIN)
                            + "?" + Conf.SSO_REDIRECT_URL_PARA_NAME + "=" + link+"&"+Conf.SSO_IS_INT_SYSTEM_URL_PARA_NAME+"=true";

                    res.sendRedirect(loginPageUrl);
                    return;
                }
            } else {
                //加Session存入本地,以备本地使用
                ((HttpServletRequest) request).getSession().setAttribute(JB4DCSessionUtility.UserLoginSessionKey, jb4DSession);
                sessionClientStore.storeSSOSession(jb4DSession.getSsoSessionToken(),jb4DSession);
                checkSessionSuccess.run(request, response, chain, jb4DSession);
            }

        }

        chain.doFilter(request, response);
        return;
    }
}
