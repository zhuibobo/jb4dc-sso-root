package com.jb4dc.sso.webserver.rest.sso.menu;

import com.github.pagehelper.PageInfo;
import com.jb4dc.base.service.IBaseService;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.service.search.GeneralSearchUtility;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.feb.dist.webserver.rest.base.GeneralRest;
import com.jb4dc.sso.dbentities.menu.MenuEntity;
import com.jb4dc.sso.dbentities.menu.MenuInnObjEntity;
import com.jb4dc.sso.service.menu.IMenuInnObjService;
import com.jb4dc.sso.service.menu.IMenuService;
import com.jb4dc.sso.webserver.remote.DBLinkRuntimeRemote;
import com.jb4dc.sso.webserver.remote.ModuleRuntimeRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/22
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "/Rest/SSO/Mu/MenuInnObj")
public class MenuInnObjRest extends GeneralRest<MenuInnObjEntity> {

    @Autowired
    IMenuInnObjService menuInnObjService;

    @Override
    public String getModuleName() {
        return "单点登录系统-菜单内对象";
    }

    @Override
    protected IBaseService<MenuInnObjEntity> getBaseService() {
        return menuInnObjService;
    }
}
