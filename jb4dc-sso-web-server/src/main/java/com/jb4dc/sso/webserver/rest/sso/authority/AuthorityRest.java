package com.jb4dc.sso.webserver.rest.sso.authority;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.tools.JsonUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.authority.AuthorityEntity;
import com.jb4dc.sso.service.authority.IAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/23
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "/Rest/SSO/Auth/Authority")
public class AuthorityRest {

    @Autowired
    IAuthorityService authorityService;

    @RequestMapping(value = "/SaveOwnerAuth", method = RequestMethod.POST)
    public JBuild4DCResponseVo saveOwnerAuth(String authOwnerType,String authOwnerId, String saveAuthEntitiesJsonString) throws JBuild4DCGenerallyException, IOException {
        //List<DepartmentEntity> departmentEntityList=departmentService.getDepartmentsByOrganId(organId);
        List<AuthorityEntity> authorityEntities= JsonUtility.toObjectList(saveAuthEntitiesJsonString,AuthorityEntity.class);
        authorityService.saveOwnerAuth(JB4DCSessionUtility.getSession(), authOwnerType, authOwnerId,authorityEntities);
        return JBuild4DCResponseVo.opSuccess();
    }
}
