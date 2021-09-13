package com.jb4dc.sso.provide;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.service.po.MenuPO;
import com.jb4dc.base.service.po.SsoAppPO;
import com.jb4dc.base.service.provide.IFramePageProvide;
import com.jb4dc.base.ymls.JBuild4DCYaml;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.service.application.ISsoAppService;
import com.jb4dc.sso.service.menu.IMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/13
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FrameMenuProvide implements IFramePageProvide {

    IMenuService menuService;

    ISsoAppService ssoAppService;

    JBuild4DCYaml jBuild4DCYaml;

    public FrameMenuProvide(IMenuService _menuService,ISsoAppService _ssoAppService,JBuild4DCYaml _jBuild4DCYaml){
        this.menuService=_menuService;
        this.ssoAppService=_ssoAppService;
        this.jBuild4DCYaml=_jBuild4DCYaml;
    }

    @Override
    public List<MenuPO> getMyFrameMenu(JB4DCSession jb4DCSession) {
        List all =  menuService.getMyFrameMenu(jb4DCSession,"SSOMainApp",jb4DCSession.getUserId());
        if(jb4DCSession.isFullAuthority()){
            all=menuService.getMenusBySystemId(jb4DCSession,"SSOMainApp");
        }
        return all;
    }

    @Override
    public List<SsoAppPO> getMyFrameAuthorityApp(String userId) throws JBuild4DCGenerallyException {
        JB4DCSession jb4DCSession= JB4DCSessionUtility.getSession();
        List myAuthAppList =  ssoAppService.getHasAuthorityAppSSO(jb4DCSession,userId);
        return myAuthAppList;
    }

    @Override
    public String getMyFrameLogoutUrl(String userId) throws JBuild4DCGenerallyException {
        return jBuild4DCYaml.getSsoLogoutUrl();
    }
}
