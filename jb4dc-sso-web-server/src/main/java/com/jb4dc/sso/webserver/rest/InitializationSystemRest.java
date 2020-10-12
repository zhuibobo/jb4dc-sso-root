package com.jb4dc.sso.webserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jb4dc.base.service.IOperationLogService;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.ymls.JBuild4DCYaml;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.service.application.ISsoAppService;
import com.jb4dc.sso.service.menu.IMenuService;
import com.jb4dc.sso.service.organ.IOrganService;
import com.jb4dc.sso.service.organ.IOrganTypeService;
import com.jb4dc.sso.service.role.IRoleGroupService;
import com.jb4dc.sso.service.systemsetting.IDictionaryGroupService;
import com.jb4dc.sso.service.systemsetting.IDictionaryService;
import com.jb4dc.sso.service.systemsetting.ISettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/11
 * To change this template use File | Settings | File Templates.
 */

@RestController
@RequestMapping(value = "/Rest/InitializationSystem")
public class InitializationSystemRest {



    @Autowired
    private IMenuService menuService;

    @Autowired
    private IOrganService organService;

    @Autowired
    private IOrganTypeService organTypeService;

    @Autowired
    private IRoleGroupService roleGroupService;

    @Autowired
    private ISsoAppService ssoAppService;

    @RequestMapping(value = "/Running", method = RequestMethod.POST)
    @ResponseBody
    public JBuild4DCResponseVo running(String createTestData) throws JBuild4DCGenerallyException, JsonProcessingException {
        JB4DCSession jb4DSession= JB4DCSessionUtility.getInitSystemSession();

        //初始化菜单
        menuService.initSystemData(jb4DSession);

        //初始化根组织
        organService.initSystemData(jb4DSession);

        roleGroupService.initSystemData(jb4DSession);

        ssoAppService.initSystemData(jb4DSession);

        if(createTestData!=null&&createTestData.toLowerCase().equals("true")){
            //创建测试的表分组
            //TableGroupEntity personDBGroup=tableGroupService.createForDemoSystem(jb4DSession);
        }

        return JBuild4DCResponseVo.success("系统数据初始化成功！");
    }

}
