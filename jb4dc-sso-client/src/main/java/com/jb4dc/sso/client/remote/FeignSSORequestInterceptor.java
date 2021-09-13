package com.jb4dc.sso.client.remote;

import com.jb4dc.base.service.general.JB4DCSessionCenter;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.StringUtility;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/25
 * To change this template use File | Settings | File Templates.
 */
public class FeignSSORequestInterceptor implements RequestInterceptor {
    //public static String SessionToken="";

    //@Autowired
    //FeignSSORequestInterceptorData feignSSORequestInterceptorData;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //传递当前登录用户的Token
        //System.out.println(feignSSORequestInterceptorData.getSessionHeaderId());
        JB4DCSession jb4DCSession= JB4DCSessionUtility.getSession();
        requestTemplate.header("content-type","application/json");
        if(jb4DCSession!=null&& StringUtility.isNotEmpty(jb4DCSession.getCookieSessionId())) {
            requestTemplate.header(JB4DCSessionCenter.WebClientHeaderSessionKeyName, jb4DCSession.getCookieSessionId());
        }
    }
}
