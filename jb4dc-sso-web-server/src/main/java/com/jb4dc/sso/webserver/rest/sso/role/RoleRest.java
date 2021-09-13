package com.jb4dc.sso.webserver.rest.sso.role;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.feb.dist.webserver.rest.base.GeneralRest;
import com.jb4dc.sso.client.remote.RoleRuntimeRemote;
import com.jb4dc.sso.dbentities.role.RoleEntity;
import com.jb4dc.sso.service.role.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/Rest/SSO/Ro/Role")
public class RoleRest extends GeneralRest<RoleEntity> implements RoleRuntimeRemote {
    @Autowired
    IRoleService roleService;

    @Override
    public String getModuleName() {
        return "单点登录系统-角色";
    }

    @Override
    protected IBaseService<RoleEntity> getBaseService() {
        return roleService;
    }

    @Override
    public JBuild4DCResponseVo getFullEnableRole() throws JBuild4DCGenerallyException {
        List<RoleEntity> roleEntities = roleService.getALL(JB4DCSessionUtility.getSession());
        return JBuild4DCResponseVo.getDataSuccess(roleEntities);
    }

    @Override
    public JBuild4DCResponseVo<List<RoleEntity>> getUserRoles(String userId){
        List<RoleEntity> userRoleList=roleService.getUserRoleList(JB4DCSessionUtility.getSession(),userId);
        return JBuild4DCResponseVo.getDataSuccess(userRoleList);
    }

    @Override
    public JBuild4DCResponseVo<RoleEntity> getRoleById(String roleId) throws JBuild4DCGenerallyException {
        RoleEntity roleEntity = roleService.getByPrimaryKey(JB4DCSessionUtility.getSession(), roleId);
        return JBuild4DCResponseVo.getDataSuccess(roleEntity);
    }
}
