package com.jb4dc.sso.webserver.remote.flow;

import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Primary
@FeignClient(name= "${jb4dc.builder.server.name}",contextId = "ModeGroupRemote",path = "${jb4dc.builder.server.context-path}/Rest/WorkFlow/Model/ModelGroup")
public interface ModelGroupRemote {
    @RequestMapping(value = "/GetTreeData",method = RequestMethod.GET)
    JBuild4DCResponseVo getTreeData();
}
