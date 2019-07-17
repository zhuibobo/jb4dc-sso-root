package com.jb4dc.sso.core;


import com.jb4dc.core.base.session.JB4DCSession;

public interface ISSOLogin {
    JB4DCSession LoginMainSystem(String account, String password);

    SSOCodePO LoginSSOSystem(String account, String password, String redirectUrl, String appId);
}
