package com.jb4dc.sso.client.proxy.impl;

import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.proxy.IRoleGroupRuntimeProxy;
import com.jb4dc.sso.client.proxy.IRoleRuntimeProxy;
import com.jb4dc.sso.client.proxy.RuntimeProxyBase;
import com.jb4dc.sso.client.remote._RoleRuntimeRemote;
import com.jb4dc.sso.dbentities.role.RoleEntity;
import com.jb4dc.sso.dbentities.role.RoleGroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RoleRuntimeProxyImpl extends RuntimeProxyBase implements IRoleRuntimeProxy {

    @Autowired
    _RoleRuntimeRemote roleRuntimeRemote;

    @Override
    public JBuild4DCResponseVo<List<RoleEntity>> getFullEnableRoleRT() throws JBuild4DCGenerallyException {
        String methodName = "getFullEnableRoleRT";
        JBuild4DCResponseVo<List<RoleEntity>> jBuild4DCResponseVo =autoGetFromCache(this.getClass(), methodName, () -> roleRuntimeRemote.getFullEnableRoleRT());
        return jBuild4DCResponseVo;
    }
}
