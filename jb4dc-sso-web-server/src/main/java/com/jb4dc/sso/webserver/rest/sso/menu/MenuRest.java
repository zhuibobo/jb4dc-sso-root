package com.jb4dc.sso.webserver.rest.sso.menu;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.menu.MenuEntity;
import com.jb4dc.sso.service.menu.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/22
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "/Rest/SSO/Mu/Menu")
public class MenuRest {

    @Autowired
    IMenuService menuService;

    @RequestMapping(value = "/GetMenusBySystemId", method = RequestMethod.POST)
    public JBuild4DCResponseVo getMenusBySystemId(String systemId){

        List<MenuEntity> menuEntities=menuService.getMenusBySystemId(JB4DCSessionUtility.getSession(),systemId);
        return JBuild4DCResponseVo.getDataSuccess(menuEntities);
    }

    @RequestMapping(value = "/GetMyAuthMenusBySystemId", method = RequestMethod.POST)
    public JBuild4DCResponseVo getMyAuthMenusBySystemId(String systemId){
        JB4DCSession jb4DCSession=JB4DCSessionUtility.getSession();
        List<MenuEntity> menuEntities=menuService.getMyAuthMenusBySystemId(JB4DCSessionUtility.getSession(),systemId);
        return JBuild4DCResponseVo.getDataSuccess(menuEntities);
    }
}
