package com.jb4dc.sso.core.impl;

import com.jb4dc.base.service.exenum.UserTypeEnum;
import com.jb4dc.base.service.general.JB4DCSessionCenter;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.encryption.digitaldigest.MD5Utility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.DateUtility;
import com.jb4dc.sso.core.ISSOLogin;
import com.jb4dc.sso.core.ISSOLoginStore;
import com.jb4dc.sso.core.SSOTokenPO;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.role.RoleEntity;
import com.jb4dc.sso.dbentities.user.UserEntity;
import com.jb4dc.sso.service.organ.IOrganService;
import com.jb4dc.sso.service.role.IRoleService;
import com.jb4dc.sso.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SSOLoginImpl implements ISSOLogin {
    //ISSOLoginStore ssoLoginStore;

    IUserService userService;

    IOrganService organService;

    IRoleService roleService;

    @Autowired
    public SSOLoginImpl(ISSOLoginStore _ssoLoginStore, IUserService _userService, IOrganService _organService, IRoleService _roleService) {
        //this.ssoLoginStore=_ssoLoginStore;
        this.userService = _userService;
        this.organService = _organService;
        this.roleService = _roleService;
    }

    /*@Override
    public JB4DCSession LoginMainSystem(String account, String password) {

        JB4DCSession b4DSession = new JB4DCSession();
        b4DSession.setOrganName("4D");
        b4DSession.setOrganId("OrganId");
        b4DSession.setUserName("Alex");
        b4DSession.setUserId("UserId");

        SSOCodePO code=ssoLoginStore.createSSOCode("","");
        b4DSession.setSsoCode(code.getToken());

        ssoLoginStore.storeSSOSession(b4DSession,code);

        JB4DCSessionUtility.addSessionAttr(JB4DCSessionUtility.UserLoginSessionKey, b4DSession);

        return b4DSession;
    }*/

    @Override
    public SSOTokenPO LoginSystem(String account, String password, String redirectUrl, String cookieSessionId,String jSessionId) throws JBuild4DCGenerallyException {

        UserEntity userEntity=userService.getByAccount(null,account);
        if(userEntity!=null){
           String inputPassword=MD5Utility.GetMD5Code(password, true);
           if(inputPassword.equals(userEntity.getUserPassword())) {

               JB4DCSession jb4DCSession = new JB4DCSession();
               OrganEntity organEntity = organService.getByPrimaryKey(jb4DCSession, userEntity.getUserOrganId());

               jb4DCSession.setCookieSessionId(cookieSessionId);
               jb4DCSession.setJaSessionId(jSessionId);
               jb4DCSession.setOrganName(organEntity.getOrganName());
               jb4DCSession.setOrganId(userEntity.getUserOrganId());
               jb4DCSession.setUserName(userEntity.getUserName());
               jb4DCSession.setUserId(userEntity.getUserId());
               if (userEntity.getUserType().equals(UserTypeEnum.manager.getDisplayName())) {
                   jb4DCSession.setFullAuthority(true);
               } else {
                   jb4DCSession.setFullAuthority(false);
               }

               List<RoleEntity> roleEntityList = roleService.getUserRoleList(jb4DCSession,userEntity.getUserOrganId());
               jb4DCSession.setRoleKeys(roleEntityList.stream().map(roleEntity -> roleEntity.getRoleId()).collect(Collectors.toList()));
               jb4DCSession.setRoleNames(roleEntityList.stream().map(roleEntity -> roleEntity.getRoleName()).collect(Collectors.toList()));

               JB4DCSessionCenter.saveSession(cookieSessionId, jb4DCSession);
               JB4DCSessionCenter.saveUserSessionWithUserId(jb4DCSession.getUserId(), jb4DCSession);
               JB4DCSessionUtility.addLocationLoginedJB4DCSession(jb4DCSession);
               String tempToken = UUID.randomUUID().toString();
               JB4DCSessionCenter.saveTempSession(tempToken, jb4DCSession);

               SSOTokenPO code = new SSOTokenPO();
               code.setToken(cookieSessionId);
               code.setTempToken(tempToken);
               code.setTime(DateUtility.getDate_yyyy_MM_dd_HH_mm_ss_SSS());
               code.setRedirectUrl(redirectUrl);
               code.setUserId(userEntity.getUserId());
               return code;
               //code =ssoLoginStore.createSSOCode(JBuild4DCSSORedirectUrl,newSessionId,b4DSession.getUserId());
               //JB4DCSessionUtility.newSession();
               //JB4DCSessionCenter.
               //b4DSession.setSsoSessionToken(code.getToken());

               //ssoLoginStore.storeSSOSession(code,b4DSession);
               //JB4DCSessionUtility.addSessionAttr(JB4DCSessionUtility.UserLoginSessionKey, b4DSession);
           }
           else
           {
               throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"用户名或密码错误!");
           }
        }
        else{
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"用户名或密码错误!");
        }
    }

    @Override
    public SSOTokenPO createTempSessionForClient(JB4DCSession jb4DSession) {
        String tempToken= UUID.randomUUID().toString();
        SSOTokenPO code=new SSOTokenPO();
        code.setToken(jb4DSession.getCookieSessionId());
        code.setTempToken(tempToken);
        code.setTime(DateUtility.getDate_yyyy_MM_dd_HH_mm_ss_SSS());
        code.setRedirectUrl("");
        code.setUserId(jb4DSession.getUserId());
        JB4DCSessionCenter.saveTempSession(tempToken,jb4DSession);
        return code;
    }


}
