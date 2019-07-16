package com.jb4dc.sso.service.application.impl;

import com.jb4dc.base.dbaccess.exenum.EnableTypeEnum;
import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dao.application.SsoAppInterfaceMapper;
import com.jb4dc.sso.dbentities.application.SsoAppInterfaceEntity;
import com.jb4dc.sso.service.application.ISsoAppInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SsoAppInterfaceServiceImpl extends BaseServiceImpl<SsoAppInterfaceEntity> implements ISsoAppInterfaceService
{
    SsoAppInterfaceMapper ssoAppInterfaceMapper;

    @Autowired
    public SsoAppInterfaceServiceImpl(SsoAppInterfaceMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        ssoAppInterfaceMapper=_defaultBaseMapper;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DSession, String id, SsoAppInterfaceEntity record) throws JBuild4DCGenerallyException {
        return super.save(jb4DSession,id, record, new IAddBefore<SsoAppInterfaceEntity>() {
            @Override
            public SsoAppInterfaceEntity run(JB4DCSession jb4DSession,SsoAppInterfaceEntity sourceEntity) throws JBuild4DCGenerallyException {

                //设置排序,以及其他参数--nextOrderNum()
                sourceEntity.setInterfaceOrderNum(ssoAppInterfaceMapper.nextOrderNum());
                sourceEntity.setInterfaceCreateTime(new Date());
                sourceEntity.setInterfaceStatus(EnableTypeEnum.enable.getDisplayName());
                sourceEntity.setInterfaceCreatorId(jb4DSession.getUserId());
                sourceEntity.setInterfaceOrganId(jb4DSession.getOrganId());
                return sourceEntity;
            }
        });
    }

    @Override
    public List<SsoAppInterfaceEntity> getAppInterfaces(JB4DCSession jb4DSession, String appId) {
        return ssoAppInterfaceMapper.selectAppInterfaces(appId);
    }
}