package com.jb4dc.sso.core.impl;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.core.ISSOLogin;
import com.jb4dc.sso.core.ISSOLoginStore;
import com.jb4dc.sso.core.SSOCodePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SSOLoginImpl implements ISSOLogin {
    ISSOLoginStore ssoLoginStore;

    @Autowired
    public SSOLoginImpl(ISSOLoginStore _ssoLoginStore) {
        this.ssoLoginStore=_ssoLoginStore;
    }

    @Override
    public JB4DCSession LoginMainSystem(String account, String password) {

        JB4DCSession b4DSession = new JB4DCSession();
        b4DSession.setOrganName("4D");
        b4DSession.setOrganId("OrganId");
        b4DSession.setUserName("Alex");
        b4DSession.setUserId("UserId");

        SSOCodePO code=ssoLoginStore.createSSOCode("","");
        b4DSession.setSsoCode(code.getCode());

        ssoLoginStore.storeSSOSession(b4DSession,code);

        JB4DCSessionUtility.addSessionAttr(JB4DCSessionUtility.UserLoginSessionKey, b4DSession);

        return b4DSession;
    }

    @Override
    public SSOCodePO LoginSSOSystem(String account, String password,String redirectUrl,String appId) {
        JB4DCSession b4DSession = new JB4DCSession();
        b4DSession.setOrganName("4D");
        b4DSession.setOrganId("OrganId");
        b4DSession.setUserName("Alex");
        b4DSession.setUserId("UserId");

        SSOCodePO code=ssoLoginStore.createSSOCode(redirectUrl,appId);
        b4DSession.setSsoCode(code.getCode());

        ssoLoginStore.storeSSOSession(b4DSession,code);

        JB4DCSessionUtility.addSessionAttr(JB4DCSessionUtility.UserLoginSessionKey, b4DSession);

        return code;
    }


}
