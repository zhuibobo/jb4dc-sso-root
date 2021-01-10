package com.jb4dc.sso.dao.role;

import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.sso.dbentities.role.RoleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<RoleEntity> {

    int countInRoleGroup(String groupId);

    RoleEntity selectGreaterThanRecord(@Param("id") String id, @Param("groupId") String roleGroupId);

    RoleEntity selectLessThanRecord(@Param("id") String id, @Param("groupId") String roleGroupId);

    List<RoleEntity> selectUserRoleList(@Param("userId") String userId);
}
