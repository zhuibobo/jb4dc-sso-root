package com.jb4dc.sso.webserver.rest.client;

import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.proxy.IOrganRuntimeProxy;
import com.jb4dc.sso.webserver.RestTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
public class OrganRuntimeProxyImplTest  extends RestTestBase {

    @Autowired
    IOrganRuntimeProxy organRuntimeProxy;

    @Test
    public void addOrganAndDepartmentAndUserTest() throws Exception {
        organRuntimeProxy.getFullEnableOrganRT();
        organRuntimeProxy.getFullEnableOrganRT();
        organRuntimeProxy.getFullEnableOrganRT();
    }
}