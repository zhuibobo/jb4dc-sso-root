package com.jb4dc.sso.dbentities.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_USER_ROLE
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class UserRoleEntity {
    //BIND_ID:主键:UUID
    @DBKeyField
    private String bindId;

    //BIND_ROLE_ID:角色ID
    private String bindRoleId;

    //BIND_USER_ID:角色ID
    private String bindUserId;

    //BIND_ORDER_NUM:排序号
    private Integer bindOrderNum;

    //BIND_CREATE_TIME:创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date bindCreateTime;

    //BIND_CREATOR_ID:创建者的ID
    private String bindCreatorId;

    //BIND_ORGAN_ID:组织ID
    private String bindOrganId;

    /**
     * 构造函数
     * @param bindId 主键
     * @param bindRoleId 角色ID
     * @param bindUserId 角色ID
     * @param bindOrderNum 排序号
     * @param bindCreateTime 创建时间
     * @param bindCreatorId 创建者的ID
     * @param bindOrganId 组织ID
     **/
    public UserRoleEntity(String bindId, String bindRoleId, String bindUserId, Integer bindOrderNum, Date bindCreateTime, String bindCreatorId, String bindOrganId) {
        this.bindId = bindId;
        this.bindRoleId = bindRoleId;
        this.bindUserId = bindUserId;
        this.bindOrderNum = bindOrderNum;
        this.bindCreateTime = bindCreateTime;
        this.bindCreatorId = bindCreatorId;
        this.bindOrganId = bindOrganId;
    }

    public UserRoleEntity() {
        super();
    }

    /**
     * 主键:UUID
     * @return java.lang.String
     **/
    public String getBindId() {
        return bindId;
    }

    /**
     * 主键:UUID
     * @param bindId 主键
     **/
    public void setBindId(String bindId) {
        this.bindId = bindId == null ? null : bindId.trim();
    }

    /**
     * 角色ID
     * @return java.lang.String
     **/
    public String getBindRoleId() {
        return bindRoleId;
    }

    /**
     * 角色ID
     * @param bindRoleId 角色ID
     **/
    public void setBindRoleId(String bindRoleId) {
        this.bindRoleId = bindRoleId == null ? null : bindRoleId.trim();
    }

    /**
     * 角色ID
     * @return java.lang.String
     **/
    public String getBindUserId() {
        return bindUserId;
    }

    /**
     * 角色ID
     * @param bindUserId 角色ID
     **/
    public void setBindUserId(String bindUserId) {
        this.bindUserId = bindUserId == null ? null : bindUserId.trim();
    }

    /**
     * 排序号
     * @return java.lang.Integer
     **/
    public Integer getBindOrderNum() {
        return bindOrderNum;
    }

    /**
     * 排序号
     * @param bindOrderNum 排序号
     **/
    public void setBindOrderNum(Integer bindOrderNum) {
        this.bindOrderNum = bindOrderNum;
    }

    /**
     * 创建时间
     * @return java.util.Date
     **/
    public Date getBindCreateTime() {
        return bindCreateTime;
    }

    /**
     * 创建时间
     * @param bindCreateTime 创建时间
     **/
    public void setBindCreateTime(Date bindCreateTime) {
        this.bindCreateTime = bindCreateTime;
    }

    /**
     * 创建者的ID
     * @return java.lang.String
     **/
    public String getBindCreatorId() {
        return bindCreatorId;
    }

    /**
     * 创建者的ID
     * @param bindCreatorId 创建者的ID
     **/
    public void setBindCreatorId(String bindCreatorId) {
        this.bindCreatorId = bindCreatorId == null ? null : bindCreatorId.trim();
    }

    /**
     * 组织ID
     * @return java.lang.String
     **/
    public String getBindOrganId() {
        return bindOrganId;
    }

    /**
     * 组织ID
     * @param bindOrganId 组织ID
     **/
    public void setBindOrganId(String bindOrganId) {
        this.bindOrganId = bindOrganId == null ? null : bindOrganId.trim();
    }
}