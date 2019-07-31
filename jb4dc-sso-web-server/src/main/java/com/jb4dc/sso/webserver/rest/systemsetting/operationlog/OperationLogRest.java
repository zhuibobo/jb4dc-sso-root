package com.jb4dc.sso.webserver.rest.systemsetting.operationlog;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.base.service.IOperationLogService;
import com.jb4dc.feb.dist.webserver.rest.base.GeneralRest;
import com.jb4dc.sso.dbentities.systemsetting.OperationLogEntity;
import com.jb4dc.sso.service.systemsetting.impl.OperationLogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/Rest/SystemSetting/Oper/OperationLog")
public class OperationLogRest extends GeneralRest<OperationLogEntity> {

    @Autowired
    OperationLogServiceImpl operationLogService;

    @Override
    public String getModuleName() {
        return "操作日志";
    }

    @Override
    protected IBaseService<OperationLogEntity> getBaseService() {
        return operationLogService;
    }
}
