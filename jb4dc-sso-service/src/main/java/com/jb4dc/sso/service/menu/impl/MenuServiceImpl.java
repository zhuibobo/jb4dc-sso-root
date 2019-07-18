package com.jb4dc.sso.service.menu.impl;

import com.jb4dc.base.dbaccess.exenum.MenuTypeEnum;
import com.jb4dc.base.dbaccess.exenum.TrueFalseEnum;
import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.sso.dao.menu.MenuMapper;
import com.jb4dc.sso.dbentities.menu.MenuEntity;
import com.jb4dc.sso.service.menu.IMenuService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/13
 * To change this template use File | Settings | File Templates.
 */

@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuEntity> implements IMenuService {

    MenuMapper menuMapper;

    @Autowired
    public MenuServiceImpl(MenuMapper _defaultBaseMapper) {
        super(_defaultBaseMapper);
        menuMapper = _defaultBaseMapper;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DSession, String id, MenuEntity entity) throws JBuild4DCGenerallyException {
        return super.save(jb4DSession, id, entity, new IAddBefore<MenuEntity>() {
            @Override
            public MenuEntity run(JB4DCSession jb4DSession, MenuEntity sourceEntity) throws JBuild4DCGenerallyException {
                sourceEntity.setMenuOrganId(jb4DSession.getOrganId());
                sourceEntity.setMenuOrganName(jb4DSession.getOrganName());
                sourceEntity.setMenuCreatorId(jb4DSession.getUserId());
                sourceEntity.setMenuOrderNum(menuMapper.nextOrderNum());
                MenuEntity parentEntity=null;
                if(StringUtility.isEmpty(sourceEntity.getMenuParentId())){
                    throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"请在实体中设置ParentId的值!");
                }
                if(!sourceEntity.getMenuParentId().equals("-1")){
                    parentEntity=menuMapper.selectByPrimaryKey(sourceEntity.getMenuParentId());
                    if(parentEntity==null){
                        throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"找不到父节点为"+sourceEntity.getMenuParentId()+"的记录!");
                    }
                    else
                    {
                        sourceEntity.setMenuParentIdList(parentEntity.getMenuParentIdList()+"*"+sourceEntity.getMenuId());
                    }
                }
                else
                {
                    sourceEntity.setMenuParentIdList("-1*"+sourceEntity.getMenuId());
                }

                sourceEntity.setMenuCreateTime(new Date());
                sourceEntity.setMenuChildCount(0);
                sourceEntity.setMenuUpdater(jb4DSession.getUserName());
                sourceEntity.setMenuUpdateTime(new Date());
                return sourceEntity;
            }
        });
    }

    private MenuEntity createMenu(JB4DCSession jb4DSession,String parentId,String menuId,String name,String text,String value,String rightUrl,String iconClassName) throws JBuild4DCGenerallyException {
        //String systemSettingCacheManageId="JB4DSystemSettingCacheManage";
        MenuEntity newMenu=getMenu(parentId,menuId,name,text,value,
                MenuTypeEnum.LeftMenu.getDisplayName(),"",rightUrl,iconClassName,"JB4DC-SSO");
        deleteByKey(jb4DSession,newMenu.getMenuId());
        saveSimple(jb4DSession,newMenu.getMenuId(),newMenu);
        return newMenu;
    }

    @Override
    public void initSystemData(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException {
        //根菜单
        String rootMenuId="0";
        MenuEntity rootMenu=getMenu("-1",rootMenuId,"Root","Root","Root", MenuTypeEnum.Root.getDisplayName(),"","","","JB4DC-Global");
        deleteByKey(jb4DSession,rootMenu.getMenuId());
        saveSimple(jb4DSession,rootMenu.getMenuId(),rootMenu);

        //根菜单->系统设置分组
        MenuEntity systemSettingRootMenu=createMenu(jb4DSession,rootMenu.getMenuId(),"JB4DSystemSettingRoot","系统设置","系统设置","系统设置","","menu-data");

        //根菜单->系统设置分组->数据字典分组
        createMenu(jb4DSession,systemSettingRootMenu.getMenuId(),"systemSettingDictionaryManagerId",
                "数据字典","数据字典","数据字典",
                "/HTML/SystemSetting/Dictionary/DictionaryManager.html","");

        //根菜单->系统设置分组->操作日志
        createMenu(jb4DSession,systemSettingRootMenu.getMenuId(),"JB4DSystemSettingOperationLog",
                "操作日志","操作日志","操作日志",
                "/HTML/SystemSetting/OperationLog/OperationLogList.html","");

        //根菜单->系统设置分组->参数设置
        createMenu(jb4DSession,systemSettingRootMenu.getMenuId(),"JB4DSystemSettingParasSetting",
                "参数设置","参数设置","参数设置",
                "/HTML/SystemSetting/ParasSetting/ParasSettingList.html","");

        //根菜单->统一用户与单点登录
        MenuEntity ssoRootMenu=createMenu(jb4DSession,rootMenu.getMenuId(),"JB4DSSORootMenu",
                "单点登录与统一用户","单点登录与统一用户","单点登录与统一用户",
                "","");

        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"JB4DOrganTypeManage",
                "组织类型","组织类型","组织类型",
                "/HTML/SSO/OrganType/OrganTypeList.html","");

        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"JB4DOrganManage",
                "组织机构","组织机构","组织机构",
                "/HTML/SSO/Organ/OrganList.html","");

        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"JB4DDepartmentManage",
                "部门管理","部门管理","部门管理",
                "/HTML/SSO/Department/DepartmentManager.html","");

        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"JB4DRoleManage",
                "角色管理","角色管理","角色管理",
                "/HTML/SSO/Role/RoleManager.html","");

        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"JB4DAppManage",
                "应用管理","应用管理","应用管理",
                "/HTML/SSO/Application/ApplicationManager.html","");

        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"JB4DMenuManage",
                "菜单管理","菜单管理","菜单管理",
                "/HTML/SSO/Menu/MenuManager.html","");
    }

    public MenuEntity getMenu(String parentId,String id,String name,String text,String value,String type,String leftUrl,String rightUrl,String iconClassName,String systemId){
        MenuEntity menuEntity=new MenuEntity();
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
        menuEntity.setMenuSystemId(systemId);
        return menuEntity;
    }
}


