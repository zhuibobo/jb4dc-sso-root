package com.jb4dc.sso.dbentities.department;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_DEPARTMENT_USER
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class DepartmentUserEntity {
    //DU_ID:主键:UUID
    @DBKeyField
    private String duId;

    //DU_DEPT_ID:部门ID
    private String duDeptId;

    //DU_USER_ID:用户ID
    private String duUserId;

    //DU_IS_MAIN:是否主属
    private String duIsMain;

    //DU_TITLE:部门用户职位
    private String duTitle;

    //DU_DESC:部门用户备注
    private String duDesc;

    //DU_CREATE_TIME:创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date duCreateTime;

    //DU_CREATOR_ID:创建用户ID
    private String duCreatorId;

    //DU_STATUS:状态:启用,禁用
    private String duStatus;

    //DU_ORDER_NUM:排序号
    private Integer duOrderNum;

    /**
     * 构造函数
     * @param duId 主键
     * @param duDeptId 部门ID
     * @param duUserId 用户ID
     * @param duIsMain 是否主属
     * @param duTitle 部门用户职位
     * @param duDesc 部门用户备注
     * @param duCreateTime 创建时间
     * @param duCreatorId 创建用户ID
     * @param duStatus 状态
     * @param duOrderNum 排序号
     **/
    public DepartmentUserEntity(String duId, String duDeptId, String duUserId, String duIsMain, String duTitle, String duDesc, Date duCreateTime, String duCreatorId, String duStatus, Integer duOrderNum) {
        this.duId = duId;
        this.duDeptId = duDeptId;
        this.duUserId = duUserId;
        this.duIsMain = duIsMain;
        this.duTitle = duTitle;
        this.duDesc = duDesc;
        this.duCreateTime = duCreateTime;
        this.duCreatorId = duCreatorId;
        this.duStatus = duStatus;
        this.duOrderNum = duOrderNum;
    }

    public DepartmentUserEntity() {
        super();
    }

    /**
     * 主键:UUID
     * @return java.lang.String
     **/
    public String getDuId() {
        return duId;
    }

    /**
     * 主键:UUID
     * @param duId 主键
     **/
    public void setDuId(String duId) {
        this.duId = duId == null ? null : duId.trim();
    }

    /**
     * 部门ID
     * @return java.lang.String
     **/
    public String getDuDeptId() {
        return duDeptId;
    }

    /**
     * 部门ID
     * @param duDeptId 部门ID
     **/
    public void setDuDeptId(String duDeptId) {
        this.duDeptId = duDeptId == null ? null : duDeptId.trim();
    }

    /**
     * 用户ID
     * @return java.lang.String
     **/
    public String getDuUserId() {
        return duUserId;
    }

    /**
     * 用户ID
     * @param duUserId 用户ID
     **/
    public void setDuUserId(String duUserId) {
        this.duUserId = duUserId == null ? null : duUserId.trim();
    }

    /**
     * 是否主属
     * @return java.lang.String
     **/
    public String getDuIsMain() {
        return duIsMain;
    }

    /**
     * 是否主属
     * @param duIsMain 是否主属
     **/
    public void setDuIsMain(String duIsMain) {
        this.duIsMain = duIsMain == null ? null : duIsMain.trim();
    }

    /**
     * 部门用户职位
     * @return java.lang.String
     **/
    public String getDuTitle() {
        return duTitle;
    }

    /**
     * 部门用户职位
     * @param duTitle 部门用户职位
     **/
    public void setDuTitle(String duTitle) {
        this.duTitle = duTitle == null ? null : duTitle.trim();
    }

    /**
     * 部门用户备注
     * @return java.lang.String
     **/
    public String getDuDesc() {
        return duDesc;
    }

    /**
     * 部门用户备注
     * @param duDesc 部门用户备注
     **/
    public void setDuDesc(String duDesc) {
        this.duDesc = duDesc == null ? null : duDesc.trim();
    }

    /**
     * 创建时间
     * @return java.util.Date
     **/
    public Date getDuCreateTime() {
        return duCreateTime;
    }

    /**
     * 创建时间
     * @param duCreateTime 创建时间
     **/
    public void setDuCreateTime(Date duCreateTime) {
        this.duCreateTime = duCreateTime;
    }

    /**
     * 创建用户ID
     * @return java.lang.String
     **/
    public String getDuCreatorId() {
        return duCreatorId;
    }

    /**
     * 创建用户ID
     * @param duCreatorId 创建用户ID
     **/
    public void setDuCreatorId(String duCreatorId) {
        this.duCreatorId = duCreatorId == null ? null : duCreatorId.trim();
    }

    /**
     * 状态:启用,禁用
     * @return java.lang.String
     **/
    public String getDuStatus() {
        return duStatus;
    }

    /**
     * 状态:启用,禁用
     * @param duStatus 状态
     **/
    public void setDuStatus(String duStatus) {
        this.duStatus = duStatus == null ? null : duStatus.trim();
    }

    /**
     * 排序号
     * @return java.lang.Integer
     **/
    public Integer getDuOrderNum() {
        return duOrderNum;
    }

    /**
     * 排序号
     * @param duOrderNum 排序号
     **/
    public void setDuOrderNum(Integer duOrderNum) {
        this.duOrderNum = duOrderNum;
    }
}