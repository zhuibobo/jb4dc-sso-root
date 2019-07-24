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
public class SessionRest {

    @Autowired
    ISSOLoginStore ssoLoginStore;

    @RequestMapping(value = "GetSession", method = RequestMethod.POST)
    public JBuild4DCResponseVo getSession(String JBuild4DSSOCode){
        JB4DCSession jb4DSession=ssoLoginStore.getSession(JBuild4DSSOCode);
        return JBuild4DCResponseVo.getDataSuccess(jb4DSession);
    }

    @RequestMapping(value = "LoginForRest", method = RequestMethod.POST)
    public JBuild4DCResponseVo loginForRest(String account,String password){
        JB4DCSession jb4DCSession=new JB4DCSession();
        jb4DCSession.setOrganId("11");
        jb4DCSession.setOrganCode("2");
        jb4DCSession.setOrganName("3");
        jb4DCSession.setUserId("4");
        jb4DCSession.setUserName("5");
        jb4DCSession.setAccountId("6");
        jb4DCSession.setAccountName("7");
        jb4DCSession.setMainDepartmentId("8");
        jb4DCSession.setMainDepartmentName("9");

        return JBuild4DCResponseVo.getDataSuccess(jb4DCSession);
    }
}
