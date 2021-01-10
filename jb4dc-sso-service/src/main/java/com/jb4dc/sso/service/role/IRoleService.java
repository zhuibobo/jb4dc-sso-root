package com.jb4dc.sso.service.role;


import com.jb4dc.base.service.IBaseService;
import com.jb4dc.sso.dbentities.role.RoleEntity;

import java.util.List;

public interface IRoleService extends IBaseService<RoleEntity> {
    int countInRoleGroup(String groupId);

    List<RoleEntity> getUserRoleList(String userId);
}