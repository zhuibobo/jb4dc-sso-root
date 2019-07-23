package com.jb4dc.sso.webserver.rest.sso.authority;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.service.authority.IAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public JBuild4DCResponseVo saveOwnerAuth(String authOwnerType,String authOwnerId,List<String> authObjIdList,String authObjType,String cleanAboutKey){
        //List<DepartmentEntity> departmentEntityList=departmentService.getDepartmentsByOrganId(organId);
        authorityService.saveOwnerAuth(JB4DCSessionUtility.getSession(),authOwnerType,authOwnerId,authObjIdList, authObjType, cleanAboutKey);
        return JBuild4DCResponseVo.opSuccess();
    }
}
