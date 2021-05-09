package com.jb4dc.sso.core;

import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;

public interface ISSOLoginStore {
    void storeSSOSession(SSOTokenPO ssoTokenPO, JB4DCSession jb4DSession) throws JBuild4DCGenerallyException;

    void removeSSOSession(String sessionToken) throws JBuild4DCGenerallyException;

    JB4DCSession getSession(String jBuild4DSSOToken) throws JBuild4DCGenerallyException;

    SSOTokenPO createSSOCode(String returnUrl, String newSessionId,String userId) throws JBuild4DCGenerallyException;
}
