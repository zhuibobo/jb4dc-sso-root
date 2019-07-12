package com.jb4dc.sso.bo;


import com.jb4dc.sso.dbentities.DepartmentEntity;
import com.jb4dc.sso.dbentities.DepartmentUserEntity;
import com.jb4dc.sso.dbentities.OrganEntity;
import com.jb4dc.sso.dbentities.UserEntity;

public class DepartmentUserVo {
    private DepartmentUserEntity departmentUserEntity;
    private UserEntity userEntity;
    private OrganEntity organEntity;
    private DepartmentEntity departmentEntity;

    public DepartmentUserEntity getDepartmentUserEntity() {
        return departmentUserEntity;
    }

    public void setDepartmentUserEntity(DepartmentUserEntity departmentUserEntity) {
        this.departmentUserEntity = departmentUserEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public OrganEntity getOrganEntity() {
        return organEntity;
    }

    public void setOrganEntity(OrganEntity organEntity) {
        this.organEntity = organEntity;
    }

    public DepartmentEntity getDepartmentEntity() {
        return departmentEntity;
    }

    public void setDepartmentEntity(DepartmentEntity departmentEntity) {
        this.departmentEntity = departmentEntity;
    }
}
