package com.jb4dc.sso.webserver.remote;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/10/20
 * To change this template use File | Settings | File Templates.
 */
@Primary
@FeignClient(name= "${jb4dc.builder.server.name}",contextId = "ModuleRuntimeRemote",path = "${jb4dc.builder.server.context-path}/Rest/Builder/RunTime/ModuleRuntime")
public interface ModuleRuntimeRemote {
    @RequestMapping(value = "/GetTreeData", method = RequestMethod.POST)
    public JBuild4DCResponseVo getTreeData(@RequestParam("dbLinkId") String dbLinkId);

    @RequestMapping(value = "/GetModuleItems", method = RequestMethod.POST)
    public JBuild4DCResponseVo<List<Map<String,Object>>> getModuleItems(@RequestParam("selectModuleId") String selectModuleId,@RequestParam("selectModuleObjectType")  String selectModuleObjectType);
}
