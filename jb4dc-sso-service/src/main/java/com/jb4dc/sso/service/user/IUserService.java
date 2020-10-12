package com.jb4dc.sso.service.user;

import com.github.pagehelper.PageInfo;
import com.jb4dc.base.service.IBaseService;
import com.jb4dc.sso.dbentities.user.UserEntity;

import java.util.List;

public interface IUserService extends IBaseService<UserEntity> {
    public static final String USER_TYPE_MANAGER = "平台管理员";
    public static final String USER_TYPE_ORGAN_ADMIN = "组织管理员";
    public static final String USER_TYPE_NORMAL_USER="一般用户";

    UserEntity getByAccount(String userAccount);

    PageInfo<UserEntity> getBindRoleUsers(String roleId, int pageNum, int pageSize);

    List<UserEntity> getByOrganId(String organId);
}
