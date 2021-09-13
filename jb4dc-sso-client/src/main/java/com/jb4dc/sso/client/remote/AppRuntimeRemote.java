package com.jb4dc.sso.client.remote;

import com.jb4dc.base.service.aspect.ClientCallRemoteCache;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.service.po.SsoAppPO;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.application.SsoAppEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/12
 * To change this template use File | Settings | File Templates.
 */
@Primary
@FeignClient(name= "${jb4dc.sso.server.name}",contextId = "AppRuntimeRemote",path = "${jb4dc.sso.server.context-path}/Rest/SSO/App/Application")
public interface AppRuntimeRemote {
    @RequestMapping(value = "/GetHasAuthorityAppSSO", method = RequestMethod.GET, produces = "application/json")
    @ClientCallRemoteCache
    JBuild4DCResponseVo<List<SsoAppPO>> getHasAuthorityAppSSO(@RequestParam("userId") String userId) throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/GetAppSSO", method = RequestMethod.GET, produces = "application/json")
    @ClientCallRemoteCache
    JBuild4DCResponseVo<SsoAppPO> getAppSSO(@RequestParam("appId") String appId) throws JBuild4DCGenerallyException;
}
