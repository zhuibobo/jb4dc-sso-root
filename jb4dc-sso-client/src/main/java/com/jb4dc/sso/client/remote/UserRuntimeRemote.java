package com.jb4dc.sso.client.remote;

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

    @RequestMapping(value = "/GetUserByOrganIdRT", method = RequestMethod.POST)
    JBuild4DCResponseVo<List<UserEntity>> getUserByOrganIdRT(@RequestParam("organId") String organId);
}
