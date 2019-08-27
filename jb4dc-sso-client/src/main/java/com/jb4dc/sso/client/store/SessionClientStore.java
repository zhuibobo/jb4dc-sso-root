package com.jb4dc.sso.client.store;

import com.jb4dc.core.base.session.JB4DCSession;
import org.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/8/27
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SessionClientStore {

    @Autowired
    private CacheManager cacheManager;

    public void storeSSOSession(String ssoToken, JB4DCSession jb4DSession) {
        cacheManager.getCache("SSOSessionStore",String.class,JB4DCSession.class).put(ssoToken,jb4DSession);
        //JB4DCacheManager.put(JB4DCacheManager.jb4dPlatformSSOSessionStoreName,ssoCodeVo.getToken(),jb4DSession);
    }

    public JB4DCSession getSession(String token) {
        JB4DCSession jb4DSession=cacheManager.getCache("SSOSessionStore",String.class,JB4DCSession.class).get(token);
        if(jb4DSession!=null) {
            jb4DSession.setSsoSessionToken(token);
        }
        return jb4DSession;
    }
}
