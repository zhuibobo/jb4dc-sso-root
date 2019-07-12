package com.jb4dc.sso.dao.user;

import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.sso.dbentities.user.UserEntity;

import java.util.List;

public interface UserMapper extends BaseMapper<UserEntity> {
    UserEntity selectByAccount(String userAccount);

    List<UserEntity> selectBindRoleUsers(String roleId);
}
