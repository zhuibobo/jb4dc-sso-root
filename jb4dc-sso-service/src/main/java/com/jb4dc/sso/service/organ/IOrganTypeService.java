package com.jb4dc.sso.service.organ;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dbentities.organ.OrganTypeEntity;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2018/7/27
 * To change this template use File | Settings | File Templates.
 */
public interface IOrganTypeService extends IBaseService<OrganTypeEntity> {
    void createDefaultOrganType(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException;
}