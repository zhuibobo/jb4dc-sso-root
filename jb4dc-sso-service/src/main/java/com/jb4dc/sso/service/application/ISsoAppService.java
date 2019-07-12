package com.jb4dc.sso.service.application;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.bo.SSOAppVo;
import com.jb4dc.sso.dbentities.SsoAppEntity;

import java.util.List;

public interface ISsoAppService extends IBaseService<SsoAppEntity> {
    void saveIntegratedMainApp(JB4DCSession jb4DSession, SSOAppVo entity) throws JBuild4DCGenerallyException;

    SSOAppVo getAppVo(JB4DCSession jb4DSession, String appId);

    void saveIntegratedSubApp(JB4DCSession jb4DSession, SSOAppVo entity) throws JBuild4DCGenerallyException;

    List<SsoAppEntity> getALLSubApp(JB4DCSession session, String appId);

    List<SsoAppEntity> getALLMainApp(JB4DCSession session);
}
