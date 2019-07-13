package com.jb4dc.sso.webserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.service.menu.IMenuService;
import com.jb4dc.system.setting.service.IDictionaryGroupService;
import com.jb4dc.system.setting.service.IDictionaryService;
import com.jb4dc.system.setting.service.IOperationLogService;
import com.jb4dc.system.setting.service.ISettingService;
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
    private IDictionaryGroupService dictionaryGroupService;

    @Autowired
    private IDictionaryService dictionaryService;

    @Autowired
    private ISettingService settingService;

    @Autowired
    private IOperationLogService operationLogService;

    @Autowired
    private IMenuService menuService;

    @RequestMapping(value = "/Running", method = RequestMethod.POST)
    @ResponseBody
    public JBuild4DCResponseVo running(String createTestData) throws JBuild4DCGenerallyException, JsonProcessingException {
        JB4DCSession jb4DSession= JB4DCSessionUtility.getSession();

        //初始化字典
        dictionaryGroupService.initSystemData(jb4DSession,dictionaryService);

        //初始化系统参数
        settingService.initSystemData(jb4DSession);

        //初始化操作日志
        operationLogService.initSystemData(jb4DSession);

        menuService.initSystemData(jb4DSession);

        if(createTestData!=null&&createTestData.toLowerCase().equals("true")){
            //创建测试的表分组
            //TableGroupEntity personDBGroup=tableGroupService.createForDemoSystem(jb4DSession);
        }

        return JBuild4DCResponseVo.success("系统数据初始化成功！");
    }

}
