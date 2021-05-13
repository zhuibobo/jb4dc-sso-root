package com.jb4dc.sso.webserver.rest.client;

import com.jb4dc.base.service.aspect.ClientCallRemoteCache;
import com.jb4dc.base.service.general.JB4DCUnitSessionSessionUtility;
import com.jb4dc.base.tools.JsonUtility;
import com.jb4dc.base.tools.RedisUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.remote.OrganRuntimeRemote;
import com.jb4dc.sso.client.remote.RoleRuntimeRemote;
import com.jb4dc.sso.client.remote.UserRuntimeRemote;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.role.RoleEntity;
import com.jb4dc.sso.dbentities.user.UserEntity;
import com.jb4dc.sso.webserver.RestTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserRuntimeRemoteTest  extends RestTestBase {

    @Autowired
    UserRuntimeRemote userRuntimeRemote;

    @Test
    public void searchUserByUserIdList() throws Exception {
        JB4DCUnitSessionSessionUtility.mockLogin(getAlex4DSession());
        JBuild4DCResponseVo<List<UserEntity>> jBuild4DCResponseVo=userRuntimeRemote.searchUserByUserIdList("834b97a7-7fd0-46ba-b87b-4113b1303c59,21ae6b28-dcd4-4f3f-b00f-c3b884f96d7e,2d6945a3-46d8-42d2-babf-6f9e402d46e3");
        System.out.println(JsonUtility.toObjectString(jBuild4DCResponseVo));
    }
}
