package com.jb4dc.sso.webserver.rest.sso.session;

import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.core.ISSOLoginStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/Rest/SSO/Session")
public class SessionRestResource {

    @Autowired
    ISSOLoginStore ssoLoginStore;

    @RequestMapping(value = "GetSession", method = RequestMethod.POST)
    public JBuild4DCResponseVo getSession(String JBuild4DSSOCode){
        JB4DCSession jb4DSession=ssoLoginStore.getSession(JBuild4DSSOCode);
        return JBuild4DCResponseVo.getDataSuccess(jb4DSession);
    }
}
