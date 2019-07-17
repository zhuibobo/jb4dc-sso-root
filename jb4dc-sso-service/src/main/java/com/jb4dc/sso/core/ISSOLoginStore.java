package com.jb4dc.sso.core;

import com.jb4dc.core.base.session.JB4DCSession;

public interface ISSOLoginStore {
    void storeSSOSession(JB4DCSession jb4DSession, SSOCodePO ssoCodeVo);

    JB4DCSession getSession(String jBuild4DSSOCode);

    SSOCodePO createSSOCode(String returnUrl, String appId);
}
