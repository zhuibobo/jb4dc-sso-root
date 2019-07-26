package com.jb4dc.sso.core.impl;


import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.DateUtility;
import com.jb4dc.core.base.tools.UUIDUtility;
import com.jb4dc.sso.core.ISSOLoginStore;
import com.jb4dc.sso.core.SSOTokenPO;
import org.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SSOLoginStoreImpl implements ISSOLoginStore {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void storeSSOSession(SSOTokenPO ssoTokenPO, JB4DCSession jb4DSession) {
        cacheManager.getCache("SSOSessionStore",String.class,JB4DCSession.class).put(ssoTokenPO.getToken(),jb4DSession);
        //JB4DCacheManager.put(JB4DCacheManager.jb4dPlatformSSOSessionStoreName,ssoCodeVo.getToken(),jb4DSession);
    }

    @Override
    public JB4DCSession getSession(String token) {
        JB4DCSession jb4DSession=cacheManager.getCache("SSOSessionStore",String.class,JB4DCSession.class).get(token);
        if(jb4DSession!=null) {
             jb4DSession.setSsoSessionToken(token);
        }
        return jb4DSession;
    }

    @Override
    public SSOTokenPO createSSOCode(String returnUrl, String appId) {
        String jBuild4DSSOCode= UUIDUtility.getUUIDNotSplit();
        SSOTokenPO codeVo=new SSOTokenPO();
        codeVo.setToken(jBuild4DSSOCode);
        codeVo.setTime(DateUtility.getDate_yyyy_MM_dd_HH_mm_ss());
        codeVo.setRedirectUrl(returnUrl);
        return codeVo;
    }
}
