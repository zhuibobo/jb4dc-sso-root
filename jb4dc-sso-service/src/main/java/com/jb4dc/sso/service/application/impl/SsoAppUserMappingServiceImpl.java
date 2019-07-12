package com.jb4dc.sso.service.application.impl;

import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dao.application.SsoAppUserMappingMapper;
import com.jb4dc.sso.dbentities.SsoAppUserMappingEntity;
import com.jb4dc.sso.service.application.ISsoAppUserMappingService;

public class SsoAppUserMappingServiceImpl extends BaseServiceImpl<SsoAppUserMappingEntity> implements ISsoAppUserMappingService
{
    SsoAppUserMappingMapper ssoAppUserMappingMapper;

    public SsoAppUserMappingServiceImpl(SsoAppUserMappingMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        ssoAppUserMappingMapper=_defaultBaseMapper;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DSession, String id, SsoAppUserMappingEntity record) throws JBuild4DCGenerallyException {
        return super.save(jb4DSession,id, record, new IAddBefore<SsoAppUserMappingEntity>() {
            @Override
            public SsoAppUserMappingEntity run(JB4DCSession jb4DSession,SsoAppUserMappingEntity sourceEntity) throws JBuild4DCGenerallyException {
                //设置排序,以及其他参数--nextOrderNum()
                return sourceEntity;
            }
        });
    }
}
