package com.jb4dc.sso.webserver.rest.systemsetting.runtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.service.po.OperationLogPO;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.webserver.rest.systemsetting.operationlog.OperationLogRest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/10/9
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "/Rest/SystemSetting/Runtime/OperationLogRuntime")
public class OperationLogRuntimeRest extends OperationLogRest {

    @RequestMapping("/WriteOperationLogRT")
    public JBuild4DCResponseVo writeOperationLogRT(@RequestBody OperationLogPO logPO, HttpServletRequest request) throws JsonProcessingException, JBuild4DCGenerallyException {
        operationLogService.writeOperationLog(JB4DCSessionUtility.getSession(),logPO.getLogSystemName(),logPO.getLogModuleName(),logPO.getLogActionName(),logPO.getLogType(),logPO.getLogText(),logPO.getLogData(),logPO.getLogClassName(),request);
        return JBuild4DCResponseVo.opSuccess();
    }
}
