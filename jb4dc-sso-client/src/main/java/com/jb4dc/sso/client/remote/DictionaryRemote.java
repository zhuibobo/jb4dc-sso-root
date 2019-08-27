package com.jb4dc.sso.client.remote;

import com.jb4dc.base.service.po.DictionaryPO;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/27
 * To change this template use File | Settings | File Templates.
 */

@FeignClient(name= "${jb4dc.sso.server.name}",contextId = "DictionaryRemote",configuration = { SSOClientFeignClientConfig.class },path = "${jb4dc.sso.server.context-path}/Rest/SystemSetting/Dict/Dictionary")
public interface DictionaryRemote {

    @RequestMapping(value = "/GetEnableListDataByGroupId", method = RequestMethod.POST)
    JBuild4DCResponseVo<List<DictionaryPO>> getEnableListDataByGroupId(@RequestParam("groupValue") String groupValue);
}
