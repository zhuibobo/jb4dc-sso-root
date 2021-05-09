package com.jb4dc.sso.client.filter;

import com.jb4dc.base.service.general.JB4DCSessionCenter;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.tools.BeanUtility;
import com.jb4dc.base.tools.JsonUtility;
import com.jb4dc.base.tools.URLUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.CookieUtility;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.sso.client.conf.Conf;
import com.jb4dc.sso.client.extend.ICheckSessionSuccess;
import com.jb4dc.sso.client.proxy.LoginProxyUtility;
import com.jb4dc.sso.client.store.SessionClientStore;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SsoWebFilter extends HttpServlet implements Filter {

    private static Logger logger = LoggerFactory.getLogger(SsoWebFilter.class);

    private ICheckSessionSuccess checkSessionSuccess;

    SessionClientStore sessionClientStore;

    private String excludedPaths;
    private ISSoWebFilterLocationPreCheck ssoWebFilterLocationPreCheck;
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public void setCheckSessionSuccess(ICheckSessionSuccess checkSessionSuccess) {
        this.checkSessionSuccess = checkSessionSuccess;
    }

    public void setSsoWebFilterLocationPreCheck(ISSoWebFilterLocationPreCheck ssoWebFilterLocationPreCheck) {
        this.ssoWebFilterLocationPreCheck = ssoWebFilterLocationPreCheck;
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
        String jSessionId=JB4DCSessionUtility.sendJSessionIdToClient(req);
        HttpServletResponse res = (HttpServletResponse) response;

        String absPath=req.getRequestURI();
        logger.debug("SsoWebFilter-URL:"+absPath);

        if (absPath.indexOf("/Runtime/")>0){
            if(!RuntimeRestInterfaceValidate.checkEnable(req,res)){
                res.setContentType("application/json;charset=utf-8");
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "没有接口的访问权限");
                result.put("errorCode", Conf.SSO_LOGIN_FAIL_RESULT.getErrorCode());
                result.put("data", null);
                result.put("traceMsg", "");
                result.put("exKVData", null);
                res.getWriter().println(JsonUtility.toObjectString(result));
                return;
            }
        }

        //排除Session验证的url规则
        String defaultExcludedPaths="/**/*.js,/**/*.js.map,/**/*.css,/**/*.woff2,/**/*.ttf,/**/*.png,/**/*.jpg,/**/*.jpeg,/**/*.gif,/**/*.apk," +
                "/**/GetClientSystemTitle**," +
                "/**/Runtime/**," +
                "/**/RunTime/**," +
                "/**/Rest/Login/**," +
                "/**/Controller/CheckSSOSession**," +
                "/**/HTML/LoginSSO.html," +
                "/**/Controller/Index";
        excludedPaths=defaultExcludedPaths+(StringUtility.isNotEmpty(excludedPaths)?","+excludedPaths:"");
        if (excludedPaths!=null && excludedPaths.trim().length()>0) {
            for (String excludedPath : excludedPaths.split(",")) {
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
        JB4DCSession jb4DCSession=null;

        String sessionId=CookieUtility.get(req,JB4DCSessionCenter.WebClientCookieSessionKeyName)!=null?CookieUtility.get(req,JB4DCSessionCenter.WebClientCookieSessionKeyName).getValue():"";
        if(StringUtility.isEmpty(sessionId)){
            sessionId=StringUtility.isNotEmpty(req.getHeader(Conf.SSO_TOKEN_HEADER_PARA_NAME))?req.getHeader(Conf.SSO_TOKEN_HEADER_PARA_NAME):"";
        }

        if(JB4DCSessionCenter.existSession(sessionId)){
            //从缓存服务中获取Session
            jb4DCSession= JB4DCSessionCenter.getSession(sessionId);
            JB4DCSessionUtility.setSessionToWebContext(jb4DCSession);
        }
        else if(StringUtility.isNotEmpty(request.getParameter(Conf.SSO_TOKEN_URL_PARA_NAME))){
            String tempToken = request.getParameter(Conf.SSO_TOKEN_URL_PARA_NAME);
            jb4DCSession= JB4DCSessionCenter.getSession(tempToken);
            if(jb4DCSession!=null){
                String cookieSessionId = jb4DCSession.getCookieSessionId();
                CookieUtility.set(res, JB4DCSessionCenter.WebClientCookieSessionKeyName, cookieSessionId, false,req.getContextPath());
            }
        }

        if(jb4DCSession==null) {
            String header = req.getHeader("content-type");
            //如果是Ajax请求?
            //json msg
            boolean isJson = header != null && header.contains("json");
            if (isJson) {
                res.setContentType("application/json;charset=utf-8");
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", Conf.SSO_LOGIN_FAIL_RESULT.getMessage());
                result.put("errorCode", Conf.SSO_LOGIN_FAIL_RESULT.getErrorCode());
                result.put("data", null);
                result.put("traceMsg", "");
                result.put("exKVData", null);
                //res.getWriter().println("{\"code\":" + Conf.SSO_LOGIN_FAIL_RESULT.getErrorCode() + ", \"msg\":\"" + Conf.SSO_LOGIN_FAIL_RESULT.getMessage() + "\"}");
                res.getWriter().println(JsonUtility.toObjectString(result));
                return;
            }
            else {
                //跳转到单点登录系统进行Session验证
                String link = null;
                try {
                    link = URLUtility.encode(req.getRequestURL().toString());
                } catch (JBuild4DCGenerallyException e) {
                    e.printStackTrace();
                }
                String checkSessionUrl = Conf.JB4DC_SSO_SERVER_ADDRESS.concat(Conf.JB4DC_SSO_SERVER_CONTEXT_PATH)
                        .concat("/Controller/CheckSSOSession") + "?" + Conf.SSO_REDIRECT_URL_PARA_NAME + "=" + link;
                res.sendRedirect(checkSessionUrl);
                //跳转到单点登录系统进行登录,创建SessionId
                /*String cookieSessionId = JB4DCSessionCenter.newCookieSessionId();
                CookieUtility.set(res, JB4DCSessionCenter.WebClientCookieSessionKeyName, cookieSessionId, false);
                String link = req.getRequestURL().toString();
                String loginPageUrl = Conf.JB4DC_SSO_SERVER_ADDRESS.concat(Conf.JB4DC_SSO_SERVER_CONTEXT_PATH)
                        .concat(Conf.JB4DC_SSO_SERVER_VIEW_LOGIN)
                        + "?" + Conf.SSO_REDIRECT_URL_PARA_NAME + "=" + link + "&" + Conf.SSO_IS_INT_SYSTEM_URL_PARA_NAME + "=true&CookieSessionId="+cookieSessionId;
                res.sendRedirect(loginPageUrl);*/
            }
        }

        if(false) {
            JB4DCSession jb4DSession = null;
            if (ssoWebFilterLocationPreCheck != null && ssoWebFilterLocationPreCheck.customSelfCheck()) {
                jb4DSession = ssoWebFilterLocationPreCheck.preCheckSession(absPath, request, response, chain);
                if (jb4DSession != null) {
                    chain.doFilter(request, response);
                    return;
                }
            }


            String forceClearClientLocationSession = req.getParameter("ForceClearClientLocationSession");
            if (StringUtility.isNotEmpty(forceClearClientLocationSession) && forceClearClientLocationSession.toLowerCase().equals("true")) {
                JB4DCSessionUtility.clearMyLocationLoginedJB4DCSession();
            }
            //判断是否存在本地Session
            //jb4DSession= JB4DClientSessionUtil.getSession(req);
            jb4DSession = JB4DCSessionUtility.getSessionNotException();

            String reValidationUserId = req.getParameter(Conf.SSO_RE_VALIDATION_CLIENT_LOCATION_USER_ID_URL_PARA_NAME);
            if (StringUtility.isNotEmpty(reValidationUserId) & jb4DSession != null) {
                if (!reValidationUserId.equals(jb4DSession.getUserId())) {
                    // total link
                    String link = req.getRequestURL().toString();
                    // 重定向到登录页面,带上原始的地址,登录后返回原始页面.
                    String loginPageUrl = Conf.JB4DC_SSO_SERVER_ADDRESS.concat(Conf.JB4DC_SSO_SERVER_CONTEXT_PATH)
                            .concat(Conf.JB4DC_SSO_SERVER_VIEW_LOGIN)
                            + "?" + Conf.SSO_REDIRECT_URL_PARA_NAME + "=" + link + "&" + Conf.SSO_IS_INT_SYSTEM_URL_PARA_NAME + "=true";
                    //CookieUtility.set(res, "JB4DC_SSO_CLIENT_COOKIE_STORE_KEY", "", false);
                    //String newSessionId = JB4DCSessionUtility.newSessionId();
                    //CookieUtility.set(res, JB4DCSessionUtility.WebClientCookieSessionKeyName, newSessionId, false);
                    res.sendRedirect(loginPageUrl);
                    JB4DCSessionUtility.clearMyLocationLoginedJB4DCSession();
                    return;
                }
            }

            if (jb4DSession == null) {
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
                        Map<String, Object> result = new HashMap<>();
                        result.put("success", false);
                        result.put("message", Conf.SSO_LOGIN_FAIL_RESULT.getMessage());
                        result.put("errorCode", Conf.SSO_LOGIN_FAIL_RESULT.getErrorCode());
                        result.put("data", null);
                        result.put("traceMsg", "");
                        result.put("exKVData", null);
                        //res.getWriter().println("{\"code\":" + Conf.SSO_LOGIN_FAIL_RESULT.getErrorCode() + ", \"msg\":\"" + Conf.SSO_LOGIN_FAIL_RESULT.getMessage() + "\"}");
                        res.getWriter().println(JsonUtility.toObjectString(result));
                        return;
                    } else {
                        // total link
                        String link = req.getRequestURL().toString();
                        // 重定向到登录页面,带上原始的地址,登录后返回原始页面.
                        String loginPageUrl = Conf.JB4DC_SSO_SERVER_ADDRESS.concat(Conf.JB4DC_SSO_SERVER_CONTEXT_PATH)
                                .concat(Conf.JB4DC_SSO_SERVER_VIEW_LOGIN)
                                + "?" + Conf.SSO_REDIRECT_URL_PARA_NAME + "=" + link + "&" + Conf.SSO_IS_INT_SYSTEM_URL_PARA_NAME + "=true";

                        res.sendRedirect(loginPageUrl);
                        return;
                    }
                } else {
                    //加Session存入本地,以备本地使用
                    ((HttpServletRequest) request).getSession().setAttribute(JB4DCSessionUtility.UserLoginSessionKey, jb4DSession);
                    //sessionClientStore.storeSSOSession(jb4DSession.getSsoSessionToken(), jb4DSession);
                    checkSessionSuccess.run(request, response, chain, jb4DSession);
                }

            }
        }
        chain.doFilter(request, response);
        return;
    }
}
