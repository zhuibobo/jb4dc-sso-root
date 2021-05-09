package com.jb4dc.sso.core.impl;


import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.core.ISSOLoginStore;
import com.jb4dc.sso.core.SSOTokenPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SSOLoginStoreImpl implements ISSOLoginStore {

    /*@Autowired
    private SSOCacheManager ssoCacheManager;*/

    @Override
    public void storeSSOSession(SSOTokenPO ssoTokenPO, JB4DCSession jb4DSession) throws JBuild4DCGenerallyException {
        throw JBuild4DCGenerallyException.getNotSupportMethodException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE);
        //ssoCacheManager.getCache("SSOSessionStore",String.class,JB4DCSession.class).put(ssoTokenPO.getToken(),jb4DSession);
    }

    @Override
    public void removeSSOSession(String sessionToken) throws JBuild4DCGenerallyException {
        throw JBuild4DCGenerallyException.getNotSupportMethodException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE);
        //ssoCacheManager.getCache("SSOSessionStore",String.class,JB4DCSession.class).remove(sessionToken);
    }

    @Override
    public JB4DCSession getSession(String token) throws JBuild4DCGenerallyException {
        throw JBuild4DCGenerallyException.getNotSupportMethodException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE);
        /*JB4DCSession jb4DSession=ssoCacheManager.getCache("SSOSessionStore",String.class,JB4DCSession.class).get(token);
        if(jb4DSession!=null) {
             jb4DSession.setSsoSessionToken(token);
        }
        return jb4DSession;*/
    }

    @Override
    public SSOTokenPO createSSOCode(String returnUrl, String newSessionId,String userId) throws JBuild4DCGenerallyException {
        throw JBuild4DCGenerallyException.getNotSupportMethodException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE);
        /*String jBuild4DSSOCode= UUIDUtility.getUUIDNotSplit();
        SSOTokenPO codeVo=new SSOTokenPO();
        codeVo.setToken(jBuild4DSSOCode);
        codeVo.setTime(DateUtility.getDate_yyyy_MM_dd_HH_mm_ss());
        codeVo.setRedirectUrl(returnUrl);
        codeVo.setUserId(userId);
        return codeVo;*/
    }
}
