package com.jb4dc.sso.service.role;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dbentities.RoleGroupEntity;

import java.util.List;

public interface IRoleGroupService extends IBaseService<RoleGroupEntity> {
    void initSystemData(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException;

    List<RoleGroupEntity> getALLOrderByAsc(JB4DCSession session);
}
