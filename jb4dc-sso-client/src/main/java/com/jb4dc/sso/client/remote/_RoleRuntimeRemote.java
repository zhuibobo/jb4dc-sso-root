package com.jb4dc.sso.client.remote;

import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.role.RoleEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
@Primary
@FeignClient(name= "${jb4dc.sso.server.name}",contextId = "RoleRuntimeRemote",path = "${jb4dc.sso.server.context-path}/Rest/SSO/Runtime/RoleRuntime")
public interface _RoleRuntimeRemote {

    @RequestMapping(value = "GetFullEnableRoleRT", method = RequestMethod.GET)
    public JBuild4DCResponseVo<List<RoleEntity>> getFullEnableRoleRT() throws JBuild4DCGenerallyException;

    @RequestMapping(value = "GetUserRolesRT", method = RequestMethod.GET)
    public JBuild4DCResponseVo<List<RoleEntity>> getUserRolesRT(@RequestParam("userId") String userId) throws JBuild4DCGenerallyException;
}
