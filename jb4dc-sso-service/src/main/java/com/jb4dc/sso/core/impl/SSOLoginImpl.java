package com.jb4dc.sso.core.impl;

import com.jb4dc.base.service.exenum.UserTypeEnum;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.encryption.digitaldigest.MD5Utility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.core.ISSOLogin;
import com.jb4dc.sso.core.ISSOLoginStore;
import com.jb4dc.sso.core.SSOTokenPO;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.user.UserEntity;
import com.jb4dc.sso.service.organ.IOrganService;
import com.jb4dc.sso.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SSOLoginImpl implements ISSOLogin {
    ISSOLoginStore ssoLoginStore;

    IUserService userService;

    IOrganService organService;

    @Autowired
    public SSOLoginImpl(ISSOLoginStore _ssoLoginStore,IUserService _userService,IOrganService _organService) {
        this.ssoLoginStore=_ssoLoginStore;
        this.userService=_userService;
        this.organService=_organService;
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
    public SSOTokenPO LoginSystem(String account, String password, String JBuild4DCSSORedirectUrl, String appId) throws JBuild4DCGenerallyException {
        //JB4DCSession b4DSession = new JB4DCSession();
        //b4DSession.setOrganName("4D");
        //b4DSession.setOrganId("OrganId");
        //b4DSession.setUserName("Alex");
        //b4DSession.setUserId("UserId");
        SSOTokenPO code=new SSOTokenPO();
        UserEntity userEntity=userService.getByAccount(account);
        if(userEntity!=null){
           String inputPassword=MD5Utility.GetMD5Code(password, true);
           if(inputPassword.equals(userEntity.getUserPassword())){

               JB4DCSession b4DSession = new JB4DCSession();
               OrganEntity organEntity=organService.getByPrimaryKey(b4DSession,userEntity.getUserOrganId());

               b4DSession.setOrganName(organEntity.getOrganName());
               b4DSession.setOrganId(userEntity.getUserOrganId());
               b4DSession.setUserName(userEntity.getUserName());
               b4DSession.setUserId(userEntity.getUserId());
               if(userEntity.getUserType().equals(UserTypeEnum.manager.getDisplayName())){
                   b4DSession.setFullAuthority(true);
               }
               else{
                   b4DSession.setFullAuthority(false);
               }

               code =ssoLoginStore.createSSOCode(JBuild4DCSSORedirectUrl,appId);
               b4DSession.setSsoSessionToken(code.getToken());

               ssoLoginStore.storeSSOSession(code,b4DSession);
               JB4DCSessionUtility.addSessionAttr(JB4DCSessionUtility.UserLoginSessionKey, b4DSession);
           }
           else
           {
               throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"用户名或密码错误!");
           }
        }
        else{
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"用户名或密码错误!");
        }

        return code;
    }


}
