package com.jb4dc.sso.service.user;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dbentities.user.UserRoleEntity;

import java.util.List;

public interface IUserRoleService extends IBaseService<UserRoleEntity> {
    void bindUsersWithRole(JB4DCSession jb4DSession, String roleId, List<String> userIds) throws JBuild4DCGenerallyException;

    boolean bindExist(JB4DCSession jb4DSession, String roleId, String userId);

    void deleteUserRoleBind(String roleId, String userId);

    void clearAllRoleMember(String roleId);
}
