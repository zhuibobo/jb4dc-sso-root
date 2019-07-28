package com.jb4dc.sso.dbentities.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_ROLE
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class RoleEntity {
    //ROLE_ID:主键:UUID
    @DBKeyField
    private String roleId;

    //ROLE_KEY:角色键值
    private String roleKey;

    //ROLE_NAME:角色组ID
    private String roleName;

    //ROLE_GROUP_ID:角色名称
    private String roleGroupId;

    //ROLE_ORDER_NUM:排序号
    private Integer roleOrderNum;

    //ROLE_CREATE_TIME:创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date roleCreateTime;

    //ROLE_DESC:备注
    private String roleDesc;

    //ROLE_STATUS:状态
    private String roleStatus;

    //ROLE_CREATOR_ID:创建者的ID
    private String roleCreatorId;

    //ROLE_ORGAN_ID:创建组织ID
    private String roleOrganId;

    /**
     * 构造函数
     * @param roleId 主键
     * @param roleKey 角色键值
     * @param roleName 角色组ID
     * @param roleGroupId 角色名称
     * @param roleOrderNum 排序号
     * @param roleCreateTime 创建时间
     * @param roleDesc 备注
     * @param roleStatus 状态
     * @param roleCreatorId 创建者的ID
     * @param roleOrganId 创建组织ID
     **/
    public RoleEntity(String roleId, String roleKey, String roleName, String roleGroupId, Integer roleOrderNum, Date roleCreateTime, String roleDesc, String roleStatus, String roleCreatorId, String roleOrganId) {
        this.roleId = roleId;
        this.roleKey = roleKey;
        this.roleName = roleName;
        this.roleGroupId = roleGroupId;
        this.roleOrderNum = roleOrderNum;
        this.roleCreateTime = roleCreateTime;
        this.roleDesc = roleDesc;
        this.roleStatus = roleStatus;
        this.roleCreatorId = roleCreatorId;
        this.roleOrganId = roleOrganId;
    }

    public RoleEntity() {
        super();
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

    /**
     * 角色键值
     * @return java.lang.String
     **/
    public String getRoleKey() {
        return roleKey;
    }

    /**
     * 角色键值
     * @param roleKey 角色键值
     **/
    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey == null ? null : roleKey.trim();
    }

    /**
     * 角色组ID
     * @return java.lang.String
     **/
    public String getRoleName() {
        return roleName;
    }

    /**
     * 角色组ID
     * @param roleName 角色组ID
     **/
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * 角色名称
     * @return java.lang.String
     **/
    public String getRoleGroupId() {
        return roleGroupId;
    }

    /**
     * 角色名称
     * @param roleGroupId 角色名称
     **/
    public void setRoleGroupId(String roleGroupId) {
        this.roleGroupId = roleGroupId == null ? null : roleGroupId.trim();
    }

    /**
     * 排序号
     * @return java.lang.Integer
     **/
    public Integer getRoleOrderNum() {
        return roleOrderNum;
    }

    /**
     * 排序号
     * @param roleOrderNum 排序号
     **/
    public void setRoleOrderNum(Integer roleOrderNum) {
        this.roleOrderNum = roleOrderNum;
    }

    /**
     * 创建时间
     * @return java.util.Date
     **/
    public Date getRoleCreateTime() {
        return roleCreateTime;
    }

    /**
     * 创建时间
     * @param roleCreateTime 创建时间
     **/
    public void setRoleCreateTime(Date roleCreateTime) {
        this.roleCreateTime = roleCreateTime;
    }

    /**
     * 备注
     * @return java.lang.String
     **/
    public String getRoleDesc() {
        return roleDesc;
    }

    /**
     * 备注
     * @param roleDesc 备注
     **/
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc == null ? null : roleDesc.trim();
    }

    /**
     * 状态
     * @return java.lang.String
     **/
    public String getRoleStatus() {
        return roleStatus;
    }

    /**
     * 状态
     * @param roleStatus 状态
     **/
    public void setRoleStatus(String roleStatus) {
        this.roleStatus = roleStatus == null ? null : roleStatus.trim();
    }

    /**
     * 创建者的ID
     * @return java.lang.String
     **/
    public String getRoleCreatorId() {
        return roleCreatorId;
    }

    /**
     * 创建者的ID
     * @param roleCreatorId 创建者的ID
     **/
    public void setRoleCreatorId(String roleCreatorId) {
        this.roleCreatorId = roleCreatorId == null ? null : roleCreatorId.trim();
    }

    /**
     * 创建组织ID
     * @return java.lang.String
     **/
    public String getRoleOrganId() {
        return roleOrganId;
    }

    /**
     * 创建组织ID
     * @param roleOrganId 创建组织ID
     **/
    public void setRoleOrganId(String roleOrganId) {
        this.roleOrganId = roleOrganId == null ? null : roleOrganId.trim();
    }
}