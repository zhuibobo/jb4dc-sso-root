package com.jb4dc.sso.dbentities.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import com.jb4dc.base.service.po.MenuPO;
import com.jb4dc.base.service.po.SsoAppPO;

import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_SSO_APP
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class SsoAppEntity extends SsoAppPO {

    public SsoAppEntity(String appId, String appCode, String appName, String appPublicKey, String appPrivateKey, String appDomain, String appIndexUrl, String appIntegratedType, String appMainImageId, String appType, String appMainId, String appCategory, String appDesc, Integer appOrderNum, Date appCreateTime, String appStatus, String appCreatorId, String appOrganId,String contextPath) {
        super(appId, appCode, appName, appPublicKey, appPrivateKey, appDomain, appIndexUrl, appIntegratedType, appMainImageId, appType, appMainId, appCategory, appDesc, appOrderNum, appCreateTime, appStatus, appCreatorId, appOrganId,contextPath);
    }

    public SsoAppEntity() {
    }
}