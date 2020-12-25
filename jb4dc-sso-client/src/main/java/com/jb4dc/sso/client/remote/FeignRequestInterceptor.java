package com.jb4dc.sso.client.remote;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.sso.client.conf.Conf;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/25
 * To change this template use File | Settings | File Templates.
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    //public static String SessionToken="";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //传递当前登录用户的Token
        JB4DCSession jb4DCSession= JB4DCSessionUtility.getSessionNotException();
        requestTemplate.header("content-type","application/json");
        if(jb4DCSession!=null&& StringUtility.isNotEmpty(jb4DCSession.getSsoSessionToken())) {
            requestTemplate.header(Conf.SSO_TOKEN_URL_PARA_NAME, jb4DCSession.getSsoSessionToken());
        }
    }
}
