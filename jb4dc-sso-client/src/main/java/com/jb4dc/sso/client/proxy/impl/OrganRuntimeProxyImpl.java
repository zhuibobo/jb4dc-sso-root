package com.jb4dc.sso.client.proxy.impl;

import com.jb4dc.base.service.cache.IBuildGeneralObj;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.remote._OrganRuntimeRemote;
import com.jb4dc.sso.client.proxy.IOrganRuntimeProxy;
import com.jb4dc.sso.client.proxy.RuntimeProxyBase;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
@Service
public class OrganRuntimeProxyImpl extends RuntimeProxyBase implements IOrganRuntimeProxy {

    @Autowired
    _OrganRuntimeRemote organRuntimeRemote;

    @Override
    public JBuild4DCResponseVo<List<OrganEntity>> getFullEnableOrganRT() throws JBuild4DCGenerallyException {
        String methodName = "getFullEnableOrganRT";
        JBuild4DCResponseVo<List<OrganEntity>> jBuild4DCResponseVo =autoGetFromCache(this.getClass(), methodName, () -> organRuntimeRemote.getFullEnableOrganRT());
        return jBuild4DCResponseVo;
    }

    @Override
    public JBuild4DCResponseVo<List<OrganEntity>> getEnableOrganMinPropRT() throws JBuild4DCGenerallyException {
        String methodName = "getEnableOrganMinPropRT";
        JBuild4DCResponseVo<List<OrganEntity>> jBuild4DCResponseVo =autoGetFromCache(this.getClass(), methodName, () -> organRuntimeRemote.getEnableOrganMinPropRT());
        return jBuild4DCResponseVo;
    }
}
