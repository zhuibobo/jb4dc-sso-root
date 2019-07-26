package com.jb4dc.sso.core;

import com.jb4dc.core.base.session.JB4DCSession;

public interface ISSOLoginStore {
    void storeSSOSession(SSOTokenPO ssoTokenPO, JB4DCSession jb4DSession);

    JB4DCSession getSession(String jBuild4DSSOToken);

    SSOTokenPO createSSOCode(String returnUrl, String appId);
}
