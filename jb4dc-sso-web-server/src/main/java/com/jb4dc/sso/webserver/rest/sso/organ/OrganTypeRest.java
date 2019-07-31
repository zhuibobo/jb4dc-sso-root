package com.jb4dc.sso.webserver.rest.sso.organ;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.feb.dist.webserver.rest.base.GeneralRest;
import com.jb4dc.sso.dbentities.organ.OrganTypeEntity;
import com.jb4dc.sso.service.organ.IOrganTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/Rest/SSO/Org/OrganType")
public class OrganTypeRest extends GeneralRest<OrganTypeEntity> {

    @Autowired
    IOrganTypeService organTypeService;

    @Override
    public String getModuleName() {
        return "组织机构类型";
    }

    @Override
    protected IBaseService<OrganTypeEntity> getBaseService() {
        return organTypeService;
    }
}
