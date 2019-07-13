package com.jb4dc.sso.dbentities.organ;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_ORGAN_TYPE
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class OrganTypeEntity {
    //ORGAN_TYPE_ID:组织机构类型ID
    @DBKeyField
    private String organTypeId;

    //ORGAN_TYPE_VALUE:组织机构编码值:唯一,与组织机构表关联
    private String organTypeValue;

    //ORGAN_TYPE_NAME:组织机构类型名称
    private String organTypeName;

    //ORGAN_TYPE_DESC:组织机构类型备注
    private String organTypeDesc;

    //ORGAN_TYPE_ORDER_NUM:排序号
    private Integer organTypeOrderNum;

    //ORGAN_TYPE_STATUS:状态:启用,禁用
    private String organTypeStatus;

    //ORGAN_TYPE_CREATE_TIME:创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date organTypeCreateTime;

    /**
     * 构造函数
     * @param organTypeId 组织机构类型ID
     * @param organTypeValue 组织机构编码值
     * @param organTypeName 组织机构类型名称
     * @param organTypeDesc 组织机构类型备注
     * @param organTypeOrderNum 排序号
     * @param organTypeStatus 状态
     * @param organTypeCreateTime 创建时间
     **/
    public OrganTypeEntity(String organTypeId, String organTypeValue, String organTypeName, String organTypeDesc, Integer organTypeOrderNum, String organTypeStatus, Date organTypeCreateTime) {
        this.organTypeId = organTypeId;
        this.organTypeValue = organTypeValue;
        this.organTypeName = organTypeName;
        this.organTypeDesc = organTypeDesc;
        this.organTypeOrderNum = organTypeOrderNum;
        this.organTypeStatus = organTypeStatus;
        this.organTypeCreateTime = organTypeCreateTime;
    }

    public OrganTypeEntity() {
        super();
    }

    /**
     * 组织机构类型ID
     * @return java.lang.String
     **/
    public String getOrganTypeId() {
        return organTypeId;
    }

    /**
     * 组织机构类型ID
     * @param organTypeId 组织机构类型ID
     **/
    public void setOrganTypeId(String organTypeId) {
        this.organTypeId = organTypeId == null ? null : organTypeId.trim();
    }

    /**
     * 组织机构编码值:唯一,与组织机构表关联
     * @return java.lang.String
     **/
    public String getOrganTypeValue() {
        return organTypeValue;
    }

    /**
     * 组织机构编码值:唯一,与组织机构表关联
     * @param organTypeValue 组织机构编码值
     **/
    public void setOrganTypeValue(String organTypeValue) {
        this.organTypeValue = organTypeValue == null ? null : organTypeValue.trim();
    }

    /**
     * 组织机构类型名称
     * @return java.lang.String
     **/
    public String getOrganTypeName() {
        return organTypeName;
    }

    /**
     * 组织机构类型名称
     * @param organTypeName 组织机构类型名称
     **/
    public void setOrganTypeName(String organTypeName) {
        this.organTypeName = organTypeName == null ? null : organTypeName.trim();
    }

    /**
     * 组织机构类型备注
     * @return java.lang.String
     **/
    public String getOrganTypeDesc() {
        return organTypeDesc;
    }

    /**
     * 组织机构类型备注
     * @param organTypeDesc 组织机构类型备注
     **/
    public void setOrganTypeDesc(String organTypeDesc) {
        this.organTypeDesc = organTypeDesc == null ? null : organTypeDesc.trim();
    }

    /**
     * 排序号
     * @return java.lang.Integer
     **/
    public Integer getOrganTypeOrderNum() {
        return organTypeOrderNum;
    }

    /**
     * 排序号
     * @param organTypeOrderNum 排序号
     **/
    public void setOrganTypeOrderNum(Integer organTypeOrderNum) {
        this.organTypeOrderNum = organTypeOrderNum;
    }

    /**
     * 状态:启用,禁用
     * @return java.lang.String
     **/
    public String getOrganTypeStatus() {
        return organTypeStatus;
    }

    /**
     * 状态:启用,禁用
     * @param organTypeStatus 状态
     **/
    public void setOrganTypeStatus(String organTypeStatus) {
        this.organTypeStatus = organTypeStatus == null ? null : organTypeStatus.trim();
    }

    /**
     * 创建时间
     * @return java.util.Date
     **/
    public Date getOrganTypeCreateTime() {
        return organTypeCreateTime;
    }

    /**
     * 创建时间
     * @param organTypeCreateTime 创建时间
     **/
    public void setOrganTypeCreateTime(Date organTypeCreateTime) {
        this.organTypeCreateTime = organTypeCreateTime;
    }
}