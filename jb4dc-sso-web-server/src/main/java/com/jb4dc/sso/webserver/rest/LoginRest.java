package com.jb4dc.sso.webserver.rest;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.core.ISSOLogin;
import com.jb4dc.sso.core.SSOCodePO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/ValidateAccount", method = RequestMethod.POST)
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
    }

    @ApiOperation(value="验证用户", notes = "验证用户")
    @ApiImplicitParam(name="user", value="User", required = true, dataType = "User")
    @RequestMapping(value = "/ValidateAccountSSO", method = RequestMethod.POST)
    public JBuild4DCResponseVo validateAccountSSO(String account, String password,String redirectUrl,String appId, HttpServletRequest request) throws IOException, ParseException, JBuild4DCGenerallyException {

        SSOCodePO code = ssoLogin.LoginSSOSystem(account,password,redirectUrl,appId);

        JB4DCSession jb4DSession=JB4DCSessionUtility.getSession();
        //request.getSession().setAttribute("theme",request.getContextPath()+"/Themes/Default");
        //operationLogService.writeUserLoginLog(jb4DSession,this.getClass(),request);

        return JBuild4DCResponseVo.opSuccess(code);
    }
}
