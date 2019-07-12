package com.jb4dc.sso.service.authority.impl;

import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dao.authority.AuthorityMapper;
import com.jb4dc.sso.dbentities.authority.AuthorityEntity;
import com.jb4dc.sso.service.authority.IAuthorityService;

public class AuthorityServiceImpl extends BaseServiceImpl<AuthorityEntity> implements IAuthorityService
{
    AuthorityMapper authorityMapper;
    public AuthorityServiceImpl(AuthorityMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        authorityMapper=_defaultBaseMapper;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DSession, String id, AuthorityEntity record) throws JBuild4DCGenerallyException {
        return super.save(jb4DSession,id, record, new IAddBefore<AuthorityEntity>() {
            @Override
            public AuthorityEntity run(JB4DCSession jb4DSession,AuthorityEntity sourceEntity) throws JBuild4DCGenerallyException {
                //设置排序,以及其他参数--nextOrderNum()
                return sourceEntity;
            }
        });
    }
}