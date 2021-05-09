package com.jb4dc.sso.core;


import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;

public interface ISSOLogin {
    SSOTokenPO LoginSystem(String account, String password, String redirectUrl, String cookieSessionId,String jSessionId) throws JBuild4DCGenerallyException;

    SSOTokenPO createTempSessionForClient(JB4DCSession jb4DSession);
    //JB4DCSession LoginMainSystem(String account, String password);

    //SSOCodePO LoginSSOSystem(String account, String password, String JBuild4DCSSORedirectUrl, String appId);
}
