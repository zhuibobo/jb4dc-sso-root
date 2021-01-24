package com.jb4dc.sso.dbentities.menu;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :tsso_menu_inn_obj
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class MenuInnObjEntity {
    //MIO_ID:
    @DBKeyField
    private String mioId;

    //MIO_MENU_ID:所属菜单ID
    private String mioMenuId;

    //MIO_AUTH_TYPE:扩展权限标记类型:绑定显示,绑定动作
    private String mioAuthType;

    //MIO_CATEGORY:扩展标记分类:权限标记,一般标记
    private String mioCategory;

    //MIO_NAME:扩展标记名称
    private String mioName;

    //MIO_VALUE:扩展标记值
    private String mioValue;

    //MIO_ACTION:绑定动作
    private String mioAction;

    //MIO_ORDER_NUM:排序号
    private Integer mioOrderNum;

    //MIO_PARENT_ID:父节点ID
    private String mioParentId;

    //MIO_PARENT_ID_LIST:父节点ID列表
    private String mioParentIdList;

    //MIO_CREATE_TIME:创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date mioCreateTime;

    //MIO_DESCRIPTION:备注
    private String mioDescription;

    /**
     * 构造函数
     * @param mioId
     * @param mioMenuId 所属菜单ID
     * @param mioAuthType 扩展权限标记类型
     * @param mioCategory 扩展标记分类
     * @param mioName 扩展标记名称
     * @param mioValue 扩展标记值
     * @param mioAction 绑定动作
     * @param mioOrderNum 排序号
     * @param mioParentId 父节点ID
     * @param mioParentIdList 父节点ID列表
     * @param mioCreateTime 创建时间
     * @param mioDescription 备注
     **/
    public MenuInnObjEntity(String mioId, String mioMenuId, String mioAuthType, String mioCategory, String mioName, String mioValue, String mioAction, Integer mioOrderNum, String mioParentId, String mioParentIdList, Date mioCreateTime, String mioDescription) {
        this.mioId = mioId;
        this.mioMenuId = mioMenuId;
        this.mioAuthType = mioAuthType;
        this.mioCategory = mioCategory;
        this.mioName = mioName;
        this.mioValue = mioValue;
        this.mioAction = mioAction;
        this.mioOrderNum = mioOrderNum;
        this.mioParentId = mioParentId;
        this.mioParentIdList = mioParentIdList;
        this.mioCreateTime = mioCreateTime;
        this.mioDescription = mioDescription;
    }

    public MenuInnObjEntity() {
        super();
    }

    /**
     *
     * @return java.lang.String
     **/
    public String getMioId() {
        return mioId;
    }

    /**
     *
     * @param mioId
     **/
    public void setMioId(String mioId) {
        this.mioId = mioId == null ? null : mioId.trim();
    }

    /**
     * 所属菜单ID
     * @return java.lang.String
     **/
    public String getMioMenuId() {
        return mioMenuId;
    }

    /**
     * 所属菜单ID
     * @param mioMenuId 所属菜单ID
     **/
    public void setMioMenuId(String mioMenuId) {
        this.mioMenuId = mioMenuId == null ? null : mioMenuId.trim();
    }

    /**
     * 扩展权限标记类型:绑定显示,绑定动作
     * @return java.lang.String
     **/
    public String getMioAuthType() {
        return mioAuthType;
    }

    /**
     * 扩展权限标记类型:绑定显示,绑定动作
     * @param mioAuthType 扩展权限标记类型
     **/
    public void setMioAuthType(String mioAuthType) {
        this.mioAuthType = mioAuthType == null ? null : mioAuthType.trim();
    }

    /**
     * 扩展标记分类:权限标记,一般标记
     * @return java.lang.String
     **/
    public String getMioCategory() {
        return mioCategory;
    }

    /**
     * 扩展标记分类:权限标记,一般标记
     * @param mioCategory 扩展标记分类
     **/
    public void setMioCategory(String mioCategory) {
        this.mioCategory = mioCategory == null ? null : mioCategory.trim();
    }

    /**
     * 扩展标记名称
     * @return java.lang.String
     **/
    public String getMioName() {
        return mioName;
    }

    /**
     * 扩展标记名称
     * @param mioName 扩展标记名称
     **/
    public void setMioName(String mioName) {
        this.mioName = mioName == null ? null : mioName.trim();
    }

    /**
     * 扩展标记值
     * @return java.lang.String
     **/
    public String getMioValue() {
        return mioValue;
    }

    /**
     * 扩展标记值
     * @param mioValue 扩展标记值
     **/
    public void setMioValue(String mioValue) {
        this.mioValue = mioValue == null ? null : mioValue.trim();
    }

    /**
     * 绑定动作
     * @return java.lang.String
     **/
    public String getMioAction() {
        return mioAction;
    }

    /**
     * 绑定动作
     * @param mioAction 绑定动作
     **/
    public void setMioAction(String mioAction) {
        this.mioAction = mioAction == null ? null : mioAction.trim();
    }

    /**
     * 排序号
     * @return java.lang.Integer
     **/
    public Integer getMioOrderNum() {
        return mioOrderNum;
    }

    /**
     * 排序号
     * @param mioOrderNum 排序号
     **/
    public void setMioOrderNum(Integer mioOrderNum) {
        this.mioOrderNum = mioOrderNum;
    }

    /**
     * 父节点ID
     * @return java.lang.String
     **/
    public String getMioParentId() {
        return mioParentId;
    }

    /**
     * 父节点ID
     * @param mioParentId 父节点ID
     **/
    public void setMioParentId(String mioParentId) {
        this.mioParentId = mioParentId == null ? null : mioParentId.trim();
    }

    /**
     * 父节点ID列表
     * @return java.lang.String
     **/
    public String getMioParentIdList() {
        return mioParentIdList;
    }

    /**
     * 父节点ID列表
     * @param mioParentIdList 父节点ID列表
     **/
    public void setMioParentIdList(String mioParentIdList) {
        this.mioParentIdList = mioParentIdList == null ? null : mioParentIdList.trim();
    }

    /**
     * 创建时间
     * @return java.util.Date
     **/
    public Date getMioCreateTime() {
        return mioCreateTime;
    }

    /**
     * 创建时间
     * @param mioCreateTime 创建时间
     **/
    public void setMioCreateTime(Date mioCreateTime) {
        this.mioCreateTime = mioCreateTime;
    }

    /**
     * 备注
     * @return java.lang.String
     **/
    public String getMioDescription() {
        return mioDescription;
    }

    /**
     * 备注
     * @param mioDescription 备注
     **/
    public void setMioDescription(String mioDescription) {
        this.mioDescription = mioDescription == null ? null : mioDescription.trim();
    }
}