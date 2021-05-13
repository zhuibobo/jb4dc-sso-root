package com.jb4dc.sso.webserver.rest.sso.runtime;

import com.github.pagehelper.PageInfo;
import com.jb4dc.base.service.aspect.ClientCallRemoteCache;
import com.jb4dc.base.service.general.JB4DCSessionCenter;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.service.search.GeneralSearchUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.user.UserEntity;
import com.jb4dc.sso.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @RequestMapping(value = "/GetUserByOrganIdRT", method = RequestMethod.GET)
    public JBuild4DCResponseVo<List<UserEntity>> getUserByOrganIdRT(String organId) {
        JB4DCSession jb4DCSession= JB4DCSessionUtility.getSession();
        List<UserEntity> userEntityList=userService.getByOrganId(jb4DCSession,organId);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,userEntityList);
    }

    @RequestMapping(value = "/SearchUserByUserIdList", method = RequestMethod.POST)
    public JBuild4DCResponseVo<List<UserEntity>> searchUserByUserIdList(String searchUserIdList) {
        JB4DCSession jb4DCSession= JB4DCSessionUtility.getSession();
        List<String> userIdList=Arrays.stream(searchUserIdList.split(",")).collect(Collectors.toList());
        List<UserEntity> userEntityList=userService.getByUserIdList(jb4DCSession,userIdList);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,userEntityList);
    }

    @RequestMapping(value = "/GetUserByOrganSearch", method = RequestMethod.GET)
    public JBuild4DCResponseVo<List<UserEntity>> getUserByOrganSearch(Integer pageSize,Integer pageNum,String searchCondition) throws IOException, ParseException {
        JB4DCSession jb4DCSession= JB4DCSessionUtility.getSession();
        Map<String,Object> searchMap= GeneralSearchUtility.deserializationToMap(searchCondition);
        //String departmentId="";
        if(searchMap.get("searchInALL").toString().equals("是")){
            //searchMap.remove("departmentId");
        }
        PageInfo<List<Map<String,Object>>> pageInfo=userService.getUserByOrganSearch(jb4DCSession,pageNum,pageSize,searchMap);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,pageInfo);
        //return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,userEntityList);
    }

    @RequestMapping(value = "/GetEnableUserMinPropRT", method = RequestMethod.GET)
    public JBuild4DCResponseVo<List<UserEntity>> getEnableUserMinPropRT() {
        JB4DCSession jb4DCSession= JB4DCSessionUtility.getSession();
        List<UserEntity> userEntityList=userService.getALLEnableUserMinProp(jb4DCSession);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,userEntityList);
    }

    @RequestMapping(value = "/GetUserByAccountName", method = RequestMethod.GET)
    public JBuild4DCResponseVo<UserEntity> getUserByAccountName(String accountName) {
        JB4DCSession jb4DCSession= JB4DCSessionUtility.getSession();
        UserEntity userEntity=userService.getByAccount(jb4DCSession,accountName);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,userEntity);
    }

    @RequestMapping(value = "/GetUserByRoleId", method = RequestMethod.GET)
    public  JBuild4DCResponseVo<List<UserEntity>> getUserByRoleId(String roleId)  throws JBuild4DCGenerallyException {
        List<UserEntity> userEntityList=userService.getUserByRoleId(JB4DCSessionUtility.getSession(),roleId);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,userEntityList);
    }

    @RequestMapping(value = "/GetUserById", method = RequestMethod.GET)
    public JBuild4DCResponseVo<UserEntity> getUserById(String userId) throws JBuild4DCGenerallyException {
        UserEntity userEntity=userService.getByPrimaryKey(JB4DCSessionUtility.getSession(),userId);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,userEntity);
    }
}
