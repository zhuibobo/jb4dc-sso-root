package com.jb4dc.sso.client.remote;

import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/24
 * To change this template use File | Settings | File Templates.
 */
@Primary
@FeignClient(name= "${jb4dc.sso.server.name}",contextId = "SessionRuntimeRemote",configuration = { SSOClientFeignClientConfig.class },path = "${jb4dc.sso.server.context-path}/Rest/SSO/Session")
public interface SessionRuntimeRemote {

    @RequestMapping(value = "/GetSession",method = RequestMethod.POST)
    JBuild4DCResponseVo getSession(@RequestParam(value = "JBuild4DCSSOToken") String JBuild4DCSSOToken);

    /**
     *
     * @Author zhuangrb
     * @Date 2019/7/24 22:56
     * @param token
     * @param account
     * @param password
     * @return com.jb4dc.core.base.vo.JBuild4DCResponseVo
     **/
    @RequestMapping(value = "/LoginForRest", method = RequestMethod.POST)
    JBuild4DCResponseVo<JB4DCSession> loginForRest(@RequestHeader("X-Auth-Token") String token, @RequestParam(value = "account") String account, @RequestParam(value = "password") String password);
}
