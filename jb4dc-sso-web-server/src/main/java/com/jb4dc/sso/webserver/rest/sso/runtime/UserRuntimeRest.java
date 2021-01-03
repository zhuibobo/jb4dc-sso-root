package com.jb4dc.sso.webserver.rest.sso.runtime;

import com.github.pagehelper.PageInfo;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.service.search.GeneralSearchUtility;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.user.UserEntity;
import com.jb4dc.sso.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
    public JBuild4DCResponseVo<List<UserEntity>> getUserByOrganIdRT(String organId) {
        List<UserEntity> userEntityList=userService.getByOrganId(organId);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,userEntityList);
    }

    @RequestMapping(value = "/GetUserByOrganSearch", method = RequestMethod.POST)
    public JBuild4DCResponseVo<List<UserEntity>> getUserByOrganSearch(Integer pageSize,Integer pageNum,String searchCondition) throws IOException, ParseException {
        JB4DCSession jb4DSession= JB4DCSessionUtility.getSession();
        Map<String,Object> searchMap= GeneralSearchUtility.deserializationToMap(searchCondition);
        //String departmentId="";
        if(searchMap.get("searchInALL").toString().equals("是")){
            //searchMap.remove("departmentId");
        }
        PageInfo<List<Map<String,Object>>> pageInfo=userService.getUserByOrganSearch(jb4DSession,pageNum,pageSize,searchMap);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,pageInfo);
        //return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,userEntityList);
    }

    @RequestMapping(value = "/GetEnableUserMinPropRT", method = RequestMethod.POST)
    public JBuild4DCResponseVo<List<UserEntity>> getEnableUserMinPropRT() {
        List<UserEntity> userEntityList=userService.getALLEnableUserMinProp();
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,userEntityList);
    }

    @RequestMapping(value = "/GetUserByAccountName", method = RequestMethod.GET)
    public JBuild4DCResponseVo<UserEntity> getUserByAccountName(String accountName) {
        UserEntity userEntity=userService.getByAccount(accountName);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,userEntity);
    }
}
