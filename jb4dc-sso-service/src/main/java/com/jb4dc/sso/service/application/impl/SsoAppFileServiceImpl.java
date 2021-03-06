package com.jb4dc.sso.service.application.impl;

import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dao.application.SsoAppFileMapper;
import com.jb4dc.sso.dbentities.application.SsoAppFileEntity;
import com.jb4dc.sso.service.application.ISsoAppFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SsoAppFileServiceImpl extends BaseServiceImpl<SsoAppFileEntity> implements ISsoAppFileService
{
    SsoAppFileMapper ssoAppFileMapper;

    @Autowired
    public SsoAppFileServiceImpl(SsoAppFileMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        ssoAppFileMapper=_defaultBaseMapper;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DSession, String id, SsoAppFileEntity record) throws JBuild4DCGenerallyException {
        return super.save(jb4DSession,id, record, new IAddBefore<SsoAppFileEntity>() {
            @Override
            public SsoAppFileEntity run(JB4DCSession jb4DSession,SsoAppFileEntity sourceEntity) throws JBuild4DCGenerallyException {
                //设置排序,以及其他参数--nextOrderNum()
                return sourceEntity;
            }
        });
    }
}