package com.jb4dc.sso.dbentities.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_SSO_APP_USER_MAPPING
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class SsoAppUserMappingEntity {
    //MAPPING_ID:主键:UUID
    @DBKeyField
    private String mappingId;

    //MAPPING_BELONG_APP_ID:所属系统ID
    private String mappingBelongAppId;

    //MAPPING_ACCOUNT:账号
    private String mappingAccount;

    //MAPPING_PASSWORD:密码
    private String mappingPassword;

    //MAPPING_CREATE_TIME:创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date mappingCreateTime;

    //MAPPING_CREATOR_ID:创建者的ID
    private String mappingCreatorId;

    //MAPPING_ORGAN_ID:创建组织ID
    private String mappingOrganId;

    /**
     * 构造函数
     * @param mappingId 主键
     * @param mappingBelongAppId 所属系统ID
     * @param mappingAccount 账号
     * @param mappingPassword 密码
     * @param mappingCreateTime 创建时间
     * @param mappingCreatorId 创建者的ID
     * @param mappingOrganId 创建组织ID
     **/
    public SsoAppUserMappingEntity(String mappingId, String mappingBelongAppId, String mappingAccount, String mappingPassword, Date mappingCreateTime, String mappingCreatorId, String mappingOrganId) {
        this.mappingId = mappingId;
        this.mappingBelongAppId = mappingBelongAppId;
        this.mappingAccount = mappingAccount;
        this.mappingPassword = mappingPassword;
        this.mappingCreateTime = mappingCreateTime;
        this.mappingCreatorId = mappingCreatorId;
        this.mappingOrganId = mappingOrganId;
    }

    public SsoAppUserMappingEntity() {
        super();
    }

    /**
     * 主键:UUID
     * @return java.lang.String
     **/
    public String getMappingId() {
        return mappingId;
    }

    /**
     * 主键:UUID
     * @param mappingId 主键
     **/
    public void setMappingId(String mappingId) {
        this.mappingId = mappingId == null ? null : mappingId.trim();
    }

    /**
     * 所属系统ID
     * @return java.lang.String
     **/
    public String getMappingBelongAppId() {
        return mappingBelongAppId;
    }

    /**
     * 所属系统ID
     * @param mappingBelongAppId 所属系统ID
     **/
    public void setMappingBelongAppId(String mappingBelongAppId) {
        this.mappingBelongAppId = mappingBelongAppId == null ? null : mappingBelongAppId.trim();
    }

    /**
     * 账号
     * @return java.lang.String
     **/
    public String getMappingAccount() {
        return mappingAccount;
    }

    /**
     * 账号
     * @param mappingAccount 账号
     **/
    public void setMappingAccount(String mappingAccount) {
        this.mappingAccount = mappingAccount == null ? null : mappingAccount.trim();
    }

    /**
     * 密码
     * @return java.lang.String
     **/
    public String getMappingPassword() {
        return mappingPassword;
    }

    /**
     * 密码
     * @param mappingPassword 密码
     **/
    public void setMappingPassword(String mappingPassword) {
        this.mappingPassword = mappingPassword == null ? null : mappingPassword.trim();
    }

    /**
     * 创建时间
     * @return java.util.Date
     **/
    public Date getMappingCreateTime() {
        return mappingCreateTime;
    }

    /**
     * 创建时间
     * @param mappingCreateTime 创建时间
     **/
    public void setMappingCreateTime(Date mappingCreateTime) {
        this.mappingCreateTime = mappingCreateTime;
    }

    /**
     * 创建者的ID
     * @return java.lang.String
     **/
    public String getMappingCreatorId() {
        return mappingCreatorId;
    }

    /**
     * 创建者的ID
     * @param mappingCreatorId 创建者的ID
     **/
    public void setMappingCreatorId(String mappingCreatorId) {
        this.mappingCreatorId = mappingCreatorId == null ? null : mappingCreatorId.trim();
    }

    /**
     * 创建组织ID
     * @return java.lang.String
     **/
    public String getMappingOrganId() {
        return mappingOrganId;
    }

    /**
     * 创建组织ID
     * @param mappingOrganId 创建组织ID
     **/
    public void setMappingOrganId(String mappingOrganId) {
        this.mappingOrganId = mappingOrganId == null ? null : mappingOrganId.trim();
    }
}