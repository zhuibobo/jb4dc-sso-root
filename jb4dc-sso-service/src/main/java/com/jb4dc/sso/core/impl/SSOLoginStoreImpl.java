package com.jb4dc.sso.core.impl;


import com.jb4dc.base.tools.BeanUtility;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.DateUtility;
import com.jb4dc.core.base.tools.UUIDUtility;
import com.jb4dc.sso.core.ISSOLoginStore;
import com.jb4dc.sso.core.SSOCodePO;
import org.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SSOLoginStoreImpl implements ISSOLoginStore {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void storeSSOSession(JB4DCSession jb4DSession, SSOCodePO ssoCodeVo) {
        cacheManager.getCache("SSOSessionStore",String.class,JB4DCSession.class).put(ssoCodeVo.getCode(),jb4DSession);
        //JB4DCacheManager.put(JB4DCacheManager.jb4dPlatformSSOSessionStoreName,ssoCodeVo.getCode(),jb4DSession);
    }

    @Override
    public JB4DCSession getSession(String code) {
        JB4DCSession jb4DSession=cacheManager.getCache("SSOSessionStore",String.class,JB4DCSession.class).get(code);
        jb4DSession.setSsoCode(code);
        return jb4DSession;
    }

    @Override
    public SSOCodePO createSSOCode(String returnUrl, String appId) {
        String jBuild4DSSOCode= UUIDUtility.getUUIDNotSplit();
        SSOCodePO codeVo=new SSOCodePO();
        codeVo.setCode(jBuild4DSSOCode);
        codeVo.setTime(DateUtility.getDate_yyyy_MM_dd_HH_mm_ss());
        codeVo.setRedirectUrl(returnUrl);
        return codeVo;
    }
}
