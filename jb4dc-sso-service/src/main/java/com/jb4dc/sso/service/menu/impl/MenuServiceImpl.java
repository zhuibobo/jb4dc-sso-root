package com.jb4dc.sso.service.menu.impl;


import com.jb4dc.base.service.exenum.MenuTypeEnum;
import com.jb4dc.base.service.exenum.TrueFalseEnum;
import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.exenum.UserTypeEnum;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.sso.dao.menu.MenuMapper;
import com.jb4dc.sso.dbentities.menu.MenuEntity;
import com.jb4dc.sso.dbentities.user.UserEntity;
import com.jb4dc.sso.service.menu.IMenuService;
import com.jb4dc.sso.service.user.IUserService;
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
    IUserService userService;

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
                if(parentEntity!=null) {
                    sourceEntity.setMenuSystemId(parentEntity.getMenuSystemId());
                }
                else if(StringUtility.isEmpty(sourceEntity.getMenuSystemId())){
                    throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"SystemId不能为空!:"+sourceEntity.getMenuName());
                }
                sourceEntity.setMenuIsExpand(TrueFalseEnum.False.getDisplayName());
                return sourceEntity;
            }
        });
    }

    @Override
    public void initSystemData(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException {
        //根菜单
        String rootMenuId="0";
        //MenuEntity rootMenu=getMenu("-1",rootMenuId,"Root","Root","Root", MenuTypeEnum.Root.getDisplayName(),"","","","SSOMainApp");
        //deleteByKey(jb4DSession,rootMenu.getMenuId());
        //saveSimple(jb4DSession,rootMenu.getMenuId(),rootMenu);

        createSSOSystem(jb4DSession,rootMenuId);
        createDevDemoSystem(jb4DSession,rootMenuId);
        createBuilderSystem(jb4DSession,rootMenuId);
        createGridSystem(jb4DSession,rootMenuId);
        //createQCSystem(jb4DSession,rootMenuId);
        //createHrSystem(jb4DSession,rootMenuId);
        //createKYGL(jb4DSession,rootMenuId);
        //createZSGL(jb4DSession,rootMenuId);
    }

    public void createSSOSystem(JB4DCSession jb4DSession,String rootMenuId) throws JBuild4DCGenerallyException {
        //根菜单->系统设置分组
        String systemId = "SSOMainApp";

        //根菜单->统一用户与单点登录
        MenuEntity ssoRootMenu=createMenu(jb4DSession,rootMenuId,"MENU-JB4DSSORoot",
                "单点登录与统一用户","单点登录与统一用户","单点登录与统一用户",
                "","", systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->统一用户与单点登录->组织类型
        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"MENU-JB4DOrganTypeManage",
                "组织类型","组织类型","组织类型",
                "/HTML/SSO/OrganType/OrganTypeList.html","", systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->统一用户与单点登录->组织机构
        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"MENU-JB4DOrganManage",
                "组织机构","组织机构","组织机构",
                "/HTML/SSO/Organ/OrganList.html","", systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->统一用户与单点登录->部门管理
        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"MENU-JB4DDepartmentManage",
                "部门管理","部门管理","部门管理",
                "/HTML/SSO/Department/DepartmentManager.html","", systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->统一用户与单点登录->角色管理
        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"MENU-JB4DRoleManage",
                "角色管理","角色管理","角色管理",
                "/HTML/SSO/Role/RoleManager.html","", systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->统一用户与单点登录->应用管理
        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"MENU-JB4DAppManage",
                "应用管理","应用管理","应用管理",
                "/HTML/SSO/Application/ApplicationManager.html","", systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->统一用户与单点登录->菜单管理
        createMenu(jb4DSession,ssoRootMenu.getMenuId(),"MENU-JB4DMenuManage",
                "菜单管理","菜单管理","菜单管理",
                "/HTML/SSO/Menu/MenuManager.html","", systemId,MenuTypeEnum.LinkMenu,"","");

        MenuEntity systemSettingRootMenu=createMenu(jb4DSession,rootMenuId,"MENU-SSO-JB4DSystemSettingRoot","系统设置","系统设置","系统设置","","menu-data", systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->系统设置分组->操作日志
        createMenu(jb4DSession,systemSettingRootMenu.getMenuId(),"MENU-JB4DSystemSettingOperationLog",
                "操作日志","操作日志","操作日志",
                "/HTML/SystemSetting/OperationLog/OperationLogList.html","", systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->系统设置分组->参数设置
        createMenu(jb4DSession,systemSettingRootMenu.getMenuId(),"MENU-JB4DSystemSettingParasSetting",
                "参数设置","参数设置","参数设置",
                "/HTML/SystemSetting/ParasSetting/ParasSettingList.html","", systemId,MenuTypeEnum.LinkMenu,"","");
    }

    public void createDevDemoSystem(JB4DCSession jb4DSession,String rootMenuId) throws JBuild4DCGenerallyException {
        String systemId = "DevMockApp";
        //根菜单->开发示例
        MenuEntity devDemoRootMenu=createMenu(jb4DSession,rootMenuId,"MENU-JB4DDevDemoRoot",
                "开发示例","开发示例","开发示例",
                "","",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->开发示例->通用列表(带查询)
        MenuEntity devDemoRootMenu_SearchList=createMenu(jb4DSession,devDemoRootMenu.getMenuId(),"MENU-JB4DDevDemoRoot-SearchList",
                "通用列表(带查询)","通用列表(带查询)","通用列表(带查询)",
                "/HTML/DevDemo/GenList/GenList.html","menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->开发示例->通用列表(不带查询)
        MenuEntity devDemoRootMenu_NoSearchList=createMenu(jb4DSession,devDemoRootMenu.getMenuId(),"MENU-JB4DDevDemoRoot-NoSearchList",
                "通用列表(不带查询)","通用列表(不带查询)","通用列表(不带查询)",
                "/HTML/DevDemo/GenList/GenListNotSearch.html","menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->开发示例->通用列表(绑定数据字典)
        MenuEntity devDemoRootId_BindDictSearchList=createMenu(jb4DSession,devDemoRootMenu.getMenuId(),"MENU-JB4DDevDemoRoot-BindDictSearchList",
                "通用列表(绑定数据字典)","通用列表(绑定数据字典)","通用列表(绑定数据字典)",
                "/HTML/DevDemo/GenList/GenListBindDictionary.html","menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->开发示例->通用列表(弹出列表)
        /*MenuEntity devDemoRootId_DialogSearchList=createMenu(jb4DSession,devDemoRootMenu.getMenuId(),"MENU-JB4DDevDemoRoot-DialogSearchList",
                "通用列表(弹出列表)","通用列表(弹出列表)","通用列表(弹出列表)",
                "/HTML/DevDemo/DevDemoGenList/ListView","menu-data",systemId);*/

        //根菜单->开发示例->树形表格
        MenuEntity devDemoRootId_TreeTable=createMenu(jb4DSession,devDemoRootMenu.getMenuId(),"MENU-JB4DDevDemoRootId-TreeTable",
                "树形表格","树形表格","树形表格",
                "/HTML/DevDemo/TreeTable/TreeTableList.html","menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->开发示例->树与列表
        MenuEntity devDemoRootId_TreeAndList=createMenu(jb4DSession,devDemoRootMenu.getMenuId(),"MENU-JB4DDevDemoRootId-TreeList",
                "树与列表","树与列表","树与列表",
                "/HTML/DevDemo/TreeAndList/Manager.html","menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->开发示例->构建测试
        createMenu(jb4DSession,devDemoRootMenu.getMenuId(),"MENU-JB4DDevDemoRoot-List1",
                "列表测试1","列表测试1","列表测试1",
                "/HTML/Builder/Runtime/WebListRuntime.html","top-menu-data",systemId,MenuTypeEnum.ModuleWebListMenu,"DevMockListIdTest0001","构建库-开发样例列表1");
    }

    public void createBuilderSystem(JB4DCSession jb4DSession,String rootMenuId) throws JBuild4DCGenerallyException {
        String systemId = "BuilderMainApp";

        //根菜单->应用设计
        MenuEntity appBuilderRootMenu=createMenu(jb4DSession,rootMenuId,"MENU-JB4DSystemAppBuilderRoot",
                "应用设计","应用设计","应用设计",
                "","menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->应用管理->环境变量
        createMenu(jb4DSession,appBuilderRootMenu.getMenuId(),"MENU-JB4DSystemAppBuilderEnvVarManager",
                "环境变量","环境变量","环境变量",
                "/HTML/Builder/EnvVar/Manager.html","top-menu-data",systemId,MenuTypeEnum.LinkMenu,"","");


        //根菜单->应用管理->API
        createMenu(jb4DSession,appBuilderRootMenu.getMenuId(),"MENU-JB4DSystemAppBuilderAPIManager",
                "API","API","API",
                "/HTML/Builder/API/Manager.html","top-menu-data",systemId,MenuTypeEnum.LinkMenu,"","");


        //根菜单->应用管理->存储设计
        MenuEntity appManagerDataStorageMenu=createMenu(jb4DSession,appBuilderRootMenu.getMenuId(),"MENU-JB4DSystemAppBuilderDataStorageBuilder",
                "存储设计","存储设计","存储设计",
                "","top-menu-data",systemId,MenuTypeEnum.LinkMenu,"","");


        //根菜单->应用管理->存储设计->服务链接
        /*MenuEntity appBuilderDataLinkMenu=createMenu(jb4DSession,appManagerDataStorageMenu.getMenuId(),"JB4DSystemAppBuilderDataLink",
                "服务链接","服务链接","服务链接",
                "","top-menu-data");*/

        //根菜单->应用管理->存储设计->数据库连接
        MenuEntity appBuilderDBLinkMenu=createMenu(jb4DSession,appManagerDataStorageMenu.getMenuId(),"MENU-JB4DSystemAppBuilderDBLink",
                "数据库连接","数据库连接","数据库连接",
                "/HTML/Builder/DataStorage/DBLink/DBLinkList.html","frame-top-menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->应用管理->存储设计->数据库管理
        MenuEntity appBuilderDataBaseMenu=createMenu(jb4DSession,appManagerDataStorageMenu.getMenuId(),"MENU-JB4DSystemAppBuilderDataBase",
                "数据库管理","数据库管理","数据库管理",
                "/HTML/Builder/DataStorage/DataBase/Manager.html","frame-top-menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->应用管理->存储设计->数据关系
        MenuEntity appBuilderDataRelationMenu=createMenu(jb4DSession,appManagerDataStorageMenu.getMenuId(),"MENU-JB4DSystemAppBuilderDataRelation",
                "数据关系","数据关系","数据关系",
                "/HTML/Builder/DataStorage/DataRelation/Manager.html","frame-top-menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->应用管理->数据集设计
        MenuEntity appBuilderDataSetMenu=createMenu(jb4DSession,appBuilderRootMenu.getMenuId(),"MENU-JB4DSystemAppBuilderDataSetBuilder",
                "数据集设计","数据集设计","数据集设计",
                "/HTML/Builder/DataSet/Manager.html","top-menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->应用管理->模块设计
        MenuEntity appBuilderModuleBuilderMenu=createMenu(jb4DSession,appBuilderRootMenu.getMenuId(),"MENU-JB4DSystemAppBuilderModuleBuilder",
                "模块设计","模块设计","模块设计",
                "/HTML/Builder/Module/Manager.html","frame-top-menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->应用管理->站点管理
        MenuEntity appBuilderSiteMenu=createMenu(jb4DSession,appBuilderRootMenu.getMenuId(),"MENU-JB4DSystemAppBuilderSite",
                "站点管理","站点管理","站点管理",
                "/HTML/Builder/Site/Manager.html","frame-top-menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        MenuEntity systemSettingRootMenu=createMenu(jb4DSession,rootMenuId,"MENU-Builder-JB4DSystemSettingRoot","系统设置","系统设置","系统设置","","menu-data", systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->系统设置分组->数据字典分组
        createMenu(jb4DSession,systemSettingRootMenu.getMenuId(),"MENU-JB4DSystemSettingDictionaryManagerId",
                "数据字典","数据字典","数据字典",
                "/HTML/SystemSetting/Dictionary/DictionaryManager.html","", systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->应用管理->菜单设计
        /*MenuEntity appBuilderMenuBuilderMenu=createMenu(jb4DSession,appBuilderRootMenu.getMenuId(),"MENU-JB4DSystemAppBuilderMenuBuilder",
                "菜单设计","菜单设计","菜单设计",
                "","top-menu-data",systemId);*/

        //根菜单->桌面设计
        MenuEntity appBuilderDesktopBuilderMenu=createMenu(jb4DSession,rootMenuId,"MENU-JB4DSystemAppBuilderDesktopBuilder",
                "Portal设计","Portal设计","Portal设计",
                "/HTML/Portal/LayoutPreview.html","frame-top-menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->大屏设计
        createMenu(jb4DSession,rootMenuId,"MENU-JB4DSystemAppBuilderBigScreen",
                "大屏设计","大屏设计","大屏设计",
                "","frame-top-menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        //根菜单->模块测试
        MenuEntity appBuilderModuleTestRootMenu=createMenu(jb4DSession,rootMenuId,"MENU-JB4DSystemAppBuilderModuleTestRoot",
                "模块测试","模块测试","模块测试",
                "","frame-top-menu-data",systemId,MenuTypeEnum.LinkMenu,"","");

        createMenu(jb4DSession,appBuilderModuleTestRootMenu.getMenuId(),"MENU-JB4DSystemAppBuilderModuleTestRoot-List1",
                "列表测试1","列表测试1","列表测试1",
                "/HTML/Builder/Runtime/WebListRuntime.html","top-menu-data",systemId,MenuTypeEnum.ModuleWebListMenu,"BuilderListIdTest0001","构建库-开发样例列表1");
    }

    public void createQCSystem(JB4DCSession jb4DSession,String rootMenuId) throws JBuild4DCGenerallyException {
        String systemId = "QCSystem";
        String menuId="";
        String menuName="";

        menuId="QCSystem-SystemSettingRoot";
        menuName="系统设置";
        MenuEntity systemSettingRoot=createMenu(jb4DSession,rootMenuId,menuId,
                menuName,menuName,menuName,
                "","",systemId,MenuTypeEnum.EmptyMenu,menuId,menuId);

        menuId="QCSystem-SystemTemplateManage";
        menuName="模版管理";
        MenuEntity systemTemplateManage=createMenu(jb4DSession,systemSettingRoot.getMenuId(),menuId,
                menuName,menuName,menuName,
                "","",systemId,MenuTypeEnum.EmptyMenu,menuId,menuId);

        menuId="QCSystem-SystemLog";
        menuName="系统日志";
        MenuEntity systemLog=createMenu(jb4DSession,systemSettingRoot.getMenuId(),menuId,
                menuName,menuName,menuName,
                "","",systemId,MenuTypeEnum.EmptyMenu,menuId,menuId);

        menuId="QCSystem-ProjectManagement";
        menuName="项目管理";
        MenuEntity systemManage=createMenu(jb4DSession,rootMenuId,menuId,
                menuName,menuName,menuName,
                "","",systemId,MenuTypeEnum.EmptyMenu,menuId,menuId);


        menuId="QCSystem-IssuesManage";
        menuName="运维问题管理";
        MenuEntity issuesManage=createMenu(jb4DSession,rootMenuId,menuId,
                menuName,menuName,menuName,
                "","",systemId,MenuTypeEnum.EmptyMenu,"",menuId);

        menuId="QCSystem-MyIssuesManage";
        menuName="我的运维问题";
        MenuEntity myIssuesManage=createMenu(jb4DSession,rootMenuId,menuId,
                menuName,menuName,menuName,
                "","",systemId,MenuTypeEnum.EmptyMenu,menuId,menuId);

        menuId="QCSystem-MySubscription";
        menuName="我的订阅";
        MenuEntity mySubscription=createMenu(jb4DSession,rootMenuId,menuId,
                menuName,menuName,menuName,
                "","",systemId,MenuTypeEnum.EmptyMenu,menuId,menuId);

        menuId="QCSystem-IssuesSearch";
        menuName="运维问题查询";
        MenuEntity issuesSearch=createMenu(jb4DSession,rootMenuId,menuId,
                menuName,menuName,menuName,
                "","",systemId,MenuTypeEnum.EmptyMenu,menuId,menuId);

        menuId="QCSystem-StatisticsRoot";
        menuName="运维统计";
        MenuEntity statisticsRoot=createMenu(jb4DSession,rootMenuId,menuId,
                menuName,menuName,menuName,
                "","",systemId,MenuTypeEnum.EmptyMenu,menuId,menuId);

        menuId="QCSystem-StatisticsIssues";
        menuName="问题分类统计";
        MenuEntity issuesStatistics=createMenu(jb4DSession,statisticsRoot.getMenuId(),menuId,
                menuName,menuName,menuName,
                "","",systemId,MenuTypeEnum.EmptyMenu,menuId,menuId);

        menuId="QCSystem-StatisticsWeeklyReport";
        menuName="运维周报";
        MenuEntity statisticsWeeklyReport=createMenu(jb4DSession,statisticsRoot.getMenuId(),menuId,
                menuName,menuName,menuName,
                "","",systemId,MenuTypeEnum.EmptyMenu,menuId,menuId);

        menuId="QCSystem-StatisticsMonthlyReport";
        menuName="运维月报";
        MenuEntity statisticsMonthlyReport=createMenu(jb4DSession,statisticsRoot.getMenuId(),menuId,
                menuName,menuName,menuName,
                "","",systemId,MenuTypeEnum.EmptyMenu,menuId,menuId);

        menuId="QCSystem-JobPlan";
        menuName="作业计划";
        MenuEntity jobPlan=createMenu(jb4DSession,rootMenuId,menuId,
                menuName,menuName,menuName,
                "","",systemId,MenuTypeEnum.EmptyMenu,menuId,menuId);
    }

    public void createGridSystem(JB4DCSession jb4DSession,String rootMenuId) throws JBuild4DCGenerallyException {
        String systemId = "GridSystem";
        String menuId = "";
        String menuName = "";

        menuId = "GridSystem-GridSettingRoot";
        menuName = "网格管理";
        MenuEntity gridSettingRoot = createMenu(jb4DSession, rootMenuId, menuId,
                menuName, menuName, menuName,
                "/HTML/Grid/GridInfo/GridManager.html", "", systemId, MenuTypeEnum.LinkMenu, menuId, menuId);

        menuId = "GridSystem-Gather-Terminal-Root";
        menuName = "采集设备管理";
        MenuEntity gatherTerminalRoot = createMenu(jb4DSession, rootMenuId, menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-MyGridRoot";
        menuName = "我的网格";
        MenuEntity myGridRoot = createMenu(jb4DSession, rootMenuId, menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-MyGridRoot-BuildHousePerson";
        menuName = "楼房登记";
        createMenu(jb4DSession, myGridRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "/HTML/Grid/BuildHouse/MyGridBuildHouseManager.html", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-MyGridRoot-Person";
        menuName = "人口登记";
        createMenu(jb4DSession, myGridRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-MyGridRoot-GridEvent-Root";
        menuName = "网格事件";
        createMenu(jb4DSession, myGridRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-MyGridRoot-SpecialBuild";
        menuName = "特殊建筑物";
        createMenu(jb4DSession, myGridRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-MyGridRoot-Enterprise";
        menuName = "企业法人";
        createMenu(jb4DSession, myGridRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-MyGridRoot-GatherStatistics";
        menuName = "采集统计";
        createMenu(jb4DSession, myGridRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-DataSearch-Root";
        menuName = "数据检索";
        MenuEntity dataSearchRoot = createMenu(jb4DSession, rootMenuId, menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-DataSearch-Build";
        menuName = "楼";
        createMenu(jb4DSession, dataSearchRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-DataSearch-House";
        menuName = "房";
        createMenu(jb4DSession, dataSearchRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-DataSearch-Person";
        menuName = "人";
        createMenu(jb4DSession, dataSearchRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-DataSearch-SpecialBuild";
        menuName = "特殊建筑物";
        createMenu(jb4DSession, dataSearchRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-DataSearch-GridEvent";
        menuName = "网格事件";
        createMenu(jb4DSession, dataSearchRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-DataStatistics-Root";
        menuName = "数据统计";
        MenuEntity dataStatisticsRoot = createMenu(jb4DSession, rootMenuId, menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-DataStatistics-BuildStatistics";
        menuName = "建筑物统计";
        createMenu(jb4DSession, dataStatisticsRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-DataStatistics-HouseStatistics";
        menuName = "房屋统计";
        createMenu(jb4DSession, dataStatisticsRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-DataStatistics-PersonStatistics";
        menuName = "人口统计";
        createMenu(jb4DSession, dataStatisticsRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);

        menuId = "GridSystem-DataStatistics-GridEventStatistics";
        menuName = "事件统计";
        createMenu(jb4DSession, dataStatisticsRoot.getMenuId(), menuId,
                menuName, menuName, menuName,
                "", "", systemId, MenuTypeEnum.EmptyMenu, menuId, menuId);
    }
    /*public void createHrSystem(JB4DCSession jb4DSession,String rootMenuId) throws JBuild4DCGenerallyException {
        String systemId = "HrSystem";
        MenuEntity hrOrganizationRootMenu=createMenu(jb4DSession,rootMenuId,"Hr-Organization-Root-Menu",
                "组织架构管理","组织架构管理","组织架构管理",
                "","",systemId,MenuTypeEnum.EmptyMenu,"","Hr-Organization-Root-Menu");


        MenuEntity organizationManager=createMenu(jb4DSession,hrOrganizationRootMenu.getMenuId(),"Hr-Organization-Manager-Menu",
                "组织管理","组织管理","组织管理",
                "","",systemId,MenuTypeEnum.EmptyMenu,"","Hr-OrganizationMenu");

        MenuEntity organizationDepartmentManager=createMenu(jb4DSession,hrOrganizationRootMenu.getMenuId(),"Hr-Organization-Manager-Menu",
                "部门人员","部门人员","部门人员",
                "","",systemId,MenuTypeEnum.EmptyMenu,"","Hr-OrganizationMenu");

    }

    public void createKYGL(JB4DCSession jb4DSession,String rootMenuId) throws JBuild4DCGenerallyException {
        String systemId = "DevMockApp";
        MenuEntity 科研管理=createMenu(jb4DSession,rootMenuId,"KYGLDevApp001",
                "科研管理","科研管理","科研管理",
                "/HTML/DevDemo/KYGL/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","KYGL");
        MenuEntity 博士后工作站=createMenu(jb4DSession,rootMenuId,"KYGLDevApp002",
                "博士后工作站","博士后工作站","博士后工作站",
                "/HTML/DevDemo/RLZY/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","KYGL");
        MenuEntity 科研项目=createMenu(jb4DSession,rootMenuId,"KYGLDevApp003",
                "科研项目","科研项目","科研项目",
                "/HTML/DevDemo/RLZY/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","KYGL");
        MenuEntity 科研成果库=createMenu(jb4DSession,rootMenuId,"KYGLDevApp004",
                "科研成果库","科研成果库","科研成果库",
                "/HTML/DevDemo/RLZY/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","KYGL");
        MenuEntity 学术活动管理=createMenu(jb4DSession,rootMenuId,"KYGLDevApp005",
                "学术活动管理","学术活动管理","学术活动管理",
                "/HTML/DevDemo/RLZY/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","KYGL");
        MenuEntity 科研信息采集=createMenu(jb4DSession,rootMenuId,"KYGLDevApp006",
                "科研信息采集","科研信息采集","科研信息采集",
                "/HTML/DevDemo/RLZY/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","KYGL");
        MenuEntity 科研采集信息库=createMenu(jb4DSession,rootMenuId,"KYGLDevApp007",
                "科研采集信息库","科研采集信息库","科研采集信息库",
                "/HTML/DevDemo/RLZY/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","KYGL");
    }

    public void createZSGL(JB4DCSession jb4DSession,String rootMenuId) throws JBuild4DCGenerallyException {
        String systemId = "DevMockApp";
        MenuEntity 科研管理=createMenu(jb4DSession,rootMenuId,"ZSGLDevApp001",
                "知识管理","知识管理","知识管理",
                "/HTML/DevDemo/ZSGL/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","ZSGL");

        MenuEntity 知识库管理=createMenu(jb4DSession,rootMenuId,"ZSGLDevApp006",
                "知识库管理","知识库管理","知识库管理",
                "/HTML/DevDemo/ZSGL/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","ZSGL");

        MenuEntity 解决方案库=createMenu(jb4DSession,rootMenuId,"ZSGLDevApp002",
                "解决方案库","解决方案库","解决方案库",
                "/HTML/DevDemo/ZSGL/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","ZSGL");

        MenuEntity 模板管理=createMenu(jb4DSession,rootMenuId,"ZSGLDevApp003",
                "模板管理","模板管理","模板管理",
                "/HTML/DevDemo/ZSGL/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","ZSGL");

        MenuEntity 公告与问答=createMenu(jb4DSession,rootMenuId,"ZSGLDevApp004",
                "公告与问答","公告与问答","公告与问答",
                "/HTML/DevDemo/ZSGL/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","ZSGL");

        MenuEntity 知识统计=createMenu(jb4DSession,rootMenuId,"ZSGLDevApp005",
                "知识统计","知识统计","知识统计",
                "/HTML/DevDemo/ZSGL/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","ZSGL");

        MenuEntity 我的设置=createMenu(jb4DSession,rootMenuId,"ZSGLDevApp007",
                "我的设置","我的设置","我的设置",
                "/HTML/DevDemo/ZSGL/Index1.html","",systemId,MenuTypeEnum.LinkMenu,"","ZSGL");
    }*/

    @Override
    public List<MenuEntity> getMenusBySystemId(JB4DCSession session, String systemId) {
        return menuMapper.selectBySystemId(systemId);
    }

    @Override
    public List getMyFrameMenu(JB4DCSession jb4DCSession, String systemId,String userId) {
        return menuMapper.getMyAuthMenusBySystemId(userId,systemId);
    }

    @Override
    public List<MenuEntity> getMyAuthMenusBySystemId(JB4DCSession session, String systemId) {
        if(session.isFullAuthority()){
            return menuMapper.selectBySystemId(systemId);
        }
        return menuMapper.getMyAuthMenusBySystemId(session.getUserId(),systemId);
    }

    @Override
    public List<MenuEntity> getMyAuthMenusBySystemId(String userId, String systemId) throws JBuild4DCGenerallyException {
        UserEntity userEntity=userService.getByPrimaryKey(null,userId);
        if(userEntity.getUserType().equals(UserTypeEnum.manager.getDisplayName())){
            return menuMapper.selectBySystemId(systemId);
        }
        return menuMapper.getMyAuthMenusBySystemId(userId,systemId);
    }

    private MenuEntity createMenu(JB4DCSession jb4DSession,String parentId,String menuId,String name,String text,String value,String rightUrl,String iconClassName,String systemId,MenuTypeEnum menuTypeEnum,String outerId,String outerName) throws JBuild4DCGenerallyException {
        //String systemSettingCacheManageId="JB4DSystemSettingCacheManage";
        MenuEntity newMenu=getMenu(parentId,menuId,name,text,value,
                menuTypeEnum.getDisplayName(),"",rightUrl,iconClassName,systemId,outerId,outerName);
        deleteByKey(jb4DSession,newMenu.getMenuId());
        saveSimple(jb4DSession,newMenu.getMenuId(),newMenu);
        return newMenu;
    }

    public MenuEntity getMenu(String parentId,String id,String name,String text,String value,String type,String leftUrl,String rightUrl,String iconClassName,String systemId,String outerId,String outerName){
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
        menuEntity.setMenuOuterName(outerName);
        menuEntity.setMenuOuterId(outerId);
        return menuEntity;
    }

    @Override
    public void moveUp(JB4DCSession jb4DCSession, String id) throws JBuild4DCGenerallyException {
        MenuEntity selfEntity=menuMapper.selectByPrimaryKey(id);
        MenuEntity ltEntity=menuMapper.selectLessThanRecord(id,selfEntity.getMenuParentId());
        switchOrder(ltEntity,selfEntity);
    }

    @Override
    public void moveDown(JB4DCSession jb4DCSession, String id) throws JBuild4DCGenerallyException {
        super.moveDown(jb4DCSession, id);
    }

    private void switchOrder(MenuEntity toEntity,MenuEntity selfEntity) {
        if(toEntity !=null){
            int newNum= toEntity.getMenuOrderNum();
            toEntity.setMenuOrderNum(selfEntity.getMenuOrderNum());
            selfEntity.setMenuOrderNum(newNum);
            menuMapper.updateByPrimaryKeySelective(toEntity);
            menuMapper.updateByPrimaryKeySelective(selfEntity);
        }
    }
}


