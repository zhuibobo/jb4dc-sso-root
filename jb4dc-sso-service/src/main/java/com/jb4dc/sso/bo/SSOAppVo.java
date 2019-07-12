package com.jb4dc.sso.bo;


import com.jb4dc.sso.dbentities.SsoAppEntity;
import com.jb4dc.sso.dbentities.SsoAppInterfaceEntity;

import java.util.List;

public class SSOAppVo {

    private SsoAppEntity ssoAppEntity;
    private List<SsoAppInterfaceEntity> ssoAppInterfaceEntityList;

    private List<SsoAppEntity> subSystemSsoAppEntityList;

    public SsoAppEntity getSsoAppEntity() {
        return ssoAppEntity;
    }

    public void setSsoAppEntity(SsoAppEntity ssoAppEntity) {
        this.ssoAppEntity = ssoAppEntity;
    }

    public List<SsoAppInterfaceEntity> getSsoAppInterfaceEntityList() {
        return ssoAppInterfaceEntityList;
    }

    public void setSsoAppInterfaceEntityList(List<SsoAppInterfaceEntity> ssoAppInterfaceEntityList) {
        this.ssoAppInterfaceEntityList = ssoAppInterfaceEntityList;
    }

    public List<SsoAppEntity> getSubSystemSsoAppEntityList() {
        return subSystemSsoAppEntityList;
    }

    public void setSubSystemSsoAppEntityList(List<SsoAppEntity> subSystemSsoAppEntityList) {
        this.subSystemSsoAppEntityList = subSystemSsoAppEntityList;
    }
}
