package com.jb4dc.sso.client.remote;

import com.jb4dc.base.service.po.MenuPO;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/26
 * To change this template use File | Settings | File Templates.
 */
@Primary
@FeignClient(name= "${jb4dc.sso.server.name}",contextId = "MenuRemote",path = "${jb4dc.sso.server.context-path}/Rest/SSO/Mu/Menu")
public interface MenuRemote {

    @RequestMapping(value = "/GetMyAuthMenusBySystemId", method = RequestMethod.POST)
    JBuild4DCResponseVo<List<MenuPO>> getMyAuthMenusBySystemId(@RequestParam("systemId") String systemId);
}
