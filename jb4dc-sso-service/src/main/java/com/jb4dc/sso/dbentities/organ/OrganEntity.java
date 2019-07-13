package com.jb4dc.sso.dbentities.organ;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_ORGAN
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class OrganEntity {
    //ORGAN_ID:主键:UUID
    @DBKeyField
    private String organId;

    //ORGAN_NAME:组织机构名称
    private String organName;

    //ORGAN_SHORT_NAME:组织机构简称
    private String organShortName;

    //ORGAN_NO:组织机构编号
    private String organNo;

    //ORGAN_CODE:组织机构代码
    private String organCode;

    //ORGAN_CREATE_TIME:创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date organCreateTime;

    //ORGAN_PHONE:联系电话
    private String organPhone;

    //ORGAN_POST:邮政编码
    private String organPost;

    //ORGAN_TYPE_VALUE:组织机构类型:关联TSSO_ORGAN_TYPE表
    private String organTypeValue;

    //ORGAN_ADDRESS:组织机构地址
    private String organAddress;

    //ORGAN_CONTACTS:联系人
    private String organContacts;

    //ORGAN_CONTACTS_MOBILE:联系人电话
    private String organContactsMobile;

    //ORGAN_WEB_SITE:站点地址
    private String organWebSite;

    //ORGAN_FAX:传真号码
    private String organFax;

    //ORGAN_CHILD_COUNT:子节点数量
    private Integer organChildCount;

    //ORGAN_IS_VIRTUAL:是否虚拟
    private String organIsVirtual;

    //ORGAN_ORDER_NUM:排序号
    private Integer organOrderNum;

    //ORGAN_PARENT_ID:父节点ID
    private String organParentId;

    //ORGAN_PARENT_ID_LIST:父节点列表
    private String organParentIdList;

    //ORGAN_STATUS:状态:启用,禁用
    private String organStatus;

    //ORGAN_CREATOR_ORG_ID:创建组织者ID
    private String organCreatorOrgId;

    //ORGAN_MAIN_IMAGE_ID:主题Logo:关联到TFS_FILE_INFO表的FILE_ID
    private String organMainImageId;

    //ORGAN_DESC:组织机构备注
    private String organDesc;

    /**
     * 构造函数
     * @param organId 主键
     * @param organName 组织机构名称
     * @param organShortName 组织机构简称
     * @param organNo 组织机构编号
     * @param organCode 组织机构代码
     * @param organCreateTime 创建时间
     * @param organPhone 联系电话
     * @param organPost 邮政编码
     * @param organTypeValue 组织机构类型
     * @param organAddress 组织机构地址
     * @param organContacts 联系人
     * @param organContactsMobile 联系人电话
     * @param organWebSite 站点地址
     * @param organFax 传真号码
     * @param organChildCount 子节点数量
     * @param organIsVirtual 是否虚拟
     * @param organOrderNum 排序号
     * @param organParentId 父节点ID
     * @param organParentIdList 父节点列表
     * @param organStatus 状态
     * @param organCreatorOrgId 创建组织者ID
     * @param organMainImageId 主题Logo
     * @param organDesc 组织机构备注
     **/
    public OrganEntity(String organId, String organName, String organShortName, String organNo, String organCode, Date organCreateTime, String organPhone, String organPost, String organTypeValue, String organAddress, String organContacts, String organContactsMobile, String organWebSite, String organFax, Integer organChildCount, String organIsVirtual, Integer organOrderNum, String organParentId, String organParentIdList, String organStatus, String organCreatorOrgId, String organMainImageId, String organDesc) {
        this.organId = organId;
        this.organName = organName;
        this.organShortName = organShortName;
        this.organNo = organNo;
        this.organCode = organCode;
        this.organCreateTime = organCreateTime;
        this.organPhone = organPhone;
        this.organPost = organPost;
        this.organTypeValue = organTypeValue;
        this.organAddress = organAddress;
        this.organContacts = organContacts;
        this.organContactsMobile = organContactsMobile;
        this.organWebSite = organWebSite;
        this.organFax = organFax;
        this.organChildCount = organChildCount;
        this.organIsVirtual = organIsVirtual;
        this.organOrderNum = organOrderNum;
        this.organParentId = organParentId;
        this.organParentIdList = organParentIdList;
        this.organStatus = organStatus;
        this.organCreatorOrgId = organCreatorOrgId;
        this.organMainImageId = organMainImageId;
        this.organDesc = organDesc;
    }

    public OrganEntity() {
        super();
    }

    /**
     * 主键:UUID
     * @return java.lang.String
     **/
    public String getOrganId() {
        return organId;
    }

    /**
     * 主键:UUID
     * @param organId 主键
     **/
    public void setOrganId(String organId) {
        this.organId = organId == null ? null : organId.trim();
    }

    /**
     * 组织机构名称
     * @return java.lang.String
     **/
    public String getOrganName() {
        return organName;
    }

    /**
     * 组织机构名称
     * @param organName 组织机构名称
     **/
    public void setOrganName(String organName) {
        this.organName = organName == null ? null : organName.trim();
    }

    /**
     * 组织机构简称
     * @return java.lang.String
     **/
    public String getOrganShortName() {
        return organShortName;
    }

    /**
     * 组织机构简称
     * @param organShortName 组织机构简称
     **/
    public void setOrganShortName(String organShortName) {
        this.organShortName = organShortName == null ? null : organShortName.trim();
    }

    /**
     * 组织机构编号
     * @return java.lang.String
     **/
    public String getOrganNo() {
        return organNo;
    }

    /**
     * 组织机构编号
     * @param organNo 组织机构编号
     **/
    public void setOrganNo(String organNo) {
        this.organNo = organNo == null ? null : organNo.trim();
    }

    /**
     * 组织机构代码
     * @return java.lang.String
     **/
    public String getOrganCode() {
        return organCode;
    }

    /**
     * 组织机构代码
     * @param organCode 组织机构代码
     **/
    public void setOrganCode(String organCode) {
        this.organCode = organCode == null ? null : organCode.trim();
    }

    /**
     * 创建时间
     * @return java.util.Date
     **/
    public Date getOrganCreateTime() {
        return organCreateTime;
    }

    /**
     * 创建时间
     * @param organCreateTime 创建时间
     **/
    public void setOrganCreateTime(Date organCreateTime) {
        this.organCreateTime = organCreateTime;
    }

    /**
     * 联系电话
     * @return java.lang.String
     **/
    public String getOrganPhone() {
        return organPhone;
    }

    /**
     * 联系电话
     * @param organPhone 联系电话
     **/
    public void setOrganPhone(String organPhone) {
        this.organPhone = organPhone == null ? null : organPhone.trim();
    }

    /**
     * 邮政编码
     * @return java.lang.String
     **/
    public String getOrganPost() {
        return organPost;
    }

    /**
     * 邮政编码
     * @param organPost 邮政编码
     **/
    public void setOrganPost(String organPost) {
        this.organPost = organPost == null ? null : organPost.trim();
    }

    /**
     * 组织机构类型:关联TSSO_ORGAN_TYPE表
     * @return java.lang.String
     **/
    public String getOrganTypeValue() {
        return organTypeValue;
    }

    /**
     * 组织机构类型:关联TSSO_ORGAN_TYPE表
     * @param organTypeValue 组织机构类型
     **/
    public void setOrganTypeValue(String organTypeValue) {
        this.organTypeValue = organTypeValue == null ? null : organTypeValue.trim();
    }

    /**
     * 组织机构地址
     * @return java.lang.String
     **/
    public String getOrganAddress() {
        return organAddress;
    }

    /**
     * 组织机构地址
     * @param organAddress 组织机构地址
     **/
    public void setOrganAddress(String organAddress) {
        this.organAddress = organAddress == null ? null : organAddress.trim();
    }

    /**
     * 联系人
     * @return java.lang.String
     **/
    public String getOrganContacts() {
        return organContacts;
    }

    /**
     * 联系人
     * @param organContacts 联系人
     **/
    public void setOrganContacts(String organContacts) {
        this.organContacts = organContacts == null ? null : organContacts.trim();
    }

    /**
     * 联系人电话
     * @return java.lang.String
     **/
    public String getOrganContactsMobile() {
        return organContactsMobile;
    }

    /**
     * 联系人电话
     * @param organContactsMobile 联系人电话
     **/
    public void setOrganContactsMobile(String organContactsMobile) {
        this.organContactsMobile = organContactsMobile == null ? null : organContactsMobile.trim();
    }

    /**
     * 站点地址
     * @return java.lang.String
     **/
    public String getOrganWebSite() {
        return organWebSite;
    }

    /**
     * 站点地址
     * @param organWebSite 站点地址
     **/
    public void setOrganWebSite(String organWebSite) {
        this.organWebSite = organWebSite == null ? null : organWebSite.trim();
    }

    /**
     * 传真号码
     * @return java.lang.String
     **/
    public String getOrganFax() {
        return organFax;
    }

    /**
     * 传真号码
     * @param organFax 传真号码
     **/
    public void setOrganFax(String organFax) {
        this.organFax = organFax == null ? null : organFax.trim();
    }

    /**
     * 子节点数量
     * @return java.lang.Integer
     **/
    public Integer getOrganChildCount() {
        return organChildCount;
    }

    /**
     * 子节点数量
     * @param organChildCount 子节点数量
     **/
    public void setOrganChildCount(Integer organChildCount) {
        this.organChildCount = organChildCount;
    }

    /**
     * 是否虚拟
     * @return java.lang.String
     **/
    public String getOrganIsVirtual() {
        return organIsVirtual;
    }

    /**
     * 是否虚拟
     * @param organIsVirtual 是否虚拟
     **/
    public void setOrganIsVirtual(String organIsVirtual) {
        this.organIsVirtual = organIsVirtual == null ? null : organIsVirtual.trim();
    }

    /**
     * 排序号
     * @return java.lang.Integer
     **/
    public Integer getOrganOrderNum() {
        return organOrderNum;
    }

    /**
     * 排序号
     * @param organOrderNum 排序号
     **/
    public void setOrganOrderNum(Integer organOrderNum) {
        this.organOrderNum = organOrderNum;
    }

    /**
     * 父节点ID
     * @return java.lang.String
     **/
    public String getOrganParentId() {
        return organParentId;
    }

    /**
     * 父节点ID
     * @param organParentId 父节点ID
     **/
    public void setOrganParentId(String organParentId) {
        this.organParentId = organParentId == null ? null : organParentId.trim();
    }

    /**
     * 父节点列表
     * @return java.lang.String
     **/
    public String getOrganParentIdList() {
        return organParentIdList;
    }

    /**
     * 父节点列表
     * @param organParentIdList 父节点列表
     **/
    public void setOrganParentIdList(String organParentIdList) {
        this.organParentIdList = organParentIdList == null ? null : organParentIdList.trim();
    }

    /**
     * 状态:启用,禁用
     * @return java.lang.String
     **/
    public String getOrganStatus() {
        return organStatus;
    }

    /**
     * 状态:启用,禁用
     * @param organStatus 状态
     **/
    public void setOrganStatus(String organStatus) {
        this.organStatus = organStatus == null ? null : organStatus.trim();
    }

    /**
     * 创建组织者ID
     * @return java.lang.String
     **/
    public String getOrganCreatorOrgId() {
        return organCreatorOrgId;
    }

    /**
     * 创建组织者ID
     * @param organCreatorOrgId 创建组织者ID
     **/
    public void setOrganCreatorOrgId(String organCreatorOrgId) {
        this.organCreatorOrgId = organCreatorOrgId == null ? null : organCreatorOrgId.trim();
    }

    /**
     * 主题Logo:关联到TFS_FILE_INFO表的FILE_ID
     * @return java.lang.String
     **/
    public String getOrganMainImageId() {
        return organMainImageId;
    }

    /**
     * 主题Logo:关联到TFS_FILE_INFO表的FILE_ID
     * @param organMainImageId 主题Logo
     **/
    public void setOrganMainImageId(String organMainImageId) {
        this.organMainImageId = organMainImageId == null ? null : organMainImageId.trim();
    }

    /**
     * 组织机构备注
     * @return java.lang.String
     **/
    public String getOrganDesc() {
        return organDesc;
    }

    /**
     * 组织机构备注
     * @param organDesc 组织机构备注
     **/
    public void setOrganDesc(String organDesc) {
        this.organDesc = organDesc == null ? null : organDesc.trim();
    }
}