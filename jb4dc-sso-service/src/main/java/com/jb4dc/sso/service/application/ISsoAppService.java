package com.jb4dc.sso.service.application;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.po.SSOAppPO;
import com.jb4dc.sso.dbentities.application.SsoAppEntity;

import java.util.List;

public interface ISsoAppService extends IBaseService<SsoAppEntity> {
    void saveIntegratedMainApp(JB4DCSession jb4DSession, SSOAppPO entity) throws JBuild4DCGenerallyException;

    SSOAppPO getAppVo(JB4DCSession jb4DSession, String appId);

    void saveIntegratedSubApp(JB4DCSession jb4DSession, SSOAppPO entity) throws JBuild4DCGenerallyException;

    List<SsoAppEntity> getALLSubApp(JB4DCSession session, String appId);

    List<SsoAppEntity> getALLMainApp(JB4DCSession session);

    void initSystemData(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException;
}
