package com.jb4dc.sso.dbentities.authority;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_AUTHORITY
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class AuthorityEntity {
    //AUTH_ID:主键:UUID
    @DBKeyField
    private String authId;

    //ROLE_ID:主键:UUID
    private String roleId;

    /**
     * 构造函数
     * @param authId 主键
     * @param roleId 主键
     **/
    public AuthorityEntity(String authId, String roleId) {
        this.authId = authId;
        this.roleId = roleId;
    }

    public AuthorityEntity() {
        super();
    }

    /**
     * 主键:UUID
     * @return java.lang.String
     **/
    public String getAuthId() {
        return authId;
    }

    /**
     * 主键:UUID
     * @param authId 主键
     **/
    public void setAuthId(String authId) {
        this.authId = authId == null ? null : authId.trim();
    }

    /**
     * 主键:UUID
     * @return java.lang.String
     **/
    public String getRoleId() {
        return roleId;
    }

    /**
     * 主键:UUID
     * @param roleId 主键
     **/
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }
}