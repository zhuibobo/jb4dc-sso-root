package com.jb4dc.sso.service.application.impl;

import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.base.service.po.SsoAppPO;
import com.jb4dc.base.service.spring.SpringContextHolder;
import com.jb4dc.base.ymls.JBuild4DCYaml;
import com.jb4dc.core.base.encryption.nsymmetric.RSAUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.po.SsoAppComplexPO;
import com.jb4dc.sso.dao.application.SsoAppMapper;
import com.jb4dc.sso.dbentities.application.SsoAppEntity;
import com.jb4dc.sso.dbentities.application.SsoAppInterfaceEntity;
import com.jb4dc.sso.service.application.ISsoAppInterfaceService;
import com.jb4dc.sso.service.application.ISsoAppService;
import com.jb4dc.sso.service.authority.IAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.Date;
import java.util.List;

@Service
public class SsoAppServiceImpl extends BaseServiceImpl<SsoAppEntity> implements ISsoAppService
{
    SsoAppMapper ssoAppMapper;

    @Autowired
    ISsoAppInterfaceService ssoAppInterfaceService;

    @Autowired
    IAuthorityService authorityService;

    @Autowired
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
    public void saveIntegratedMainApp(JB4DCSession jb4DSession, SsoAppComplexPO entity) throws JBuild4DCGenerallyException {
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
    public SsoAppComplexPO getAppVo(JB4DCSession jb4DSession, String appId) {
        SsoAppComplexPO ssoAppVo=new SsoAppComplexPO();
        SsoAppEntity ssoAppEntity=ssoAppMapper.selectByPrimaryKey(appId);
        List<SsoAppInterfaceEntity> ssoAppInterfaceEntityList= ssoAppInterfaceService.getAppInterfaces(jb4DSession,appId);
        ssoAppVo.setSsoAppEntity(ssoAppEntity);
        ssoAppVo.setSsoAppInterfaceEntityList(ssoAppInterfaceEntityList);
        return ssoAppVo;
    }

    @Override
    public void saveIntegratedSubApp(JB4DCSession jb4DSession, SsoAppComplexPO entity) throws JBuild4DCGenerallyException {
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

    @Override
    public void initSystemData(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException {
        try {
            //单点登录与统一用户管理系统
            SsoAppComplexPO ssoAppBO = innerNewMainApp(jb4DSession, "SSOMainApp", "单点登录与统一用户管理系统","JB4DCGateWay","/Gateway/HTML/Frame/FrameV2/FrameView.html","SSOSystem");

            //代码生产器
            //SsoAppComplexPO codeGenerateAppBO = innerNewMainApp(jb4DSession, "CodeGenerateAppBO", "代码生成器","http://58.51.184.121:9102/CodeGen","/HTML/Login.html","CodeGen");

            //应用构建系统
            SsoAppComplexPO builderAppBO = innerNewMainApp(jb4DSession, "BuilderMainApp", "应用构建系统","JB4DCGateWay","/Gateway/HTML/Frame/FrameV2/FrameView.html","JB4DCBuilder");

            //开发样例系统
            //SSOAppPO devMockAppBO = innerNewMainApp(jb4DSession, "DevMockApp", "开发样例系统","http://127.0.0.1:9102/DevMock","/HTML/FrameV1/FrameView.html");

            //人力资源系统
            //SSOAppPO hrAppBO = innerNewMainApp(jb4DSession, "HrSystem", "人力资源系统","http://127.0.0.1:9102/HrSystem","/HTML/FrameV1/FrameView.html");

            //服务运维系统
            SsoAppComplexPO qcAppBO = innerNewMainApp(jb4DSession, "QCSystem", "服务运维系统","JB4DCGateWay","/Gateway/HTML/Frame/FrameV2/FrameView.html","QCSystem");

            //网格化系统
            //SsoAppComplexPO GridAppBO = innerNewMainApp(jb4DSession, "GridSystem", "网格化社会管理系统","http://58.51.184.121:9106/GridSystem","/HTML/FrameV2/FrameView.html","GridSystem");

        } catch (Exception e) {
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,e.getMessage());
        }
    }

    @Override
    public List getHasAuthorityAppSSO(JB4DCSession jb4DSession,String userId) throws JBuild4DCGenerallyException{
        try {
            List<SsoAppEntity> ssoAppEntityList;
            if(authorityService.userIsFullAuth(jb4DSession,userId)){
                ssoAppEntityList = ssoAppMapper.selectAllASC();
            }
            else {
                ssoAppEntityList = ssoAppMapper.selectHasAuthorityAppSSO(userId);
            }
            if(SpringContextHolder.getActiveProfile().equals("dev")||SpringContextHolder.getActiveProfile().equals("devjar")){
                for (SsoAppEntity ssoAppEntity : ssoAppEntityList) {
                    ssoAppEntity.setAppDomain(ssoAppEntity.getAppDomain().replaceAll("58.51.184.121","127.0.0.1"));
                }
            }
            return ssoAppEntityList;
        }
        catch (Exception e) {
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,e.getMessage());
        }
    }

    private SsoAppComplexPO innerNewMainApp(JB4DCSession jb4DSession, String appId, String appName, String domain, String indexUrl,String contextPath) throws Exception {
        KeyPair keyPair= RSAUtility.getKeyPair();
        String publicKey= RSAUtility.getPublicKeyBase64(keyPair);
        String privateKey=RSAUtility.getPrivateKeyBase64(keyPair);
        this.deleteByKeyNotValidate(jb4DSession, appId, JBuild4DCYaml.getWarningOperationCode());
        SsoAppEntity ssoMainApp = new SsoAppEntity();
        ssoMainApp.setAppId(appId);
        ssoMainApp.setAppCode(appId);
        ssoMainApp.setAppName(appName);
        ssoMainApp.setAppPublicKey(publicKey);
        ssoMainApp.setAppPrivateKey(privateKey);
        ssoMainApp.setAppDomain(domain);
        ssoMainApp.setAppIndexUrl(indexUrl);
        ssoMainApp.setAppIntegratedType("开发集成");
        ssoMainApp.setAppMainImageId("");
        ssoMainApp.setAppType("主系统");
        ssoMainApp.setAppMainId("");
        ssoMainApp.setAppCategory("web");
        ssoMainApp.setAppDesc("");
        ssoMainApp.setAppStatus("启用");
        ssoMainApp.setAppContextPath(contextPath);
        SsoAppComplexPO ssoAppBO = new SsoAppComplexPO();
        ssoAppBO.setSsoAppEntity(ssoMainApp);
        this.saveIntegratedMainApp(jb4DSession, ssoAppBO);
        return ssoAppBO;
    }
}
