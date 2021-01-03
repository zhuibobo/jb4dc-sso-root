package com.jb4dc.sso.client.remote;

import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/10/9
 * To change this template use File | Settings | File Templates.
 */
@Primary
@FeignClient(name= "${jb4dc.sso.server.name}",contextId = "OrganRuntimeRemote",path = "${jb4dc.sso.server.context-path}/Rest/SSO/Runtime/OrganRuntime")
public interface _OrganRuntimeRemote {

    @RequestMapping(value = "/GetFullEnableOrganRT", method = RequestMethod.POST)
    JBuild4DCResponseVo<List<OrganEntity>> getFullEnableOrganRT() throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/GetEnableOrganMinPropRT", method = RequestMethod.POST)
    JBuild4DCResponseVo<List<OrganEntity>> getEnableOrganMinPropRT() throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/GetOrganById", method = RequestMethod.GET)
    JBuild4DCResponseVo<OrganEntity> getOrganById(@RequestParam("organId") String organId) throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/GetAllChildOrganIdIncludeSelfRT", method = RequestMethod.GET)
    public JBuild4DCResponseVo<List<String>> getAllChildOrganIdIncludeSelfRT(@RequestParam("organId") String organId) throws JBuild4DCGenerallyException;
}

