package com.jb4dc.sso.webserver.rest.sso.role;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.feb.dist.webserver.rest.base.GeneralRest;
import com.jb4dc.sso.dbentities.role.RoleEntity;
import com.jb4dc.sso.service.role.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/Rest/SSO/Ro/Role")
public class RoleRest extends GeneralRest<RoleEntity> {
    @Autowired
    IRoleService roleService;

    @Override
    public String getModuleName() {
        return "角色";
    }

    @Override
    protected IBaseService<RoleEntity> getBaseService() {
        return roleService;
    }
}
