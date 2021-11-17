package com.jb4dc.sso.webserver.remote.portlet;

import com.jb4dc.base.service.po.ZTreeNodePO;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Primary
@FeignClient(name= "${jb4dc.builder.server.name}",contextId = "TemplatePageRemote",path = "${jb4dc.builder.server.context-path}/Rest/Portlet/TemplatePage")
public interface TemplatePageRemote {
    @RequestMapping(value = "/GetTreeData", method =  RequestMethod.GET)
    JBuild4DCResponseVo<List<ZTreeNodePO>> getTreeData();
}
