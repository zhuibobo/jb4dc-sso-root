package com.jb4dc.sso.client.proxy.impl;

import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.proxy.IOrganRuntimeProxy;
import com.jb4dc.sso.client.proxy.IUserRuntimeProxy;
import com.jb4dc.sso.client.proxy.RuntimeProxyBase;
import com.jb4dc.sso.client.remote._UserRuntimeRemote;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserRuntimeProxyImpl  extends RuntimeProxyBase implements IUserRuntimeProxy {

    @Autowired
    _UserRuntimeRemote userRuntimeRemote;

    @Override
    public JBuild4DCResponseVo<List<UserEntity>> getUserByOrganIdRT(String organId) throws JBuild4DCGenerallyException, IOException {
        String methodName = "getFullEnableOrganRT"+organId;
        JBuild4DCResponseVo<List<UserEntity>> jBuild4DCResponseVo =autoGetFromCache(this.getClass(), methodName, () -> userRuntimeRemote.getUserByOrganIdRT(organId));
        return jBuild4DCResponseVo;
    }

    @Override
    public JBuild4DCResponseVo<List<UserEntity>> getEnableUserMinPropRT() throws JBuild4DCGenerallyException {
        String methodName = "getEnableUserMinPropRT";
        JBuild4DCResponseVo<List<UserEntity>> jBuild4DCResponseVo =autoGetFromCache(this.getClass(), methodName, () -> userRuntimeRemote.getEnableUserMinPropRT());
        return jBuild4DCResponseVo;
    }
}
