package com.jb4dc.sso.client.remote;

import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
@Primary
@FeignClient(name= "${jb4dc.sso.server.name}",contextId = "CacheRuntimeRemote",path = "${jb4dc.sso.server.context-path}/Rest/SystemSetting/Runtime/CacheRuntime")
public interface CacheRuntimeRemote {

    @RequestMapping("/GetPlatformGlobalCacheVersion")
    JBuild4DCResponseVo<Integer> getPlatformGlobalCacheVersion();
}
