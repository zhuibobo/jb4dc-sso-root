package com.jb4dc.sso.dbentities.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_SSO_APP_INTERFACE
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class SsoAppInterfaceEntity {
    //INTERFACE_ID:主键:UUID
    @DBKeyField
    private String interfaceId;

    //INTERFACE_BELONG_APP_ID:所属系统ID
    private String interfaceBelongAppId;

    //INTERFACE_CODE:接口类型:默认定义和自定义:eg[LOGIN]
    private String interfaceCode;

    //INTERFACE_NAME:接口名称
    private String interfaceName;

    //INTERFACE_URL:接口地址
    private String interfaceUrl;

    //INTERFACE_PARAS:参数
    private String interfaceParas;

    //INTERFACE_FORMAT:格式化方法
    private String interfaceFormat;

    //INTERFACE_DESC:备注
    private String interfaceDesc;

    //INTERFACE_ORDER_NUM:排序号
    private Integer interfaceOrderNum;

    //INTERFACE_CREATE_TIME:创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date interfaceCreateTime;

    //INTERFACE_STATUS:状态
    private String interfaceStatus;

    //INTERFACE_CREATOR_ID:创建者的ID
    private String interfaceCreatorId;

    //INTERFACE_ORGAN_ID:创建组织ID
    private String interfaceOrganId;

    /**
     * 构造函数
     * @param interfaceId 主键
     * @param interfaceBelongAppId 所属系统ID
     * @param interfaceCode 接口类型
     * @param interfaceName 接口名称
     * @param interfaceUrl 接口地址
     * @param interfaceParas 参数
     * @param interfaceFormat 格式化方法
     * @param interfaceDesc 备注
     * @param interfaceOrderNum 排序号
     * @param interfaceCreateTime 创建时间
     * @param interfaceStatus 状态
     * @param interfaceCreatorId 创建者的ID
     * @param interfaceOrganId 创建组织ID
     **/
    public SsoAppInterfaceEntity(String interfaceId, String interfaceBelongAppId, String interfaceCode, String interfaceName, String interfaceUrl, String interfaceParas, String interfaceFormat, String interfaceDesc, Integer interfaceOrderNum, Date interfaceCreateTime, String interfaceStatus, String interfaceCreatorId, String interfaceOrganId) {
        this.interfaceId = interfaceId;
        this.interfaceBelongAppId = interfaceBelongAppId;
        this.interfaceCode = interfaceCode;
        this.interfaceName = interfaceName;
        this.interfaceUrl = interfaceUrl;
        this.interfaceParas = interfaceParas;
        this.interfaceFormat = interfaceFormat;
        this.interfaceDesc = interfaceDesc;
        this.interfaceOrderNum = interfaceOrderNum;
        this.interfaceCreateTime = interfaceCreateTime;
        this.interfaceStatus = interfaceStatus;
        this.interfaceCreatorId = interfaceCreatorId;
        this.interfaceOrganId = interfaceOrganId;
    }

    public SsoAppInterfaceEntity() {
        super();
    }

    /**
     * 主键:UUID
     * @return java.lang.String
     **/
    public String getInterfaceId() {
        return interfaceId;
    }

    /**
     * 主键:UUID
     * @param interfaceId 主键
     **/
    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId == null ? null : interfaceId.trim();
    }

    /**
     * 所属系统ID
     * @return java.lang.String
     **/
    public String getInterfaceBelongAppId() {
        return interfaceBelongAppId;
    }

    /**
     * 所属系统ID
     * @param interfaceBelongAppId 所属系统ID
     **/
    public void setInterfaceBelongAppId(String interfaceBelongAppId) {
        this.interfaceBelongAppId = interfaceBelongAppId == null ? null : interfaceBelongAppId.trim();
    }

    /**
     * 接口类型:默认定义和自定义:eg[LOGIN]
     * @return java.lang.String
     **/
    public String getInterfaceCode() {
        return interfaceCode;
    }

    /**
     * 接口类型:默认定义和自定义:eg[LOGIN]
     * @param interfaceCode 接口类型
     **/
    public void setInterfaceCode(String interfaceCode) {
        this.interfaceCode = interfaceCode == null ? null : interfaceCode.trim();
    }

    /**
     * 接口名称
     * @return java.lang.String
     **/
    public String getInterfaceName() {
        return interfaceName;
    }

    /**
     * 接口名称
     * @param interfaceName 接口名称
     **/
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName == null ? null : interfaceName.trim();
    }

    /**
     * 接口地址
     * @return java.lang.String
     **/
    public String getInterfaceUrl() {
        return interfaceUrl;
    }

    /**
     * 接口地址
     * @param interfaceUrl 接口地址
     **/
    public void setInterfaceUrl(String interfaceUrl) {
        this.interfaceUrl = interfaceUrl == null ? null : interfaceUrl.trim();
    }

    /**
     * 参数
     * @return java.lang.String
     **/
    public String getInterfaceParas() {
        return interfaceParas;
    }

    /**
     * 参数
     * @param interfaceParas 参数
     **/
    public void setInterfaceParas(String interfaceParas) {
        this.interfaceParas = interfaceParas == null ? null : interfaceParas.trim();
    }

    /**
     * 格式化方法
     * @return java.lang.String
     **/
    public String getInterfaceFormat() {
        return interfaceFormat;
    }

    /**
     * 格式化方法
     * @param interfaceFormat 格式化方法
     **/
    public void setInterfaceFormat(String interfaceFormat) {
        this.interfaceFormat = interfaceFormat == null ? null : interfaceFormat.trim();
    }

    /**
     * 备注
     * @return java.lang.String
     **/
    public String getInterfaceDesc() {
        return interfaceDesc;
    }

    /**
     * 备注
     * @param interfaceDesc 备注
     **/
    public void setInterfaceDesc(String interfaceDesc) {
        this.interfaceDesc = interfaceDesc == null ? null : interfaceDesc.trim();
    }

    /**
     * 排序号
     * @return java.lang.Integer
     **/
    public Integer getInterfaceOrderNum() {
        return interfaceOrderNum;
    }

    /**
     * 排序号
     * @param interfaceOrderNum 排序号
     **/
    public void setInterfaceOrderNum(Integer interfaceOrderNum) {
        this.interfaceOrderNum = interfaceOrderNum;
    }

    /**
     * 创建时间
     * @return java.util.Date
     **/
    public Date getInterfaceCreateTime() {
        return interfaceCreateTime;
    }

    /**
     * 创建时间
     * @param interfaceCreateTime 创建时间
     **/
    public void setInterfaceCreateTime(Date interfaceCreateTime) {
        this.interfaceCreateTime = interfaceCreateTime;
    }

    /**
     * 状态
     * @return java.lang.String
     **/
    public String getInterfaceStatus() {
        return interfaceStatus;
    }

    /**
     * 状态
     * @param interfaceStatus 状态
     **/
    public void setInterfaceStatus(String interfaceStatus) {
        this.interfaceStatus = interfaceStatus == null ? null : interfaceStatus.trim();
    }

    /**
     * 创建者的ID
     * @return java.lang.String
     **/
    public String getInterfaceCreatorId() {
        return interfaceCreatorId;
    }

    /**
     * 创建者的ID
     * @param interfaceCreatorId 创建者的ID
     **/
    public void setInterfaceCreatorId(String interfaceCreatorId) {
        this.interfaceCreatorId = interfaceCreatorId == null ? null : interfaceCreatorId.trim();
    }

    /**
     * 创建组织ID
     * @return java.lang.String
     **/
    public String getInterfaceOrganId() {
        return interfaceOrganId;
    }

    /**
     * 创建组织ID
     * @param interfaceOrganId 创建组织ID
     **/
    public void setInterfaceOrganId(String interfaceOrganId) {
        this.interfaceOrganId = interfaceOrganId == null ? null : interfaceOrganId.trim();
    }
}