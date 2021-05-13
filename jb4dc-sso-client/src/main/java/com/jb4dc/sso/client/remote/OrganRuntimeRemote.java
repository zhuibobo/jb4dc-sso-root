package com.jb4dc.sso.client.remote;

import com.jb4dc.base.service.aspect.ClientCallRemoteCache;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/10/9
 * To change this template use File | Settings | File Templates.
 */
@Primary
@FeignClient(name= "${jb4dc.sso.server.name}",contextId = "OrganRuntimeRemote",path = "${jb4dc.sso.server.context-path}/Rest/SSO/Runtime/OrganRuntime")
public interface OrganRuntimeRemote {

    @RequestMapping(value = "/GetFullEnableOrganRT", method = RequestMethod.POST)
    @ClientCallRemoteCache
    JBuild4DCResponseVo<List<OrganEntity>> getFullEnableOrganRT() throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/GetEnableOrganMinPropRT", method = RequestMethod.POST)
    @ClientCallRemoteCache
    JBuild4DCResponseVo<List<OrganEntity>> getEnableOrganMinPropRT() throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/GetEnableChildOrganRT", method = RequestMethod.GET)
    @ClientCallRemoteCache
    JBuild4DCResponseVo<List<OrganEntity>> getEnableChildOrganRT(@RequestParam("organId") String organId) throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/GetOrganById", method = RequestMethod.GET)
    @ClientCallRemoteCache
    JBuild4DCResponseVo<OrganEntity> getOrganById(@RequestParam("organId") String organId) throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/GetAllChildOrganIdIncludeSelfRT", method = RequestMethod.GET)
    @ClientCallRemoteCache
    JBuild4DCResponseVo<List<String>> getAllChildOrganIdIncludeSelfRT(@RequestParam("organId") String organId) throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/GetEnableOrganMinMapJsonPropRT", method = RequestMethod.GET)
    @ClientCallRemoteCache
    JBuild4DCResponseVo<Map<String, Map<String,String>>> getEnableOrganMinMapJsonPropRT() throws JBuild4DCGenerallyException;
}

