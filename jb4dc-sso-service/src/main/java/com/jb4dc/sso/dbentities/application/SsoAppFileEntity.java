package com.jb4dc.sso.dbentities.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_SSO_APP_FILE
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class SsoAppFileEntity {
    //APP_FILE_ID:主键:UUID
    @DBKeyField
    private String appFileId;

    //APP_BELONG_APP_ID:所属系统ID
    private String appBelongAppId;

    //APP_FILE_NAME:文件名称
    private String appFileName;

    //APP_FILE_IS_MAIN:是否是主文件:用于Post模拟时的主执行文件
    private String appFileIsMain;

    //APP_FILE_DESC:备注
    private String appFileDesc;

    //APP_FILE_ORDER_NUM:排序号
    private Integer appFileOrderNum;

    //APP_FILE_CREATE_TIME:创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date appFileCreateTime;

    //APP_FILE_STATUS:状态
    private String appFileStatus;

    //APP_FILE_CREATOR_ID:创建者的ID
    private String appFileCreatorId;

    //APP_FILE_ORGAN_ID:创建组织ID
    private String appFileOrganId;

    //APP_FILE_CNT_FILE_ID:存储的文件ID:关联到TFS_FILE_INFO表的FILE_ID
    private String appFileCntFileId;

    /**
     * 构造函数
     * @param appFileId 主键
     * @param appBelongAppId 所属系统ID
     * @param appFileName 文件名称
     * @param appFileIsMain 是否是主文件
     * @param appFileDesc 备注
     * @param appFileOrderNum 排序号
     * @param appFileCreateTime 创建时间
     * @param appFileStatus 状态
     * @param appFileCreatorId 创建者的ID
     * @param appFileOrganId 创建组织ID
     * @param appFileCntFileId 存储的文件ID
     **/
    public SsoAppFileEntity(String appFileId, String appBelongAppId, String appFileName, String appFileIsMain, String appFileDesc, Integer appFileOrderNum, Date appFileCreateTime, String appFileStatus, String appFileCreatorId, String appFileOrganId, String appFileCntFileId) {
        this.appFileId = appFileId;
        this.appBelongAppId = appBelongAppId;
        this.appFileName = appFileName;
        this.appFileIsMain = appFileIsMain;
        this.appFileDesc = appFileDesc;
        this.appFileOrderNum = appFileOrderNum;
        this.appFileCreateTime = appFileCreateTime;
        this.appFileStatus = appFileStatus;
        this.appFileCreatorId = appFileCreatorId;
        this.appFileOrganId = appFileOrganId;
        this.appFileCntFileId = appFileCntFileId;
    }

    public SsoAppFileEntity() {
        super();
    }

    /**
     * 主键:UUID
     * @return java.lang.String
     **/
    public String getAppFileId() {
        return appFileId;
    }

    /**
     * 主键:UUID
     * @param appFileId 主键
     **/
    public void setAppFileId(String appFileId) {
        this.appFileId = appFileId == null ? null : appFileId.trim();
    }

    /**
     * 所属系统ID
     * @return java.lang.String
     **/
    public String getAppBelongAppId() {
        return appBelongAppId;
    }

    /**
     * 所属系统ID
     * @param appBelongAppId 所属系统ID
     **/
    public void setAppBelongAppId(String appBelongAppId) {
        this.appBelongAppId = appBelongAppId == null ? null : appBelongAppId.trim();
    }

    /**
     * 文件名称
     * @return java.lang.String
     **/
    public String getAppFileName() {
        return appFileName;
    }

    /**
     * 文件名称
     * @param appFileName 文件名称
     **/
    public void setAppFileName(String appFileName) {
        this.appFileName = appFileName == null ? null : appFileName.trim();
    }

    /**
     * 是否是主文件:用于Post模拟时的主执行文件
     * @return java.lang.String
     **/
    public String getAppFileIsMain() {
        return appFileIsMain;
    }

    /**
     * 是否是主文件:用于Post模拟时的主执行文件
     * @param appFileIsMain 是否是主文件
     **/
    public void setAppFileIsMain(String appFileIsMain) {
        this.appFileIsMain = appFileIsMain == null ? null : appFileIsMain.trim();
    }

    /**
     * 备注
     * @return java.lang.String
     **/
    public String getAppFileDesc() {
        return appFileDesc;
    }

    /**
     * 备注
     * @param appFileDesc 备注
     **/
    public void setAppFileDesc(String appFileDesc) {
        this.appFileDesc = appFileDesc == null ? null : appFileDesc.trim();
    }

    /**
     * 排序号
     * @return java.lang.Integer
     **/
    public Integer getAppFileOrderNum() {
        return appFileOrderNum;
    }

    /**
     * 排序号
     * @param appFileOrderNum 排序号
     **/
    public void setAppFileOrderNum(Integer appFileOrderNum) {
        this.appFileOrderNum = appFileOrderNum;
    }

    /**
     * 创建时间
     * @return java.util.Date
     **/
    public Date getAppFileCreateTime() {
        return appFileCreateTime;
    }

    /**
     * 创建时间
     * @param appFileCreateTime 创建时间
     **/
    public void setAppFileCreateTime(Date appFileCreateTime) {
        this.appFileCreateTime = appFileCreateTime;
    }

    /**
     * 状态
     * @return java.lang.String
     **/
    public String getAppFileStatus() {
        return appFileStatus;
    }

    /**
     * 状态
     * @param appFileStatus 状态
     **/
    public void setAppFileStatus(String appFileStatus) {
        this.appFileStatus = appFileStatus == null ? null : appFileStatus.trim();
    }

    /**
     * 创建者的ID
     * @return java.lang.String
     **/
    public String getAppFileCreatorId() {
        return appFileCreatorId;
    }

    /**
     * 创建者的ID
     * @param appFileCreatorId 创建者的ID
     **/
    public void setAppFileCreatorId(String appFileCreatorId) {
        this.appFileCreatorId = appFileCreatorId == null ? null : appFileCreatorId.trim();
    }

    /**
     * 创建组织ID
     * @return java.lang.String
     **/
    public String getAppFileOrganId() {
        return appFileOrganId;
    }

    /**
     * 创建组织ID
     * @param appFileOrganId 创建组织ID
     **/
    public void setAppFileOrganId(String appFileOrganId) {
        this.appFileOrganId = appFileOrganId == null ? null : appFileOrganId.trim();
    }

    /**
     * 存储的文件ID:关联到TFS_FILE_INFO表的FILE_ID
     * @return java.lang.String
     **/
    public String getAppFileCntFileId() {
        return appFileCntFileId;
    }

    /**
     * 存储的文件ID:关联到TFS_FILE_INFO表的FILE_ID
     * @param appFileCntFileId 存储的文件ID
     **/
    public void setAppFileCntFileId(String appFileCntFileId) {
        this.appFileCntFileId = appFileCntFileId == null ? null : appFileCntFileId.trim();
    }
}