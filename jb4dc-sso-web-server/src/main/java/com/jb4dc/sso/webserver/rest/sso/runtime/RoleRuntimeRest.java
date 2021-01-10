package com.jb4dc.sso.webserver.rest.sso.runtime;

import com.github.pagehelper.PageInfo;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.role.RoleEntity;
import com.jb4dc.sso.dbentities.user.UserEntity;
import com.jb4dc.sso.service.role.IRoleService;
import com.jb4dc.sso.service.user.IUserRoleService;
import com.jb4dc.sso.webserver.rest.sso.role.RoleRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/10/31
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "/Rest/SSO/Runtime/RoleRuntime")
public class RoleRuntimeRest extends RoleRest {

    @Autowired
    IRoleService roleService;

    @RequestMapping(value = "GetFullEnableRoleRT", method = RequestMethod.GET)
    public JBuild4DCResponseVo getFullEnableRoleRT() throws JBuild4DCGenerallyException {
        List<RoleEntity> roleEntities = roleService.getALL(null);
        return JBuild4DCResponseVo.getDataSuccess(roleEntities);
    }

    @RequestMapping(value = "GetUserRolesRT", method = RequestMethod.GET)
    public JBuild4DCResponseVo<List<RoleEntity>> getUserRolesRT(String userId){
        List<RoleEntity> userRoleList=roleService.getUserRoleList(userId);
        return JBuild4DCResponseVo.getDataSuccess(userRoleList);
    }
}
