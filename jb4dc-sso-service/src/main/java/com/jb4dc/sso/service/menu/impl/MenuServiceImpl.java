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
import java.util.List;

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
                if(sourceEntity.getMenuParentId().equals("0")){
                    sourceEntity.setMenuParentIdList("0*"+sourceEntity.getMenuId());
                }
                else{
                    parentEntity=menuMapper.selectByPrimaryKey(sourceEntity.getMenuParentId());
                    if(parentEntity==null){
                        throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"找不到父节点为"+sourceEntity.getMenuParentId()+"的记录!");
                    }
                    else
                    {
                        sourceEntity.setMenuParentIdList(parentEntity.getMenuParentIdList()+"*"+sourceEntity.getMenuId());
                    }
                    parentEntity.setMenuChildCount(parentEntity.getMenuChildCount()+1);
                    menuMapper.updateByPrimaryKeySelective(parentEntity);
                }

                sourceEntity.setMenuCreateTime(new Date());
                sourceEntity.setMenuChildCount(0);
                sourceEntity.setMenuUpdater(jb4DSession.getUserName());
                sourceEntity.setMenuUpdateTime(new Date());
                return sourceEntity;
            }
        });
    }

    private MenuEntity createMenu(JB4DCSession jb4DSession,String parentId,String menuId,String name,String text,String value,String rightUrl,String iconClassName,String systemId) throws JBuild4DCGenerallyException {
        //String systemSettingCacheManageId="JB4DSystemSettingCacheManage";
        MenuEntity newMenu=getMenu(parentId,menuId,name,text,value,
                MenuTypeEnum.LeftMenu.getDisplayName(),"",rightUrl,iconClassName,systemId);
        deleteByKey(jb4DSession,newMenu.getMenuId());
        saveSimple(jb4DSession,newMenu.getMenuId(),newMenu);
        return newMenu;
    }

    @Override
    public void initSystemData(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException {
        //根菜单
        String rootMenuId="0";
        //MenuEntity rootMenu=getMenu("-1",rootMenuId,"Root","Root","Root", MenuTypeEnum.Root.getDisplayName(),"","","","SSOMainApp");
        //deleteByKey(jb4DSession,rootMenu.getMenuId());
        //saveSimple(jb4DSession,rootMenu.getMenuId(),rootMenu);

        //根菜单->系统设置分组
        String systemId = "SSOMainApp";
        MenuEntity systemSettingRootMenu=createMenu(jb4DSession,rootMenuId,"JB4DSystemSettingRoot","系统设置","系统设置","系统设置","","menu-data", systemId);

        //根菜单->系统设置分组->数据字典分组
        createMenu(jb4DSession,systemSettingRootMenu.getMenuId(),"systemSettingDictionaryManagerId",
                "数据字典","数据字典","数据字典",
                "/HTML/SystemSetting/Dictionary/DictionaryManager.html","", systemId);

        //根菜单->系统设置分组->操作日志
        createMenu(jb4DSession,systemSettingRootMenu.getMenuId(),"JB4DSystemSettingOperationLog",
                "操作日志","操作日志","操作日志",
                "/HTML/SystemSetting/OperationLog/OperationLogList.html","", systemId);

        //根菜单->系统设置分组->参数设置
        createMenu(jb4DSession,systemSettingRootMenu.getMenuId(),"JB4DSystemSettingParasSetting",
                "参数设置","参数设置","参数设置",
                "/HTML/SystemSetting/ParasSetting/ParasSettingList.html","", systemId);

        //根菜单->统一用户与单点登录
        MenuEntity ssoRootMenu=createMenu(jb4DSession,rootMenuId,"JB4DSSORootMenu",
                "单点登录与统一用户","单点登录与统一用户","单点登录与统一用户",
                "","", systemId);

        //根菜单->统一用户与单点登录->组织类型
        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"JB4DOrganTypeManage",
                "组织类型","组织类型","组织类型",
                "/HTML/SSO/OrganType/OrganTypeList.html","", systemId);

        //根菜单->统一用户与单点登录->组织机构
        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"JB4DOrganManage",
                "组织机构","组织机构","组织机构",
                "/HTML/SSO/Organ/OrganList.html","", systemId);

        //根菜单->统一用户与单点登录->部门管理
        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"JB4DDepartmentManage",
                "部门管理","部门管理","部门管理",
                "/HTML/SSO/Department/DepartmentManager.html","", systemId);

        //根菜单->统一用户与单点登录->角色管理
        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"JB4DRoleManage",
                "角色管理","角色管理","角色管理",
                "/HTML/SSO/Role/RoleManager.html","", systemId);

        //根菜单->统一用户与单点登录->应用管理
        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"JB4DAppManage",
                "应用管理","应用管理","应用管理",
                "/HTML/SSO/Application/ApplicationManager.html","", systemId);

        //根菜单->统一用户与单点登录->菜单管理
        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"JB4DMenuManage",
                "菜单管理","菜单管理","菜单管理",
                "/HTML/SSO/Menu/MenuManager.html","", systemId);

        systemId = "DevMockApp";
        //根菜单->开发示例
        MenuEntity devDemoRootMenu=createMenu(jb4DSession,rootMenuId,"JB4DDevDemoRoot",
                "开发示例","开发示例","开发示例",
                "","",systemId);

        //根菜单->开发示例->通用列表(带查询)
        MenuEntity devDemoRootMenu_SearchList=createMenu(jb4DSession,devDemoRootMenu.getMenuId(),"JB4DDevDemoRoot_SearchList",
                "通用列表(带查询)","通用列表(带查询)","通用列表(带查询)",
                "DevDemo/GenList/GenList.html","menu-data",systemId);

        //根菜单->开发示例->通用列表(不带查询)
        MenuEntity devDemoRootMenu_NoSearchList=createMenu(jb4DSession,devDemoRootMenu.getMenuId(),"JB4DDevDemoRoot_NoSearchList",
                "通用列表(不带查询)","通用列表(不带查询)","通用列表(不带查询)",
                "DevDemo/GenList/GenListNotSearch.html","menu-data",systemId);

        //根菜单->开发示例->通用列表(绑定数据字典)
        MenuEntity devDemoRootId_BindDictSearchList=createMenu(jb4DSession,devDemoRootMenu.getMenuId(),"JB4DDevDemoRoot_BindDictSearchList",
                "通用列表(绑定数据字典)","通用列表(绑定数据字典)","通用列表(绑定数据字典)",
                "DevDemo/GenList/GenListBindDictionary.html","menu-data",systemId);

        //根菜单->开发示例->通用列表(弹出列表)
        MenuEntity devDemoRootId_DialogSearchList=createMenu(jb4DSession,devDemoRootMenu.getMenuId(),"JB4DDevDemoRoot_DialogSearchList",
                "通用列表(弹出列表)","通用列表(弹出列表)","通用列表(弹出列表)",
                "DevDemo/DevDemoGenList/ListView","menu-data",systemId);

        //根菜单->开发示例->树形表格
        MenuEntity devDemoRootId_TreeTable=createMenu(jb4DSession,devDemoRootMenu.getMenuId(),"devDemoRootId_TreeTable",
                "树形表格","树形表格","树形表格",
                "DevDemo/TreeTable/TreeTableList.html","menu-data",systemId);

        //根菜单->开发示例->树与列表
        MenuEntity devDemoRootId_TreeAndList=createMenu(jb4DSession,devDemoRootMenu.getMenuId(),"devDemoRootId_TreeList",
                "树与列表","树与列表","树与列表",
                "DevDemo/TreeAndList/Manager.html","menu-data",systemId);

        systemId = "BuilderMainApp";

        //根菜单->应用设计
        MenuEntity appBuilderRootMenu=createMenu(jb4DSession,rootMenuId,"JB4DSystemAppBuilderRoot",
                "应用设计","应用设计","应用设计",
                "","menu-data",systemId);

        //根菜单->应用管理->存储设计
        MenuEntity appManagerDataStorageMenu=createMenu(jb4DSession,appBuilderRootMenu.getMenuId(),"JB4DSystemAppBuilderDataStorageBuilder",
                "存储设计","存储设计","存储设计",
                "","top-menu-data",systemId);


        //根菜单->应用管理->存储设计->服务链接
        /*MenuEntity appBuilderDataLinkMenu=createMenu(jb4DSession,appManagerDataStorageMenu.getMenuId(),"JB4DSystemAppBuilderDataLink",
                "服务链接","服务链接","服务链接",
                "","top-menu-data");*/

        //根菜单->应用管理->存储设计->数据库连接
        MenuEntity appBuilderDBLinkMenu=createMenu(jb4DSession,appManagerDataStorageMenu.getMenuId(),"JB4DSystemAppBuilderDBLink",
                "数据库连接","数据库连接","数据库连接",
                "Builder/DataStorage/DBLink/DBLinkList.html","frame-top-menu-data",systemId);

        //根菜单->应用管理->存储设计->数据库管理
        MenuEntity appBuilderDataBaseMenu=createMenu(jb4DSession,appManagerDataStorageMenu.getMenuId(),"JB4DSystemAppBuilderDataBase",
                "数据库管理","数据库管理","数据库管理",
                "Builder/DataStorage/DataBase/Manager.html","frame-top-menu-data",systemId);

        //根菜单->应用管理->存储设计->数据关系
        MenuEntity appBuilderDataRelationMenu=createMenu(jb4DSession,appManagerDataStorageMenu.getMenuId(),"JB4DSystemAppBuilderDataRelation",
                "数据关系","数据关系","数据关系",
                "Builder/DataStorage/DataRelation/Manager.html","frame-top-menu-data",systemId);

        //根菜单->应用管理->数据集设计
        MenuEntity appBuilderDataSetMenu=createMenu(jb4DSession,appBuilderRootMenu.getMenuId(),"JB4DSystemAppBuilderDataSetBuilder",
                "数据集设计","数据集设计","数据集设计",
                "Builder/DataSet/Manager.html","top-menu-data",systemId);

        //根菜单->应用管理->模块设计
        MenuEntity appBuilderModuleBuilderMenu=createMenu(jb4DSession,appBuilderRootMenu.getMenuId(),"JB4DSystemAppBuilderModuleBuilder",
                "模块设计","模块设计","模块设计",
                "Builder/Module/Manager.html","frame-top-menu-data",systemId);

        //根菜单->应用管理->菜单设计
        MenuEntity appBuilderMenuBuilderMenu=createMenu(jb4DSession,appBuilderRootMenu.getMenuId(),"JB4DSystemAppBuilderMenuBuilder",
                "菜单设计","菜单设计","菜单设计",
                "","top-menu-data",systemId);

        //根菜单->应用管理->桌面设计
        MenuEntity appBuilderDesktopBuilderMenu=createMenu(jb4DSession,appBuilderRootMenu.getMenuId(),"JB4DSystemAppBuilderDesktopBuilder",
                "Portal设计","Portal设计","Portal设计",
                "Portal/LayoutPreview.html","frame-top-menu-data",systemId);

        //根菜单->应用管理->大屏设计
        createMenu(jb4DSession,appBuilderRootMenu.getMenuId(),"JB4DSystemAppBuilderBigScreen",
                "大屏设计","大屏设计","大屏设计",
                "","frame-top-menu-data",systemId);
    }

    @Override
    public List<MenuEntity> getMenusBySystemId(JB4DCSession session, String systemId) {
        return menuMapper.selectBySystemId(systemId);
    }

    @Override
    public List getMyFrameMenu(JB4DCSession jb4DCSession, String systemId) {
        return menuMapper.selectBySystemId(systemId);
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


