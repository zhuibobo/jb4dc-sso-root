package com.jb4dc.sso.client.remote;

import com.jb4dc.base.service.aspect.ClientCallRemoteCache;
import com.jb4dc.base.service.po.MenuPO;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/26
 * To change this template use File | Settings | File Templates.
 */
@Primary
@FeignClient(name= "${jb4dc.sso.server.name}",contextId = "MenuRuntimeRemote",path = "${jb4dc.sso.server.context-path}/Rest/SSO/Runtime/MenuRuntime")
public interface MenuRuntimeRemote {

    @RequestMapping(value = "/GetMyAuthMenusBySystemIdRT", method = RequestMethod.POST)
    @ClientCallRemoteCache
    JBuild4DCResponseVo<List<MenuPO>> getMyAuthMenusBySystemIdRT(@RequestParam("userId") String userId,@RequestParam("systemId") String systemId);
}
