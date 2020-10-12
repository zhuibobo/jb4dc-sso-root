package com.jb4dc.sso.webserver.rest.sso.runtime;

import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.user.UserEntity;
import com.jb4dc.sso.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/10/9
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "/Rest/SSO/Runtime/UserRuntime")
public class UserRuntimeRest {

    @Autowired
    IUserService userService;

    @RequestMapping(value = "/GetUserByOrganIdRT", method = RequestMethod.POST)
    public JBuild4DCResponseVo getUserByOrganIdRT(String organId) {
        List<UserEntity> userEntityList=userService.getByOrganId(organId);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,userEntityList);
    }

}
