package com.jb4dc.sso.service.provide;

import com.jb4dc.base.service.po.MenuPO;
import com.jb4dc.base.service.provide.IFrameMenuProvide;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dbentities.menu.MenuEntity;
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
public class FrameMenuProvide implements IFrameMenuProvide {

    IMenuService menuService;

    public FrameMenuProvide(IMenuService _menuService){
        this.menuService=_menuService;
    }

    @Override
    public List<MenuPO> getMyFrameMenu(JB4DCSession jb4DCSession) {
        List all =  menuService.getALL(jb4DCSession);
        return all;
    }
}
