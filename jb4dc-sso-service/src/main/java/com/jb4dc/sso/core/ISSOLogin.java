package com.jb4dc.sso.core;


import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;

public interface ISSOLogin {
    SSOTokenPO LoginSystem(String account, String password, String JBuild4DCSSORedirectUrl, String appId) throws JBuild4DCGenerallyException;
    //JB4DCSession LoginMainSystem(String account, String password);

    //SSOCodePO LoginSSOSystem(String account, String password, String JBuild4DCSSORedirectUrl, String appId);
}
