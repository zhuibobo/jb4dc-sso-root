package com.jb4dc.sso.webserver.rest.sso.menu;

import com.github.pagehelper.PageInfo;
import com.jb4dc.base.service.IBaseService;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.service.search.GeneralSearchUtility;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.feb.dist.webserver.rest.base.GeneralRest;
import com.jb4dc.sso.dbentities.menu.MenuEntity;
import com.jb4dc.sso.dbentities.role.RoleEntity;
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
@RequestMapping(value = "/Rest/SSO/Mu/Menu")
public class MenuRest extends GeneralRest<MenuEntity> {

    @Autowired
    protected IMenuService menuService;

    @Autowired
    DBLinkRuntimeRemote dbLinkRuntimeRemote;

    @Autowired
    ModuleRuntimeRemote moduleRuntimeRemote;

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

    @Override
    public String getModuleName() {
        return "单点登录系统-菜单";
    }

    @Override
    protected IBaseService<MenuEntity> getBaseService() {
        return menuService;
    }

    @RequestMapping(value = "/GetFullDBLink", method = RequestMethod.POST)
    public JBuild4DCResponseVo getFullDBLink(){
        return dbLinkRuntimeRemote.getFullDBLink();
    }

    @RequestMapping(value = "/GetMouldTreeData", method = RequestMethod.POST)
    public JBuild4DCResponseVo getMouldTreeData(String dbLinkId){
        return moduleRuntimeRemote.getTreeData(dbLinkId);
    }

    @RequestMapping(value = "/GetSelectModuleObjectListByModuleId", method = RequestMethod.POST)
    public JBuild4DCResponseVo<List<Map<String,Object>>> getSelectModuleObjectListByModuleId(String searchCondition) throws IOException, ParseException {
        Map<String, Object> searchMap = GeneralSearchUtility.deserializationToMap(searchCondition);
        String selectModuleObjectType=searchMap.get("selectModuleObjectType").toString();
        String selectModuleId=searchMap.get("selectModuleId").toString();
        JBuild4DCResponseVo<List<Map<String,Object>>> responseVo=moduleRuntimeRemote.getModuleItems(selectModuleId,selectModuleObjectType);
        PageInfo<Map<String,Object>> proPageInfo=new PageInfo<>();
        proPageInfo.setList(responseVo.getData());
        proPageInfo.setPageNum(1);
        proPageInfo.setPageSize(1000);
        proPageInfo.setTotal(1000);
        return JBuild4DCResponseVo.getDataSuccess(proPageInfo);
        //return moduleRuntimeRemote.getTreeData(dbLinkId);
    }
}
