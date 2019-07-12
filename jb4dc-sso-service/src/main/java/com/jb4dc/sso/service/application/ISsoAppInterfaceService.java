package com.jb4dc.sso.service.application;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dbentities.application.SsoAppInterfaceEntity;

import java.util.List;

public interface ISsoAppInterfaceService extends IBaseService<SsoAppInterfaceEntity> {
    List<SsoAppInterfaceEntity> getAppInterfaces(JB4DCSession jb4DSession, String appId);
}
