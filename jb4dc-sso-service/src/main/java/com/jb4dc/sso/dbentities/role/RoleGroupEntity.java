package com.jb4dc.sso.dbentities.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_ROLE_GROUP
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class RoleGroupEntity {
    //ROLE_GROUP_ID:主键:UUID
    @DBKeyField
    private String roleGroupId;

    //ROLE_GROUP_NAME:角色组名称
    private String roleGroupName;

    //ROLE_GROUP_ORDER_NUM:排序号
    private Integer roleGroupOrderNum;

    //ROLE_GROUP_CREATE_TIME:创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date roleGroupCreateTime;

    //ROLE_GROUP_DESC:备注
    private String roleGroupDesc;

    //ROLE_GROUP_STATUS:状态
    private String roleGroupStatus;

    //ROLE_GROUP_PARENT_ID:父节点ID
    private String roleGroupParentId;

    //ROLE_GROUP_IS_SYSTEM:是否系统
    private String roleGroupIsSystem;

    //ROLE_GROUP_DEL_ENABLE:能否删除
    private String roleGroupDelEnable;

    //ROLE_GROUP_PID_LIST:父节点列表
    private String roleGroupPidList;

    //ROLE_GROUP_CHILD_COUNT:子节点数量
    private Integer roleGroupChildCount;

    //ROLE_GROUP_CREATOR_ID:创建者的ID
    private String roleGroupCreatorId;

    //ROLE_GROUP_ORGAN_ID:创建组织ID
    private String roleGroupOrganId;

    /**
     * 构造函数
     * @param roleGroupId 主键
     * @param roleGroupName 角色组名称
     * @param roleGroupOrderNum 排序号
     * @param roleGroupCreateTime 创建时间
     * @param roleGroupDesc 备注
     * @param roleGroupStatus 状态
     * @param roleGroupParentId 父节点ID
     * @param roleGroupIsSystem 是否系统
     * @param roleGroupDelEnable 能否删除
     * @param roleGroupPidList 父节点列表
     * @param roleGroupChildCount 子节点数量
     * @param roleGroupCreatorId 创建者的ID
     * @param roleGroupOrganId 创建组织ID
     **/
    public RoleGroupEntity(String roleGroupId, String roleGroupName, Integer roleGroupOrderNum, Date roleGroupCreateTime, String roleGroupDesc, String roleGroupStatus, String roleGroupParentId, String roleGroupIsSystem, String roleGroupDelEnable, String roleGroupPidList, Integer roleGroupChildCount, String roleGroupCreatorId, String roleGroupOrganId) {
        this.roleGroupId = roleGroupId;
        this.roleGroupName = roleGroupName;
        this.roleGroupOrderNum = roleGroupOrderNum;
        this.roleGroupCreateTime = roleGroupCreateTime;
        this.roleGroupDesc = roleGroupDesc;
        this.roleGroupStatus = roleGroupStatus;
        this.roleGroupParentId = roleGroupParentId;
        this.roleGroupIsSystem = roleGroupIsSystem;
        this.roleGroupDelEnable = roleGroupDelEnable;
        this.roleGroupPidList = roleGroupPidList;
        this.roleGroupChildCount = roleGroupChildCount;
        this.roleGroupCreatorId = roleGroupCreatorId;
        this.roleGroupOrganId = roleGroupOrganId;
    }

    public RoleGroupEntity() {
        super();
    }

    /**
     * 主键:UUID
     * @return java.lang.String
     **/
    public String getRoleGroupId() {
        return roleGroupId;
    }

    /**
     * 主键:UUID
     * @param roleGroupId 主键
     **/
    public void setRoleGroupId(String roleGroupId) {
        this.roleGroupId = roleGroupId == null ? null : roleGroupId.trim();
    }

    /**
     * 角色组名称
     * @return java.lang.String
     **/
    public String getRoleGroupName() {
        return roleGroupName;
    }

    /**
     * 角色组名称
     * @param roleGroupName 角色组名称
     **/
    public void setRoleGroupName(String roleGroupName) {
        this.roleGroupName = roleGroupName == null ? null : roleGroupName.trim();
    }

    /**
     * 排序号
     * @return java.lang.Integer
     **/
    public Integer getRoleGroupOrderNum() {
        return roleGroupOrderNum;
    }

    /**
     * 排序号
     * @param roleGroupOrderNum 排序号
     **/
    public void setRoleGroupOrderNum(Integer roleGroupOrderNum) {
        this.roleGroupOrderNum = roleGroupOrderNum;
    }

    /**
     * 创建时间
     * @return java.util.Date
     **/
    public Date getRoleGroupCreateTime() {
        return roleGroupCreateTime;
    }

    /**
     * 创建时间
     * @param roleGroupCreateTime 创建时间
     **/
    public void setRoleGroupCreateTime(Date roleGroupCreateTime) {
        this.roleGroupCreateTime = roleGroupCreateTime;
    }

    /**
     * 备注
     * @return java.lang.String
     **/
    public String getRoleGroupDesc() {
        return roleGroupDesc;
    }

    /**
     * 备注
     * @param roleGroupDesc 备注
     **/
    public void setRoleGroupDesc(String roleGroupDesc) {
        this.roleGroupDesc = roleGroupDesc == null ? null : roleGroupDesc.trim();
    }

    /**
     * 状态
     * @return java.lang.String
     **/
    public String getRoleGroupStatus() {
        return roleGroupStatus;
    }

    /**
     * 状态
     * @param roleGroupStatus 状态
     **/
    public void setRoleGroupStatus(String roleGroupStatus) {
        this.roleGroupStatus = roleGroupStatus == null ? null : roleGroupStatus.trim();
    }

    /**
     * 父节点ID
     * @return java.lang.String
     **/
    public String getRoleGroupParentId() {
        return roleGroupParentId;
    }

    /**
     * 父节点ID
     * @param roleGroupParentId 父节点ID
     **/
    public void setRoleGroupParentId(String roleGroupParentId) {
        this.roleGroupParentId = roleGroupParentId == null ? null : roleGroupParentId.trim();
    }

    /**
     * 是否系统
     * @return java.lang.String
     **/
    public String getRoleGroupIsSystem() {
        return roleGroupIsSystem;
    }

    /**
     * 是否系统
     * @param roleGroupIsSystem 是否系统
     **/
    public void setRoleGroupIsSystem(String roleGroupIsSystem) {
        this.roleGroupIsSystem = roleGroupIsSystem == null ? null : roleGroupIsSystem.trim();
    }

    /**
     * 能否删除
     * @return java.lang.String
     **/
    public String getRoleGroupDelEnable() {
        return roleGroupDelEnable;
    }

    /**
     * 能否删除
     * @param roleGroupDelEnable 能否删除
     **/
    public void setRoleGroupDelEnable(String roleGroupDelEnable) {
        this.roleGroupDelEnable = roleGroupDelEnable == null ? null : roleGroupDelEnable.trim();
    }

    /**
     * 父节点列表
     * @return java.lang.String
     **/
    public String getRoleGroupPidList() {
        return roleGroupPidList;
    }

    /**
     * 父节点列表
     * @param roleGroupPidList 父节点列表
     **/
    public void setRoleGroupPidList(String roleGroupPidList) {
        this.roleGroupPidList = roleGroupPidList == null ? null : roleGroupPidList.trim();
    }

    /**
     * 子节点数量
     * @return java.lang.Integer
     **/
    public Integer getRoleGroupChildCount() {
        return roleGroupChildCount;
    }

    /**
     * 子节点数量
     * @param roleGroupChildCount 子节点数量
     **/
    public void setRoleGroupChildCount(Integer roleGroupChildCount) {
        this.roleGroupChildCount = roleGroupChildCount;
    }

    /**
     * 创建者的ID
     * @return java.lang.String
     **/
    public String getRoleGroupCreatorId() {
        return roleGroupCreatorId;
    }

    /**
     * 创建者的ID
     * @param roleGroupCreatorId 创建者的ID
     **/
    public void setRoleGroupCreatorId(String roleGroupCreatorId) {
        this.roleGroupCreatorId = roleGroupCreatorId == null ? null : roleGroupCreatorId.trim();
    }

    /**
     * 创建组织ID
     * @return java.lang.String
     **/
    public String getRoleGroupOrganId() {
        return roleGroupOrganId;
    }

    /**
     * 创建组织ID
     * @param roleGroupOrganId 创建组织ID
     **/
    public void setRoleGroupOrganId(String roleGroupOrganId) {
        this.roleGroupOrganId = roleGroupOrganId == null ? null : roleGroupOrganId.trim();
    }
}