package com.jb4dc.sso.webserver.remote.builder;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/10/20
 * To change this template use File | Settings | File Templates.
 */
@Primary
@FeignClient(name= "${jb4dc.builder.server.name}",contextId = "DBLinkRuntimeRemote",path = "${jb4dc.builder.server.context-path}/Rest/Builder/DataStorage/DBLink")
public interface DBLinkRemote {

    @RequestMapping(value = "/GetFullDBLink", method = RequestMethod.POST)
    JBuild4DCResponseVo getFullDBLink();
}
