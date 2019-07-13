package com.jb4dc.sso.service.menu;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dbentities.menu.MenuEntity;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/13
 * To change this template use File | Settings | File Templates.
 */
public interface IMenuService extends IBaseService<MenuEntity> {
    void initSystemData(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException;
}