package com.jb4dc.sso.client.remote;

import com.jb4dc.base.service.aspect.ClientCallRemoteCache;
import com.jb4dc.base.service.cache.JB4DCCacheManagerV2;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.user.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/10/9
 * To change this template use File | Settings | File Templates.
 */
@Primary
@FeignClient(name= "${jb4dc.sso.server.name}",contextId = "UserRuntimeRemote",path = "${jb4dc.sso.server.context-path}/Rest/SSO/Runtime/UserRuntime")
public interface UserRuntimeRemote {

    @RequestMapping(value = "/GetUserByOrganIdRT", method = RequestMethod.GET)
    @ClientCallRemoteCache
    JBuild4DCResponseVo<List<UserEntity>> getUserByOrganIdRT(@RequestParam("organId") String organId)  throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/GetEnableUserMinPropRT", method = RequestMethod.GET)
    @ClientCallRemoteCache
    JBuild4DCResponseVo<List<UserEntity>> getEnableUserMinPropRT()  throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/GetUserByAccountName", method = RequestMethod.GET)
    @ClientCallRemoteCache
    JBuild4DCResponseVo<UserEntity> getUserByAccountName(@RequestParam("accountName") String accountName)  throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/GetUserById", method = RequestMethod.GET)
    @ClientCallRemoteCache(expirationTimeSeconds = JB4DCCacheManagerV2.ExpirationTime_20Minutes)
    JBuild4DCResponseVo<UserEntity> getUserById(@RequestParam("userId") String userId)  throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/SearchUserByUserIdList", method = RequestMethod.POST)
    @ClientCallRemoteCache
    JBuild4DCResponseVo<List<UserEntity>> searchUserByUserIdList(@RequestParam("searchUserIdList") String searchUserIdList)  throws JBuild4DCGenerallyException;

    @RequestMapping(value = "/GetUserByRoleId", method = RequestMethod.GET)
    @ClientCallRemoteCache
    JBuild4DCResponseVo<List<UserEntity>> getUserByRoleId(@RequestParam("roleId") String roleId)  throws JBuild4DCGenerallyException;


}
