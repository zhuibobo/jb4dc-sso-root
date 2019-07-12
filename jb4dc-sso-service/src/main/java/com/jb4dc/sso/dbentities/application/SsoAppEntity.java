package com.jb4dc.sso.dbentities.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_SSO_APP
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class SsoAppEntity {
    //APP_ID:主键:UUID
    @DBKeyField
    private String appId;

    //APP_CODE:集成系统标识键
    private String appCode;

    //APP_NAME:集成系统名称
    private String appName;

    //APP_PUBLIC_KEY:公钥
    private String appPublicKey;

    //APP_PRIVATE_KEY:私钥
    private String appPrivateKey;

    //APP_DOMAIN:域名或者ip:用于验证来源使用
    private String appDomain;

    //APP_INDEX_URL:主页的地址:用于默认登录完成之后的目标地址
    private String appIndexUrl;

    //APP_INTEGRATED_TYPE:系统的集成类型:开发集成或者模拟登录集成,模拟登录为post模拟账号进行登录
    private String appIntegratedType;

    //APP_MAIN_IMAGE_ID:主题图片ID:关联到TFS_FILE_INFO表的FILE_ID
    private String appMainImageId;

    //APP_TYPE:系统类型:主系统或者为子系统
    private String appType;

    //APP_MAIN_ID:主系统ID:为子系统时,用于存储主系统的ID:TSSO_SSO_APP中的APP_ID
    private String appMainId;

    //APP_CATEGORY:系统分类:App或者Web系统
    private String appCategory;

    //APP_DESC:备注
    private String appDesc;

    //APP_ORDER_NUM:排序号
    private Integer appOrderNum;

    //APP_CREATE_TIME:创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date appCreateTime;

    //APP_STATUS:状态
    private String appStatus;

    //APP_CREATER_ID:创建者的ID
    private String appCreaterId;

    //APP_ORGAN_ID:创建组织ID
    private String appOrganId;

    /**
     * 构造函数
     * @param appId 主键
     * @param appCode 集成系统标识键
     * @param appName 集成系统名称
     * @param appPublicKey 公钥
     * @param appPrivateKey 私钥
     * @param appDomain 域名或者ip
     * @param appIndexUrl 主页的地址
     * @param appIntegratedType 系统的集成类型
     * @param appMainImageId 主题图片ID
     * @param appType 系统类型
     * @param appMainId 主系统ID
     * @param appCategory 系统分类
     * @param appDesc 备注
     * @param appOrderNum 排序号
     * @param appCreateTime 创建时间
     * @param appStatus 状态
     * @param appCreaterId 创建者的ID
     * @param appOrganId 创建组织ID
     **/
    public SsoAppEntity(String appId, String appCode, String appName, String appPublicKey, String appPrivateKey, String appDomain, String appIndexUrl, String appIntegratedType, String appMainImageId, String appType, String appMainId, String appCategory, String appDesc, Integer appOrderNum, Date appCreateTime, String appStatus, String appCreaterId, String appOrganId) {
        this.appId = appId;
        this.appCode = appCode;
        this.appName = appName;
        this.appPublicKey = appPublicKey;
        this.appPrivateKey = appPrivateKey;
        this.appDomain = appDomain;
        this.appIndexUrl = appIndexUrl;
        this.appIntegratedType = appIntegratedType;
        this.appMainImageId = appMainImageId;
        this.appType = appType;
        this.appMainId = appMainId;
        this.appCategory = appCategory;
        this.appDesc = appDesc;
        this.appOrderNum = appOrderNum;
        this.appCreateTime = appCreateTime;
        this.appStatus = appStatus;
        this.appCreaterId = appCreaterId;
        this.appOrganId = appOrganId;
    }

    public SsoAppEntity() {
        super();
    }

    /**
     * 主键:UUID
     * @return java.lang.String
     **/
    public String getAppId() {
        return appId;
    }

    /**
     * 主键:UUID
     * @param appId 主键
     **/
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    /**
     * 集成系统标识键
     * @return java.lang.String
     **/
    public String getAppCode() {
        return appCode;
    }

    /**
     * 集成系统标识键
     * @param appCode 集成系统标识键
     **/
    public void setAppCode(String appCode) {
        this.appCode = appCode == null ? null : appCode.trim();
    }

    /**
     * 集成系统名称
     * @return java.lang.String
     **/
    public String getAppName() {
        return appName;
    }

    /**
     * 集成系统名称
     * @param appName 集成系统名称
     **/
    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    /**
     * 公钥
     * @return java.lang.String
     **/
    public String getAppPublicKey() {
        return appPublicKey;
    }

    /**
     * 公钥
     * @param appPublicKey 公钥
     **/
    public void setAppPublicKey(String appPublicKey) {
        this.appPublicKey = appPublicKey == null ? null : appPublicKey.trim();
    }

    /**
     * 私钥
     * @return java.lang.String
     **/
    public String getAppPrivateKey() {
        return appPrivateKey;
    }

    /**
     * 私钥
     * @param appPrivateKey 私钥
     **/
    public void setAppPrivateKey(String appPrivateKey) {
        this.appPrivateKey = appPrivateKey == null ? null : appPrivateKey.trim();
    }

    /**
     * 域名或者ip:用于验证来源使用
     * @return java.lang.String
     **/
    public String getAppDomain() {
        return appDomain;
    }

    /**
     * 域名或者ip:用于验证来源使用
     * @param appDomain 域名或者ip
     **/
    public void setAppDomain(String appDomain) {
        this.appDomain = appDomain == null ? null : appDomain.trim();
    }

    /**
     * 主页的地址:用于默认登录完成之后的目标地址
     * @return java.lang.String
     **/
    public String getAppIndexUrl() {
        return appIndexUrl;
    }

    /**
     * 主页的地址:用于默认登录完成之后的目标地址
     * @param appIndexUrl 主页的地址
     **/
    public void setAppIndexUrl(String appIndexUrl) {
        this.appIndexUrl = appIndexUrl == null ? null : appIndexUrl.trim();
    }

    /**
     * 系统的集成类型:开发集成或者模拟登录集成,模拟登录为post模拟账号进行登录
     * @return java.lang.String
     **/
    public String getAppIntegratedType() {
        return appIntegratedType;
    }

    /**
     * 系统的集成类型:开发集成或者模拟登录集成,模拟登录为post模拟账号进行登录
     * @param appIntegratedType 系统的集成类型
     **/
    public void setAppIntegratedType(String appIntegratedType) {
        this.appIntegratedType = appIntegratedType == null ? null : appIntegratedType.trim();
    }

    /**
     * 主题图片ID:关联到TFS_FILE_INFO表的FILE_ID
     * @return java.lang.String
     **/
    public String getAppMainImageId() {
        return appMainImageId;
    }

    /**
     * 主题图片ID:关联到TFS_FILE_INFO表的FILE_ID
     * @param appMainImageId 主题图片ID
     **/
    public void setAppMainImageId(String appMainImageId) {
        this.appMainImageId = appMainImageId == null ? null : appMainImageId.trim();
    }

    /**
     * 系统类型:主系统或者为子系统
     * @return java.lang.String
     **/
    public String getAppType() {
        return appType;
    }

    /**
     * 系统类型:主系统或者为子系统
     * @param appType 系统类型
     **/
    public void setAppType(String appType) {
        this.appType = appType == null ? null : appType.trim();
    }

    /**
     * 主系统ID:为子系统时,用于存储主系统的ID:TSSO_SSO_APP中的APP_ID
     * @return java.lang.String
     **/
    public String getAppMainId() {
        return appMainId;
    }

    /**
     * 主系统ID:为子系统时,用于存储主系统的ID:TSSO_SSO_APP中的APP_ID
     * @param appMainId 主系统ID
     **/
    public void setAppMainId(String appMainId) {
        this.appMainId = appMainId == null ? null : appMainId.trim();
    }

    /**
     * 系统分类:App或者Web系统
     * @return java.lang.String
     **/
    public String getAppCategory() {
        return appCategory;
    }

    /**
     * 系统分类:App或者Web系统
     * @param appCategory 系统分类
     **/
    public void setAppCategory(String appCategory) {
        this.appCategory = appCategory == null ? null : appCategory.trim();
    }

    /**
     * 备注
     * @return java.lang.String
     **/
    public String getAppDesc() {
        return appDesc;
    }

    /**
     * 备注
     * @param appDesc 备注
     **/
    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc == null ? null : appDesc.trim();
    }

    /**
     * 排序号
     * @return java.lang.Integer
     **/
    public Integer getAppOrderNum() {
        return appOrderNum;
    }

    /**
     * 排序号
     * @param appOrderNum 排序号
     **/
    public void setAppOrderNum(Integer appOrderNum) {
        this.appOrderNum = appOrderNum;
    }

    /**
     * 创建时间
     * @return java.util.Date
     **/
    public Date getAppCreateTime() {
        return appCreateTime;
    }

    /**
     * 创建时间
     * @param appCreateTime 创建时间
     **/
    public void setAppCreateTime(Date appCreateTime) {
        this.appCreateTime = appCreateTime;
    }

    /**
     * 状态
     * @return java.lang.String
     **/
    public String getAppStatus() {
        return appStatus;
    }

    /**
     * 状态
     * @param appStatus 状态
     **/
    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus == null ? null : appStatus.trim();
    }

    /**
     * 创建者的ID
     * @return java.lang.String
     **/
    public String getAppCreaterId() {
        return appCreaterId;
    }

    /**
     * 创建者的ID
     * @param appCreaterId 创建者的ID
     **/
    public void setAppCreaterId(String appCreaterId) {
        this.appCreaterId = appCreaterId == null ? null : appCreaterId.trim();
    }

    /**
     * 创建组织ID
     * @return java.lang.String
     **/
    public String getAppOrganId() {
        return appOrganId;
    }

    /**
     * 创建组织ID
     * @param appOrganId 创建组织ID
     **/
    public void setAppOrganId(String appOrganId) {
        this.appOrganId = appOrganId == null ? null : appOrganId.trim();
    }
}