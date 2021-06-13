package com.jb4dc.sso.client.session;

import com.jb4dc.base.service.exenum.UserTypeEnum;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.client.remote.OrganRuntimeRemote;
import com.jb4dc.sso.client.remote.UserRuntimeRemote;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class SSOSessionUtility {
    static OrganRuntimeRemote organRuntimeRemote;

    static UserRuntimeRemote userRuntimeRemote;

    @Autowired
    public SSOSessionUtility(OrganRuntimeRemote _organRuntimeRemote,UserRuntimeRemote _userRuntimeRemote) {
        organRuntimeRemote=_organRuntimeRemote;
        userRuntimeRemote=_userRuntimeRemote;
    }

    public static JB4DCSession buildJB4DCSessionFromRemote(String userId, String organId) throws JBuild4DCGenerallyException {
        UserEntity userEntity=userRuntimeRemote.getUserById(userId).getData();
        OrganEntity organEntity=organRuntimeRemote.getOrganById(organId).getData();

        JB4DCSession jb4DCSession=new JB4DCSession();
        jb4DCSession.setAppClientToken("");
        jb4DCSession.setCookieSessionId("");
        jb4DCSession.setJaSessionId("");
        jb4DCSession.setOrganId(organEntity.getOrganId());
        jb4DCSession.setOrganCode(organEntity.getOrganCode());
        jb4DCSession.setOrganName(organEntity.getOrganName());
        jb4DCSession.setUserId(userEntity.getUserId());
        jb4DCSession.setUserName(userEntity.getUserName());
        jb4DCSession.setAccountId(userEntity.getUserAccount());
        jb4DCSession.setAccountName(userEntity.getUserAccount());
        jb4DCSession.setMainDepartmentId("");
        jb4DCSession.setMainDepartmentName("");
        jb4DCSession.setRoleKeys(new ArrayList<>());
        jb4DCSession.setRoleNames(new ArrayList<>());
        jb4DCSession.setExMap(new HashMap<>());
        if (userEntity.getUserType().equals(UserTypeEnum.manager.getDisplayName())) {
            jb4DCSession.setFullAuthority(true);
        } else {
            jb4DCSession.setFullAuthority(false);
        }
        //jb4DCSession.setFullAuthority(false);
        jb4DCSession.setSsoSessionToken("");
        return jb4DCSession;
    }
}
