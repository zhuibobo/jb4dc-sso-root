package com.jb4dc.sso.dbentities.department;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_DEPARTMENT
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class DepartmentEntity {
    //DEPT_ID:主键:UUID
    @DBKeyField
    private String deptId;

    //DEPT_NAME:部门名称
    private String deptName;

    //DEPT_SHORT_NAME:部门简称
    private String deptShortName;

    //DEPT_NO:部门编号
    private String deptNo;

    //DEPT_PER_CHARGE:部门负责人
    private String deptPerCharge;

    //DEPT_PER_CHARGE_PHONE:部门负责人电话
    private String deptPerChargePhone;

    //DEPT_IS_VIRTUAL:是否虚拟
    private String deptIsVirtual;

    //DEPT_CHILD_COUNT:子节点数量
    private Integer deptChildCount;

    //DEPT_CREATE_TIME:创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deptCreateTime;

    //DEPT_CREATOR_ID:创建用户ID
    private String deptCreatorId;

    //DEPT_ORDER_NUM:排序号
    private Integer deptOrderNum;

    //DEPT_IS_ROOT:是否根节点:根节点在组织创建时自动创建
    private String deptIsRoot;

    //DEPT_PARENT_ID:父节点ID:根节点的ParentId为0
    private String deptParentId;

    //DEPT_PARENT_ID_LIST:父节点列表
    private String deptParentIdList;

    //DEPT_STATUS:状态:启用,禁用
    private String deptStatus;

    //DEPT_ORGAN_ID:所属组织ID
    private String deptOrganId;

    //DEPT_DESC:部门备注
    private String deptDesc;

    /**
     * 构造函数
     * @param deptId 主键
     * @param deptName 部门名称
     * @param deptShortName 部门简称
     * @param deptNo 部门编号
     * @param deptPerCharge 部门负责人
     * @param deptPerChargePhone 部门负责人电话
     * @param deptIsVirtual 是否虚拟
     * @param deptChildCount 子节点数量
     * @param deptCreateTime 创建时间
     * @param deptCreatorId 创建用户ID
     * @param deptOrderNum 排序号
     * @param deptIsRoot 是否根节点
     * @param deptParentId 父节点ID
     * @param deptParentIdList 父节点列表
     * @param deptStatus 状态
     * @param deptOrganId 所属组织ID
     * @param deptDesc 部门备注
     **/
    public DepartmentEntity(String deptId, String deptName, String deptShortName, String deptNo, String deptPerCharge, String deptPerChargePhone, String deptIsVirtual, Integer deptChildCount, Date deptCreateTime, String deptCreatorId, Integer deptOrderNum, String deptIsRoot, String deptParentId, String deptParentIdList, String deptStatus, String deptOrganId, String deptDesc) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.deptShortName = deptShortName;
        this.deptNo = deptNo;
        this.deptPerCharge = deptPerCharge;
        this.deptPerChargePhone = deptPerChargePhone;
        this.deptIsVirtual = deptIsVirtual;
        this.deptChildCount = deptChildCount;
        this.deptCreateTime = deptCreateTime;
        this.deptCreatorId = deptCreatorId;
        this.deptOrderNum = deptOrderNum;
        this.deptIsRoot = deptIsRoot;
        this.deptParentId = deptParentId;
        this.deptParentIdList = deptParentIdList;
        this.deptStatus = deptStatus;
        this.deptOrganId = deptOrganId;
        this.deptDesc = deptDesc;
    }

    public DepartmentEntity() {
        super();
    }

    /**
     * 主键:UUID
     * @return java.lang.String
     **/
    public String getDeptId() {
        return deptId;
    }

    /**
     * 主键:UUID
     * @param deptId 主键
     **/
    public void setDeptId(String deptId) {
        this.deptId = deptId == null ? null : deptId.trim();
    }

    /**
     * 部门名称
     * @return java.lang.String
     **/
    public String getDeptName() {
        return deptName;
    }

    /**
     * 部门名称
     * @param deptName 部门名称
     **/
    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    /**
     * 部门简称
     * @return java.lang.String
     **/
    public String getDeptShortName() {
        return deptShortName;
    }

    /**
     * 部门简称
     * @param deptShortName 部门简称
     **/
    public void setDeptShortName(String deptShortName) {
        this.deptShortName = deptShortName == null ? null : deptShortName.trim();
    }

    /**
     * 部门编号
     * @return java.lang.String
     **/
    public String getDeptNo() {
        return deptNo;
    }

    /**
     * 部门编号
     * @param deptNo 部门编号
     **/
    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo == null ? null : deptNo.trim();
    }

    /**
     * 部门负责人
     * @return java.lang.String
     **/
    public String getDeptPerCharge() {
        return deptPerCharge;
    }

    /**
     * 部门负责人
     * @param deptPerCharge 部门负责人
     **/
    public void setDeptPerCharge(String deptPerCharge) {
        this.deptPerCharge = deptPerCharge == null ? null : deptPerCharge.trim();
    }

    /**
     * 部门负责人电话
     * @return java.lang.String
     **/
    public String getDeptPerChargePhone() {
        return deptPerChargePhone;
    }

    /**
     * 部门负责人电话
     * @param deptPerChargePhone 部门负责人电话
     **/
    public void setDeptPerChargePhone(String deptPerChargePhone) {
        this.deptPerChargePhone = deptPerChargePhone == null ? null : deptPerChargePhone.trim();
    }

    /**
     * 是否虚拟
     * @return java.lang.String
     **/
    public String getDeptIsVirtual() {
        return deptIsVirtual;
    }

    /**
     * 是否虚拟
     * @param deptIsVirtual 是否虚拟
     **/
    public void setDeptIsVirtual(String deptIsVirtual) {
        this.deptIsVirtual = deptIsVirtual == null ? null : deptIsVirtual.trim();
    }

    /**
     * 子节点数量
     * @return java.lang.Integer
     **/
    public Integer getDeptChildCount() {
        return deptChildCount;
    }

    /**
     * 子节点数量
     * @param deptChildCount 子节点数量
     **/
    public void setDeptChildCount(Integer deptChildCount) {
        this.deptChildCount = deptChildCount;
    }

    /**
     * 创建时间
     * @return java.util.Date
     **/
    public Date getDeptCreateTime() {
        return deptCreateTime;
    }

    /**
     * 创建时间
     * @param deptCreateTime 创建时间
     **/
    public void setDeptCreateTime(Date deptCreateTime) {
        this.deptCreateTime = deptCreateTime;
    }

    /**
     * 创建用户ID
     * @return java.lang.String
     **/
    public String getDeptCreatorId() {
        return deptCreatorId;
    }

    /**
     * 创建用户ID
     * @param deptCreatorId 创建用户ID
     **/
    public void setDeptCreatorId(String deptCreatorId) {
        this.deptCreatorId = deptCreatorId == null ? null : deptCreatorId.trim();
    }

    /**
     * 排序号
     * @return java.lang.Integer
     **/
    public Integer getDeptOrderNum() {
        return deptOrderNum;
    }

    /**
     * 排序号
     * @param deptOrderNum 排序号
     **/
    public void setDeptOrderNum(Integer deptOrderNum) {
        this.deptOrderNum = deptOrderNum;
    }

    /**
     * 是否根节点:根节点在组织创建时自动创建
     * @return java.lang.String
     **/
    public String getDeptIsRoot() {
        return deptIsRoot;
    }

    /**
     * 是否根节点:根节点在组织创建时自动创建
     * @param deptIsRoot 是否根节点
     **/
    public void setDeptIsRoot(String deptIsRoot) {
        this.deptIsRoot = deptIsRoot == null ? null : deptIsRoot.trim();
    }

    /**
     * 父节点ID:根节点的ParentId为0
     * @return java.lang.String
     **/
    public String getDeptParentId() {
        return deptParentId;
    }

    /**
     * 父节点ID:根节点的ParentId为0
     * @param deptParentId 父节点ID
     **/
    public void setDeptParentId(String deptParentId) {
        this.deptParentId = deptParentId == null ? null : deptParentId.trim();
    }

    /**
     * 父节点列表
     * @return java.lang.String
     **/
    public String getDeptParentIdList() {
        return deptParentIdList;
    }

    /**
     * 父节点列表
     * @param deptParentIdList 父节点列表
     **/
    public void setDeptParentIdList(String deptParentIdList) {
        this.deptParentIdList = deptParentIdList == null ? null : deptParentIdList.trim();
    }

    /**
     * 状态:启用,禁用
     * @return java.lang.String
     **/
    public String getDeptStatus() {
        return deptStatus;
    }

    /**
     * 状态:启用,禁用
     * @param deptStatus 状态
     **/
    public void setDeptStatus(String deptStatus) {
        this.deptStatus = deptStatus == null ? null : deptStatus.trim();
    }

    /**
     * 所属组织ID
     * @return java.lang.String
     **/
    public String getDeptOrganId() {
        return deptOrganId;
    }

    /**
     * 所属组织ID
     * @param deptOrganId 所属组织ID
     **/
    public void setDeptOrganId(String deptOrganId) {
        this.deptOrganId = deptOrganId == null ? null : deptOrganId.trim();
    }

    /**
     * 部门备注
     * @return java.lang.String
     **/
    public String getDeptDesc() {
        return deptDesc;
    }

    /**
     * 部门备注
     * @param deptDesc 部门备注
     **/
    public void setDeptDesc(String deptDesc) {
        this.deptDesc = deptDesc == null ? null : deptDesc.trim();
    }
}