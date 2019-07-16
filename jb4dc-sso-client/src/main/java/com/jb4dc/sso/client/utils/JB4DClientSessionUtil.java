package com.jb4dc.sso.client.utils;

import com.jb4dc.core.base.exception.JBuild4DCSessionTimeoutException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.client.conf.Conf;

import javax.servlet.http.HttpServletRequest;

public class JB4DClientSessionUtil {
    public static JB4DCSession getSession(HttpServletRequest request) throws JBuild4DCSessionTimeoutException {
        //HttpServletRequest request = ServletRequest
        if(request == null) {
            throw new JBuild4DCSessionTimeoutException();
        }
        JB4DCSession b4DSession = (JB4DCSession)request.getSession().getAttribute(Conf.SSO_LOCATION_SESSION_KEY);
        if(b4DSession == null) {
            return null;
        }
        return b4DSession;
    }
}
