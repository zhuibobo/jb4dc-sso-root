package com.jb4dc.sso.webserver.rest;

import com.jb4dc.base.service.general.JB4DCSessionCenter;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.CookieUtility;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.core.ISSOLogin;
import com.jb4dc.sso.core.SSOTokenPO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/9
 * To change this template use File | Settings | File Templates.
 */

@RestController
@RequestMapping(value = "/Rest/Login")
public class LoginRest {

    @Autowired
    ISSOLogin ssoLogin;

    /*@RequestMapping(value = "/ValidateAccount", method = RequestMethod.POST)
    public JBuild4DCResponseVo validateAccount(String accountName, String password) {
        if(accountName.equals("Alex")&&password.equals("4D")){
            JB4DCSession jB4DCSession = new JB4DCSession();
            jB4DCSession.setOrganName("4D");
            jB4DCSession.setOrganId("OrganIdForAlex");
            jB4DCSession.setUserName("十万个");
            jB4DCSession.setUserId("UserIdForAlex");
            JB4DCSessionUtility.addSessionAttr(JB4DCSessionUtility.UserLoginSessionKey, jB4DCSession);
            return JBuild4DCResponseVo.success("验证成功!");
        }
        return JBuild4DCResponseVo.error("验证失败");
    }*/

    @ApiOperation(value="验证用户", notes = "验证用户")
    @ApiImplicitParam(name="user", value="User", required = true, dataType = "User")
    @RequestMapping(value = "/ValidateAccountSSO", method = RequestMethod.POST)
    public JBuild4DCResponseVo validateAccountSSO(String accountName, String password, String redirectUrl, HttpServletRequest req, HttpServletResponse res) throws IOException, ParseException, JBuild4DCGenerallyException {
        String cookieSessionId= JB4DCSessionCenter.newCookieSessionId();
        CookieUtility.set(res, JB4DCSessionCenter.WebClientCookieSessionKeyName, cookieSessionId, false,req.getContextPath());
        SSOTokenPO code = ssoLogin.LoginSystem(accountName,password,redirectUrl,cookieSessionId,req.getSession().getId());
        //JB4DCSession jb4DSession=JB4DCSessionCenter.getSession(newSessionId);
        return JBuild4DCResponseVo.opSuccess(code);
    }
}
