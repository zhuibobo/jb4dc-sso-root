package com.jb4dc.system.setting.service.impl;

import com.jb4dc.base.dbaccess.exenum.MenuTypeEnum;
import com.jb4dc.base.dbaccess.exenum.TrueFalseEnum;
import com.jb4dc.base.service.po.MenuPO;
import com.jb4dc.base.service.provide.IFrameMenuProvide;
import com.jb4dc.core.base.session.JB4DCSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/9
 * To change this template use File | Settings | File Templates.
 */
public class FrameMenuImpl implements IFrameMenuProvide {
    @Override
    public List<MenuPO> getMyFrameMenu(JB4DCSession jb4DCSession) {
        //return null;
        List<MenuPO> result=new ArrayList<>();
        String rootMenuId="0";
        MenuPO rootMenu=getMenu("-1",rootMenuId,"Root","Root","Root", MenuTypeEnum.Root.getDisplayName(),"","","");

        MenuPO Dictionary=getMenu(rootMenu.getMenuId(),"Dictionary",
                "数据字典","数据字典","数据字典",
                "","","/HTML/Dictionary/DictionaryManager.html","");

        MenuPO OperationLog=getMenu(rootMenu.getMenuId(),"OperationLog",
                "操作日志","操作日志","操作日志",
                "","","/HTML/OperationLog/OperationLogList.html","");

        MenuPO ParasSetting=getMenu(rootMenu.getMenuId(),"ParasSetting",
                "参数设置","参数设置","参数设置",
                "","","/HTML/ParasSetting/ParasSettingList.html","");

        result.add(rootMenu);
        result.add(Dictionary);
        result.add(OperationLog);
        result.add(ParasSetting);
        return result;
    }

    public MenuPO getMenu(String parentId, String id, String name, String text, String value, String type, String leftUrl, String rightUrl, String iconClassName){
        MenuPO menuEntity=new MenuPO();
        menuEntity.setMenuId(id);
        menuEntity.setMenuName(name);
        menuEntity.setMenuText(text);
        menuEntity.setMenuValue(value);
        menuEntity.setMenuType(type);
        menuEntity.setMenuIsExpand(TrueFalseEnum.False.getDisplayName());
        menuEntity.setMenuIsSystem(TrueFalseEnum.True.getDisplayName());
        menuEntity.setMenuLeftUrl(leftUrl);
        menuEntity.setMenuRightUrl(rightUrl);
        menuEntity.setMenuParentId(parentId);
        menuEntity.setMenuClassName(iconClassName);
        return menuEntity;
    }
}
