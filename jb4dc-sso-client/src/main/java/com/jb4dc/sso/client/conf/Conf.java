package com.jb4dc.sso.client.conf;


import com.jb4dc.core.base.vo.JBuild4DCResponseVo;

/**
 * Conf
 */
public class Conf {

    public static final String KEY_JB4DC_SSO_SERVER_ADDRESS = "KEY_JB4DC_SSO_SERVER_ADDRESS";
    public static final String KEY_JB4DC_SSO_SERVER_CONTEXT_PATH="KEY_JB4DC_SSO_SERVER_CONTEXT_PATH";
    public static final String KEY_JB4DC_SSO_SERVER_VIEW_LOGIN = "KEY_JB4DC_SSO_SERVER_VIEW_LOGIN";
    public static final String KEY_JB4DC_SSO_SERVER_VIEW_LOGOUT = "KEY_JB4DC_SSO_SERVER_VIEW_LOGOUT";
    public static final String KEY_JB4DC_SSO_SERVER_EXCLUDED="KEY_JB4DC_SSO_SERVER_EXCLUDED";

    public static String JB4DC_SSO_SERVER_ADDRESS;
    public static String JB4DC_SSO_SERVER_CONTEXT_PATH;
    public static String JB4DC_SSO_SERVER_VIEW_LOGIN;
    public static String JB4DC_SSO_SERVER_VIEW_LOGOUT;
    public static String JB4DC_SSO_SERVER_EXCLUDED;

    /**
     * 存储于Cookie中的key,value为当前Session的获取Code
     */
    //public static final String JB4DC_SSO_CLIENT_COOKIE_STORE_KEY = "JB4DC_SSO_CLIENT_COOKIE_STORE_KEY";

    /**
     * 返回到登录页面时,附带上的来源URL参数名
     */
    //public static final String SSO_REDIRECT_URL_PARA_NAME = "JBuild4DCSSORedirectUrl";

    //public static final String SSO_IS_INT_SYSTEM_URL_PARA_NAME="IsJBuild4DCSSOIntegrateSystem";

    //public static final String SSO_TOKEN_URL_PARA_NAME ="JBuild4DCSSOToken";

    //public static final String SSO_TOKEN_HEADER_PARA_NAME ="JBuild4DCSSOHeaderToken";
    /**
     * 跳转时候附带的USERID,用于验证与本地存储的Session中的用户是否一致,解决进入子系统后,关掉页面,切换账号,再次进入系统的问题
     */
    //public static final String SSO_RE_VALIDATION_CLIENT_LOCATION_USER_ID_URL_PARA_NAME="JBuild4DCSSOReVerificationUserId";

    /**
     * login fail result
     */
    public static final JBuild4DCResponseVo SSO_LOGIN_FAIL_RESULT = new JBuild4DCResponseVo(false, "sso not login.",null,501);
}
