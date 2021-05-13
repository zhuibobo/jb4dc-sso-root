package com.jb4dc.sso.webserver.rest.client;

import com.jb4dc.base.service.general.JB4DCUnitSessionSessionUtility;
import com.jb4dc.base.tools.RedisUtility;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.remote.OrganRuntimeRemote;
import com.jb4dc.sso.client.remote.RoleRuntimeRemote;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.role.RoleEntity;
import com.jb4dc.sso.webserver.RestTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
public class OrganRuntimeProxyImplTest  extends RestTestBase {

    @Autowired
    OrganRuntimeRemote organRuntimeRemote;

    @Autowired
    RoleRuntimeRemote roleRuntimeRemote;

    @Autowired
    RedisUtility redisUtility;

    @Test
    public void addOrganAndDepartmentAndUserTest() throws Exception {
        JBuild4DCResponseVo<List<OrganEntity>> jBuild4DCResponseVoOrganEntity=organRuntimeRemote.getFullEnableOrganRT();
        System.out.println(jBuild4DCResponseVoOrganEntity);

        JBuild4DCResponseVo<List<RoleEntity>> jBuild4DCResponseVoRoleEntity=roleRuntimeRemote.getUserRoles("Zhuang_Rui_Bo_UID");
        System.out.println(jBuild4DCResponseVoRoleEntity);
        //organRuntimeProxy.getFullEnableOrganRT();
        //organRuntimeProxy.getFullEnableOrganRT();
    }

    @Test
    public void getAllChildOrganIdIncludeSelfRT() throws Exception {
        JB4DCUnitSessionSessionUtility.mockLogin(getAlex4DSession());
        JBuild4DCResponseVo<List<String>> responseVo=organRuntimeRemote.getAllChildOrganIdIncludeSelfRT("24655792-87a2-4057-887f-05db96e868be");
        System.out.println(responseVo);

        //organRuntimeProxy.getFullEnableOrganRT();
        //organRuntimeProxy.getFullEnableOrganRT();
    }
    @Test
    public void resd() throws Exception {
        /*JB4DCSession session= JB4DCSessionUtility.getInitSystemSession();
        String sessionId=JB4DCSessionUtility.();
        JB4DCSessionUtility.setSession(sessionId,session);
        JB4DCSession session1=JB4DCSessionUtility.getSession(sessionId);
        System.out.println(session1);*/
        //redisUtility.setString("1","123");
        //stringRedisTemplate.opsForValue().set("1","1");
    }

}
