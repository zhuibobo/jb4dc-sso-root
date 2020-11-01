package com.jb4dc.sso.webserver.rest.systemsetting.runtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.service.po.OperationLogPO;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.service.systemsetting.ICacheVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "/Rest/SystemSetting/Runtime/CacheRuntime")
public class CacheRuntimeRest {

    @Autowired
    ICacheVersionService cacheVersionService;

    @RequestMapping("/GetPlatformGlobalCacheVersion")
    public JBuild4DCResponseVo<Integer> getPlatformGlobalCacheVersion() throws JBuild4DCGenerallyException {
        return JBuild4DCResponseVo.getDataSuccess(cacheVersionService.getPlatformGlobalCacheVersion());
    }
}
