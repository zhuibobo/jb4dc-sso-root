package com.jb4dc.sso.client.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jb4dc.base.service.po.OperationLogPO;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/31
 * To change this template use File | Settings | File Templates.
 */

@Primary
@FeignClient(name= "${jb4dc.sso.server.name}",contextId = "OperationLogRuntimeRemote",configuration = { SSOClientFeignClientConfig.class },path = "${jb4dc.sso.server.context-path}/Rest/SystemSetting/Oper/OperationLog")
public interface OperationLogRuntimeRemote {

    @RequestMapping(value = "/WriteOperationLogRT", method = RequestMethod.POST)
    JBuild4DCResponseVo writeOperationLogRT(OperationLogPO logPO) throws JsonProcessingException, JBuild4DCGenerallyException;
}
