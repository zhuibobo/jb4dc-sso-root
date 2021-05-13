package com.jb4dc.sso.webserver.rest.sso.role;

import com.github.pagehelper.PageInfo;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.user.UserEntity;
import com.jb4dc.sso.service.user.IUserRoleService;
import com.jb4dc.sso.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/Rest/SSO/Ro/UserRoleBind")
public class UserRoleBindRest {

    @Autowired
    IUserRoleService userRoleService;

    @Autowired
    IUserService userService;

    @RequestMapping(value = "BindUsersWithRole", method = RequestMethod.POST)
    public JBuild4DCResponseVo bindUsersWithRole(String roleId,@RequestParam("userIds[]") List<String> userIds) throws JBuild4DCGenerallyException {
        userRoleService.bindUsersWithRole(JB4DCSessionUtility.getSession(),roleId,userIds);
        return JBuild4DCResponseVo.opSuccess();
    }

    @RequestMapping(value = "GetBindRoleUsers", method = RequestMethod.POST)
    public JBuild4DCResponseVo getBindRoleUsers(String roleId,Integer pageSize,Integer pageNum) throws JBuild4DCGenerallyException {
        PageInfo<UserEntity> pageInfo = userService.getBindRoleUsers(JB4DCSessionUtility.getSession(),roleId,pageNum,pageSize);
        return JBuild4DCResponseVo.getDataSuccess(pageInfo);
    }

    @RequestMapping(value = "DeleteUserRoleBind", method = RequestMethod.DELETE)
    public JBuild4DCResponseVo deleteUserRoleBind(String roleId,String userId) throws JBuild4DCGenerallyException {
        userRoleService.deleteUserRoleBind(roleId,userId);
        return JBuild4DCResponseVo.opSuccess();
    }

    @RequestMapping(value = "ClearAllRoleMember", method = RequestMethod.DELETE)
    public JBuild4DCResponseVo clearAllRoleMember(String roleId) throws JBuild4DCGenerallyException {
        userRoleService.clearAllRoleMember(roleId);
        return JBuild4DCResponseVo.opSuccess();
    }
}
