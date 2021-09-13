package com.jb4dc.sso.client.remote;

import com.jb4dc.base.service.aspect.ClientCallRemoteCache;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.role.RoleGroupEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
@Primary
@FeignClient(name= "${jb4dc.sso.server.name}",contextId = "RoleGroupRuntimeRemote",path = "${jb4dc.sso.server.context-path}/Rest/SSO/Ro/RoleGroup")
public interface RoleGroupRuntimeRemote {

    @RequestMapping(value = "GetALLRoleGroup", method = RequestMethod.POST)
    @ClientCallRemoteCache
    JBuild4DCResponseVo<List<RoleGroupEntity>> getALLRoleGroup();
}
