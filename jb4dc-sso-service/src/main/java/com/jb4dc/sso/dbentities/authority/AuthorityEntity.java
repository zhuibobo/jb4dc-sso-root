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

    //AUTH_OBJ_ID:权限对象ID:系统,菜单,操作按钮等
    private String authObjId;

    //AUTH_OBJ_TYPE:权限对象类型:System,Menu,Operation等
    private String authObjType;

    //AUTH_OWNER_ID:权限所有者ID:角色ID,用户ID等
    private String authOwnerId;

    //AUTH_OWNER_TYPE:权限所有者类型:Role,User等
    private String authOwnerType;

    //AUTH_CREATOR_ID:创建者的ID
    private String authCreatorId;

    //AUTH_CREATOR_ORGAN_ID:创建组织ID
    private String authCreatorOrganId;

    //AUTH_ORGAN_ID:所在组织ID
    private String authOrganId;

    //AUTH_DESC:权限绑定备注
    private String authDesc;

    /**
     * 构造函数
     * @param authId 主键
     * @param authObjId 权限对象ID
     * @param authObjType 权限对象类型
     * @param authOwnerId 权限所有者ID
     * @param authOwnerType 权限所有者类型
     * @param authCreatorId 创建者的ID
     * @param authCreatorOrganId 创建组织ID
     * @param authOrganId 所在组织ID
     * @param authDesc 权限绑定备注
     **/
    public AuthorityEntity(String authId, String authObjId, String authObjType, String authOwnerId, String authOwnerType, String authCreatorId, String authCreatorOrganId, String authOrganId, String authDesc) {
        this.authId = authId;
        this.authObjId = authObjId;
        this.authObjType = authObjType;
        this.authOwnerId = authOwnerId;
        this.authOwnerType = authOwnerType;
        this.authCreatorId = authCreatorId;
        this.authCreatorOrganId = authCreatorOrganId;
        this.authOrganId = authOrganId;
        this.authDesc = authDesc;
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
     * 权限对象ID:系统,菜单,操作按钮等
     * @return java.lang.String
     **/
    public String getAuthObjId() {
        return authObjId;
    }

    /**
     * 权限对象ID:系统,菜单,操作按钮等
     * @param authObjId 权限对象ID
     **/
    public void setAuthObjId(String authObjId) {
        this.authObjId = authObjId == null ? null : authObjId.trim();
    }

    /**
     * 权限对象类型:System,Menu,Operation等
     * @return java.lang.String
     **/
    public String getAuthObjType() {
        return authObjType;
    }

    /**
     * 权限对象类型:System,Menu,Operation等
     * @param authObjType 权限对象类型
     **/
    public void setAuthObjType(String authObjType) {
        this.authObjType = authObjType == null ? null : authObjType.trim();
    }

    /**
     * 权限所有者ID:角色ID,用户ID等
     * @return java.lang.String
     **/
    public String getAuthOwnerId() {
        return authOwnerId;
    }

    /**
     * 权限所有者ID:角色ID,用户ID等
     * @param authOwnerId 权限所有者ID
     **/
    public void setAuthOwnerId(String authOwnerId) {
        this.authOwnerId = authOwnerId == null ? null : authOwnerId.trim();
    }

    /**
     * 权限所有者类型:Role,User等
     * @return java.lang.String
     **/
    public String getAuthOwnerType() {
        return authOwnerType;
    }

    /**
     * 权限所有者类型:Role,User等
     * @param authOwnerType 权限所有者类型
     **/
    public void setAuthOwnerType(String authOwnerType) {
        this.authOwnerType = authOwnerType == null ? null : authOwnerType.trim();
    }

    /**
     * 创建者的ID
     * @return java.lang.String
     **/
    public String getAuthCreatorId() {
        return authCreatorId;
    }

    /**
     * 创建者的ID
     * @param authCreatorId 创建者的ID
     **/
    public void setAuthCreatorId(String authCreatorId) {
        this.authCreatorId = authCreatorId == null ? null : authCreatorId.trim();
    }

    /**
     * 创建组织ID
     * @return java.lang.String
     **/
    public String getAuthCreatorOrganId() {
        return authCreatorOrganId;
    }

    /**
     * 创建组织ID
     * @param authCreatorOrganId 创建组织ID
     **/
    public void setAuthCreatorOrganId(String authCreatorOrganId) {
        this.authCreatorOrganId = authCreatorOrganId == null ? null : authCreatorOrganId.trim();
    }

    /**
     * 所在组织ID
     * @return java.lang.String
     **/
    public String getAuthOrganId() {
        return authOrganId;
    }

    /**
     * 所在组织ID
     * @param authOrganId 所在组织ID
     **/
    public void setAuthOrganId(String authOrganId) {
        this.authOrganId = authOrganId == null ? null : authOrganId.trim();
    }

    /**
     * 权限绑定备注
     * @return java.lang.String
     **/
    public String getAuthDesc() {
        return authDesc;
    }

    /**
     * 权限绑定备注
     * @param authDesc 权限绑定备注
     **/
    public void setAuthDesc(String authDesc) {
        this.authDesc = authDesc == null ? null : authDesc.trim();
    }
}