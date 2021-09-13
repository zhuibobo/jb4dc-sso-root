/*
package com.jb4dc.sso.webserver.rest.sso.runtime;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.role.RoleEntity;
import com.jb4dc.sso.service.role.IRoleService;
import com.jb4dc.sso.webserver.rest.sso.role.RoleRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

*/
/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/10/31
 * To change this template use File | Settings | File Templates.
 *//*

@RestController
@RequestMapping(value = "/Rest/SSO/Runtime/RoleRuntime")
public class RoleRuntimeRest extends RoleRest {

    @Autowired
    IRoleService roleService;

    @RequestMapping(value = "GetFullEnableRole", method = RequestMethod.GET)
    public JBuild4DCResponseVo getFullEnableRole() throws JBuild4DCGenerallyException {
        List<RoleEntity> roleEntities = roleService.getALL(JB4DCSessionUtility.getSession());
        return JBuild4DCResponseVo.getDataSuccess(roleEntities);
    }

    @RequestMapping(value = "GetUserRoles", method = RequestMethod.GET)
    public JBuild4DCResponseVo<List<RoleEntity>> getUserRoles(String userId){
        List<RoleEntity> userRoleList=roleService.getUserRoleList(JB4DCSessionUtility.getSession(),userId);
        return JBuild4DCResponseVo.getDataSuccess(userRoleList);
    }

    @RequestMapping(value = "GetRoleById", method = RequestMethod.GET)
    public JBuild4DCResponseVo<RoleEntity> getRoleById(String roleId) throws JBuild4DCGenerallyException {
        RoleEntity roleEntity = roleService.getByPrimaryKey(JB4DCSessionUtility.getSession(), roleId);
        return JBuild4DCResponseVo.getDataSuccess(roleEntity);
    }
}
*/
