package com.jb4dc.sso.webserver.rest.sso.authority;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.tools.JsonUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.authority.AuthorityEntity;
import com.jb4dc.sso.service.authority.IAuthorityService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    public JBuild4DCResponseVo saveOwnerAuth(String authOwnerType,String authOwnerId, String saveAuthEntitiesJsonString,@RequestParam("removeAuthObjIdList[]") List<String> removeAuthObjIdList) throws JBuild4DCGenerallyException, IOException {
        //List<DepartmentEntity> departmentEntityList=departmentService.getDepartmentsByOrganId(organId);
        List<AuthorityEntity> authorityEntities= JsonUtility.toObjectList(saveAuthEntitiesJsonString,AuthorityEntity.class);
        authorityService.saveOwnerAuth(JB4DCSessionUtility.getSession(), authOwnerType, authOwnerId,authorityEntities,removeAuthObjIdList);
        return JBuild4DCResponseVo.opSuccess();
    }

    @RequestMapping(value = "/GetOwnerAuth", method = RequestMethod.GET)
    public JBuild4DCResponseVo getOwnerAuth(String authOwnerType,String authOwnerId) throws JBuild4DCGenerallyException, IOException {
        List<AuthorityEntity> authorityEntities = authorityService.getOwnerAuth(JB4DCSessionUtility.getSession(), authOwnerType, authOwnerId);
        return JBuild4DCResponseVo.getDataSuccess(authorityEntities);
    }

    @RequestMapping(value = "/GetObjAuthOwnerDesc", method = RequestMethod.GET)
    public JBuild4DCResponseVo getObjAuthOwnerDesc(String authObjId) throws JBuild4DCGenerallyException {
        List<Map<String,Object>> objAuthOwnerDesc = authorityService.getObjAuthOwnerDesc(JB4DCSessionUtility.getSession(), authObjId);
        return JBuild4DCResponseVo.getDataSuccess(objAuthOwnerDesc);
    }
}
