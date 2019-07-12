package com.jb4dc.sso.dao.user;

import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.sso.dbentities.UserRoleEntity;
import org.apache.ibatis.annotations.Param;

public interface UserRoleMapper extends BaseMapper<UserRoleEntity> {
    int bindExist(@Param("roleId") String roleId, @Param("userId") String userId);

    void deleteUserRoleBind(@Param("roleId") String roleId, @Param("userId") String userId);

    void clearAllRoleMember(String roleId);
}
