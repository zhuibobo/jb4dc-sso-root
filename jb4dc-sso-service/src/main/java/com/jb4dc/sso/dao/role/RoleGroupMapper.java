package com.jb4dc.sso.dao.role;

import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.sso.dbentities.role.RoleGroupEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleGroupMapper extends BaseMapper<RoleGroupEntity> {
    List<RoleGroupEntity> selectAllOrderByAsc();

    int countChildsRoleGroup(String groupId);

    RoleGroupEntity selectLessThanRecord(@Param("id") String id, @Param("parentId") String roleGroupParentId);

    RoleGroupEntity selectGreaterThanRecord(@Param("id") String id, @Param("parentId") String roleGroupParentId);
}
