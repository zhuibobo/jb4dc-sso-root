package com.jb4dc.sso.service.application.impl;

import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.bo.SSOAppVo;
import com.jb4dc.sso.dao.application.SsoAppMapper;
import com.jb4dc.sso.dbentities.application.SsoAppEntity;
import com.jb4dc.sso.dbentities.application.SsoAppInterfaceEntity;
import com.jb4dc.sso.service.application.ISsoAppInterfaceService;
import com.jb4dc.sso.service.application.ISsoAppService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class SsoAppServiceImpl extends BaseServiceImpl<SsoAppEntity> implements ISsoAppService
{
    SsoAppMapper ssoAppMapper;

    @Autowired
    ISsoAppInterfaceService ssoAppInterfaceService;

    public SsoAppServiceImpl(SsoAppMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        ssoAppMapper=_defaultBaseMapper;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DSession, String id, SsoAppEntity record) throws JBuild4DCGenerallyException {
        return super.save(jb4DSession,id, record, new IAddBefore<SsoAppEntity>() {
            @Override
            public SsoAppEntity run(JB4DCSession jb4DSession,SsoAppEntity sourceEntity) throws JBuild4DCGenerallyException {
                sourceEntity.setAppOrderNum(ssoAppMapper.nextOrderNum());
                sourceEntity.setAppCreateTime(new Date());
                sourceEntity.setAppCreatorId(jb4DSession.getUserId());
                sourceEntity.setAppOrganId(jb4DSession.getOrganId());
                return sourceEntity;
            }
        });
    }

    @Override
    public void saveIntegratedMainApp(JB4DCSession jb4DSession, SSOAppVo entity) throws JBuild4DCGenerallyException {
        entity.getSsoAppEntity().setAppIntegratedType("开发集成");
        entity.getSsoAppEntity().setAppMainId("");
        entity.getSsoAppEntity().setAppType("主系统");
        this.saveSimple(jb4DSession,entity.getSsoAppEntity().getAppId(),entity.getSsoAppEntity());
        if(entity.getSsoAppInterfaceEntityList()!=null&&entity.getSsoAppInterfaceEntityList().size()>0){
            for (SsoAppInterfaceEntity ssoAppInterfaceEntity : entity.getSsoAppInterfaceEntityList()) {
                ssoAppInterfaceService.saveSimple(jb4DSession,ssoAppInterfaceEntity.getInterfaceId(),ssoAppInterfaceEntity);
            }
        }
    }

    @Override
    public SSOAppVo getAppVo(JB4DCSession jb4DSession, String appId) {
        SSOAppVo ssoAppVo=new SSOAppVo();
        SsoAppEntity ssoAppEntity=ssoAppMapper.selectByPrimaryKey(appId);
        List<SsoAppInterfaceEntity> ssoAppInterfaceEntityList= ssoAppInterfaceService.getAppInterfaces(jb4DSession,appId);
        ssoAppVo.setSsoAppEntity(ssoAppEntity);
        ssoAppVo.setSsoAppInterfaceEntityList(ssoAppInterfaceEntityList);
        return ssoAppVo;
    }

    @Override
    public void saveIntegratedSubApp(JB4DCSession jb4DSession, SSOAppVo entity) throws JBuild4DCGenerallyException {
        if(entity.getSsoAppEntity().getAppMainId()==null||entity.getSsoAppEntity().getAppMainId().equals("")){
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"所属主系统ID不能为空!");
        }
        entity.getSsoAppEntity().setAppIntegratedType("开发集成");
        entity.getSsoAppEntity().setAppType("子系统");
        this.saveSimple(jb4DSession,entity.getSsoAppEntity().getAppId(),entity.getSsoAppEntity());
        if(entity.getSsoAppInterfaceEntityList()!=null&&entity.getSsoAppInterfaceEntityList().size()>0){
            for (SsoAppInterfaceEntity ssoAppInterfaceEntity : entity.getSsoAppInterfaceEntityList()) {
                ssoAppInterfaceService.saveSimple(jb4DSession,ssoAppInterfaceEntity.getInterfaceId(),ssoAppInterfaceEntity);
            }
        }
    }

    @Override
    public List<SsoAppEntity> getALLSubApp(JB4DCSession session, String appId) {
        return ssoAppMapper.selectALLSubApp(appId);
    }

    @Override
    public List<SsoAppEntity> getALLMainApp(JB4DCSession session) {
        return ssoAppMapper.selectAllMainApp();
    }
}
