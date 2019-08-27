package com.jb4dc.sso.client.provide;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.service.provide.ISessionProvide;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.sso.client.conf.Conf;
import com.jb4dc.sso.client.store.SessionClientStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/27
 * To change this template use File | Settings | File Templates.
 */
@Primary
@Service
public class ClientSessionProvide implements ISessionProvide {

    private static Logger logger = LoggerFactory.getLogger(ClientSessionProvide.class);

    @Autowired
    SessionClientStore sessionClientStore;

    @Override
    public JB4DCSession provideCustSession(HttpServletRequest request) {
        //String jb4dcSSOToken = request.getHeader(Conf.SSO_TOKEN_URL_PARA_NAME);
        logger.debug("BuildJB4DCSessionInterceptor Get JBuild4DCSSOToken:" + request.getHeader(Conf.SSO_TOKEN_URL_PARA_NAME));
        String jb4dcSSOToken = request.getHeader(Conf.SSO_TOKEN_URL_PARA_NAME);

        if (StringUtility.isNotEmpty(jb4dcSSOToken)) {
            JB4DCSession jb4DCSession = sessionClientStore.getSession(jb4dcSSOToken);
            if(jb4DCSession==null&&jb4dcSSOToken.equals(JB4DCSessionUtility.InitSystemSsoSessionToken)){
                return JB4DCSessionUtility.getInitSystemSession();
            }
            return jb4DCSession;
        }
        return null;
    }
}
