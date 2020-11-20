package com.jb4dc.sso.client.proxy.impl;

import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.proxy.IOrganRuntimeProxy;
import com.jb4dc.sso.client.proxy.IRoleGroupRuntimeProxy;
import com.jb4dc.sso.client.proxy.RuntimeProxyBase;
import com.jb4dc.sso.client.remote._OrganRuntimeRemote;
import com.jb4dc.sso.client.remote._RoleGroupRuntimeRemote;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.role.RoleGroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RoleGroupRuntimeProxyImpl extends RuntimeProxyBase implements IRoleGroupRuntimeProxy {

    @Autowired
    _RoleGroupRuntimeRemote roleGroupRuntimeRemote;

    @Override
    public JBuild4DCResponseVo<List<RoleGroupEntity>> getALLRoleGroup() throws JBuild4DCGenerallyException, IOException {
        String methodName = "getALLRoleGroup";
        JBuild4DCResponseVo<List<RoleGroupEntity>> jBuild4DCResponseVo =autoGetFromCache(this.getClass(), methodName, () -> roleGroupRuntimeRemote.getALLRoleGroup());
        return jBuild4DCResponseVo;
    }
}
