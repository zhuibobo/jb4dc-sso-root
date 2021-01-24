package com.jb4dc.sso.service.menu.impl;

import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dao.menu.MenuInnObjMapper;
import com.jb4dc.sso.dbentities.menu.MenuInnObjEntity;
import com.jb4dc.sso.service.menu.IMenuInnObjService;
import org.springframework.stereotype.Service;

@Service
public class MenuInnObjServiceImpl extends BaseServiceImpl<MenuInnObjEntity> implements IMenuInnObjService
{
    MenuInnObjMapper menuInnObjMapper;
    public MenuInnObjServiceImpl(MenuInnObjMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        menuInnObjMapper=_defaultBaseMapper;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DCSession, String id, MenuInnObjEntity record) throws JBuild4DCGenerallyException {
        return super.save(jb4DCSession,id, record, new IAddBefore<MenuInnObjEntity>() {
            @Override
            public MenuInnObjEntity run(JB4DCSession jb4DCSession,MenuInnObjEntity sourceEntity) throws JBuild4DCGenerallyException {
                //设置排序,以及其他参数--nextOrderNum()
                return sourceEntity;
            }
        });
    }
}
