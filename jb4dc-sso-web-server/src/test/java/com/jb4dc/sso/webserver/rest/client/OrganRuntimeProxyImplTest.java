package com.jb4dc.sso.webserver.rest.client;

import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.proxy.IOrganRuntimeProxy;
import com.jb4dc.sso.client.proxy.IRoleRuntimeProxy;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.role.RoleEntity;
import com.jb4dc.sso.webserver.RestTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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

    @Autowired
    IRoleRuntimeProxy roleRuntimeProxy;

    @Test
    public void addOrganAndDepartmentAndUserTest() throws Exception {
        JBuild4DCResponseVo<List<OrganEntity>> jBuild4DCResponseVoOrganEntity=organRuntimeProxy.getFullEnableOrganRT();
        System.out.println(jBuild4DCResponseVoOrganEntity);

        JBuild4DCResponseVo<List<RoleEntity>> jBuild4DCResponseVoRoleEntity=roleRuntimeProxy.getUserRolesRT("Zhuang_Rui_Bo_UID");
        System.out.println(jBuild4DCResponseVoRoleEntity);
        //organRuntimeProxy.getFullEnableOrganRT();
        //organRuntimeProxy.getFullEnableOrganRT();
    }

    @Test
    public void getAllChildOrganIdIncludeSelfRT() throws Exception {
        JBuild4DCResponseVo<List<String>> responseVo=organRuntimeProxy.getAllChildOrganIdIncludeSelfRT("24655792-87a2-4057-887f-05db96e868be");
        System.out.println(responseVo);

        //organRuntimeProxy.getFullEnableOrganRT();
        //organRuntimeProxy.getFullEnableOrganRT();
    }
}
