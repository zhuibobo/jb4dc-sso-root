package com.jb4dc.sso.dbentities.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_USER
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class UserEntity {
    //USER_ID:主键:UUID
    @DBKeyField
    private String userId;

    //USER_NAME:用户名
    private String userName;

    //USER_ACCOUNT:账号
    private String userAccount;

    //USER_PASSWORD:密码
    private String userPassword;

    //USER_EMAIL:邮件地址
    private String userEmail;

    //USER_PHONE_NUMBER:手机号码
    private String userPhoneNumber;

    //USER_HEAD_ID:头像ID:关联到TFS_FILE_INFO表的FILE_ID
    private String userHeadId;

    //USER_ORGAN_ID:所属组织ID
    private String userOrganId;

    //USER_DESC:用户备注
    private String userDesc;

    //USER_CREATE_TIME:创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date userCreateTime;

    //USER_CREATOR_ID:创建用户ID
    private String userCreatorId;

    //USER_STATUS:状态:启用,禁用
    private String userStatus;

    //USER_ORDER_NUM:排序号
    private Integer userOrderNum;

    //USER_TYPE:用户类型:平台管理员,组织机构管理员,一般用户
    private String userType;

    /**
     * 构造函数
     * @param userId 主键
     * @param userName 用户名
     * @param userAccount 账号
     * @param userPassword 密码
     * @param userEmail 邮件地址
     * @param userPhoneNumber 手机号码
     * @param userHeadId 头像ID
     * @param userOrganId 所属组织ID
     * @param userDesc 用户备注
     * @param userCreateTime 创建时间
     * @param userCreatorId 创建用户ID
     * @param userStatus 状态
     * @param userOrderNum 排序号
     * @param userType 用户类型
     **/
    public UserEntity(String userId, String userName, String userAccount, String userPassword, String userEmail, String userPhoneNumber, String userHeadId, String userOrganId, String userDesc, Date userCreateTime, String userCreatorId, String userStatus, Integer userOrderNum, String userType) {
        this.userId = userId;
        this.userName = userName;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
        this.userHeadId = userHeadId;
        this.userOrganId = userOrganId;
        this.userDesc = userDesc;
        this.userCreateTime = userCreateTime;
        this.userCreatorId = userCreatorId;
        this.userStatus = userStatus;
        this.userOrderNum = userOrderNum;
        this.userType = userType;
    }

    public UserEntity() {
        super();
    }

    /**
     * 主键:UUID
     * @return java.lang.String
     **/
    public String getUserId() {
        return userId;
    }

    /**
     * 主键:UUID
     * @param userId 主键
     **/
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 用户名
     * @return java.lang.String
     **/
    public String getUserName() {
        return userName;
    }

    /**
     * 用户名
     * @param userName 用户名
     **/
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 账号
     * @return java.lang.String
     **/
    public String getUserAccount() {
        return userAccount;
    }

    /**
     * 账号
     * @param userAccount 账号
     **/
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount == null ? null : userAccount.trim();
    }

    /**
     * 密码
     * @return java.lang.String
     **/
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * 密码
     * @param userPassword 密码
     **/
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    /**
     * 邮件地址
     * @return java.lang.String
     **/
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * 邮件地址
     * @param userEmail 邮件地址
     **/
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail == null ? null : userEmail.trim();
    }

    /**
     * 手机号码
     * @return java.lang.String
     **/
    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    /**
     * 手机号码
     * @param userPhoneNumber 手机号码
     **/
    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber == null ? null : userPhoneNumber.trim();
    }

    /**
     * 头像ID:关联到TFS_FILE_INFO表的FILE_ID
     * @return java.lang.String
     **/
    public String getUserHeadId() {
        return userHeadId;
    }

    /**
     * 头像ID:关联到TFS_FILE_INFO表的FILE_ID
     * @param userHeadId 头像ID
     **/
    public void setUserHeadId(String userHeadId) {
        this.userHeadId = userHeadId == null ? null : userHeadId.trim();
    }

    /**
     * 所属组织ID
     * @return java.lang.String
     **/
    public String getUserOrganId() {
        return userOrganId;
    }

    /**
     * 所属组织ID
     * @param userOrganId 所属组织ID
     **/
    public void setUserOrganId(String userOrganId) {
        this.userOrganId = userOrganId == null ? null : userOrganId.trim();
    }

    /**
     * 用户备注
     * @return java.lang.String
     **/
    public String getUserDesc() {
        return userDesc;
    }

    /**
     * 用户备注
     * @param userDesc 用户备注
     **/
    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc == null ? null : userDesc.trim();
    }

    /**
     * 创建时间
     * @return java.util.Date
     **/
    public Date getUserCreateTime() {
        return userCreateTime;
    }

    /**
     * 创建时间
     * @param userCreateTime 创建时间
     **/
    public void setUserCreateTime(Date userCreateTime) {
        this.userCreateTime = userCreateTime;
    }

    /**
     * 创建用户ID
     * @return java.lang.String
     **/
    public String getUserCreatorId() {
        return userCreatorId;
    }

    /**
     * 创建用户ID
     * @param userCreatorId 创建用户ID
     **/
    public void setUserCreatorId(String userCreatorId) {
        this.userCreatorId = userCreatorId == null ? null : userCreatorId.trim();
    }

    /**
     * 状态:启用,禁用
     * @return java.lang.String
     **/
    public String getUserStatus() {
        return userStatus;
    }

    /**
     * 状态:启用,禁用
     * @param userStatus 状态
     **/
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus == null ? null : userStatus.trim();
    }

    /**
     * 排序号
     * @return java.lang.Integer
     **/
    public Integer getUserOrderNum() {
        return userOrderNum;
    }

    /**
     * 排序号
     * @param userOrderNum 排序号
     **/
    public void setUserOrderNum(Integer userOrderNum) {
        this.userOrderNum = userOrderNum;
    }

    /**
     * 用户类型:平台管理员,组织机构管理员,一般用户
     * @return java.lang.String
     **/
    public String getUserType() {
        return userType;
    }

    /**
     * 用户类型:平台管理员,组织机构管理员,一般用户
     * @param userType 用户类型
     **/
    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }
}