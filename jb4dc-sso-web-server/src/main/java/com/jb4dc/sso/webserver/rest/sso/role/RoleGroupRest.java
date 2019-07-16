package com.jb4dc.sso.webserver.rest.sso.role;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.feb.dist.webserver.rest.base.GeneralRest;
import com.jb4dc.sso.dbentities.role.RoleGroupEntity;
import com.jb4dc.sso.service.role.IRoleGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/Rest/SSO/Ro/RoleGroup")
public class RoleGroupRest extends GeneralRest<RoleGroupEntity> {

    @Autowired
    IRoleGroupService roleGroupService;

    @Override
    protected IBaseService<RoleGroupEntity> getBaseService() {
        return roleGroupService;
    }

    @RequestMapping(value = "GetTreeData", method = RequestMethod.POST)
    public List<RoleGroupEntity> getTreeData() {
        List<RoleGroupEntity> datasetGroupEntityList=roleGroupService.getALLOrderByAsc(JB4DCSessionUtility.getSession());
        return datasetGroupEntityList;
    }
}
