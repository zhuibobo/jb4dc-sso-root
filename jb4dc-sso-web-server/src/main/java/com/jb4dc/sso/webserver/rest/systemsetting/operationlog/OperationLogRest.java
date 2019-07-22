package com.jb4dc.sso.webserver.rest.systemsetting.operationlog;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.feb.dist.webserver.rest.base.GeneralRest;
import com.jb4dc.sso.dbentities.systemsetting.OperationLogEntity;
import com.jb4dc.sso.service.systemsetting.IOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/Rest/SystemSetting/Oper/OperationLog")
public class OperationLogRest extends GeneralRest<OperationLogEntity> {

    @Autowired
    IOperationLogService operationLogService;

    @Override
    protected IBaseService<OperationLogEntity> getBaseService() {
        return operationLogService;
    }
}
