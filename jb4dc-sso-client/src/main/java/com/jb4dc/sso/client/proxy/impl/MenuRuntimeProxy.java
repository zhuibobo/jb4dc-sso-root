package com.jb4dc.sso.client.proxy.impl;

import com.jb4dc.base.service.po.MenuPO;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.proxy.IMenuRuntimeProxy;
import com.jb4dc.sso.client.proxy.RuntimeProxyBase;
import com.jb4dc.sso.client.remote._MenuRuntimeRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/12
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MenuRuntimeProxy extends RuntimeProxyBase implements IMenuRuntimeProxy {

    @Autowired
    _MenuRuntimeRemote menuRuntimeRemote;

    @Override
    public JBuild4DCResponseVo<List<MenuPO>> getMyAuthMenusBySystemIdRT(String userId, String systemId) throws JBuild4DCGenerallyException {
        String methodName = "getMyAuthMenusBySystemIdRT"+userId+systemId;
        JBuild4DCResponseVo<List<MenuPO>> jBuild4DCResponseVo =autoGetFromCache(this.getClass(), methodName, () -> menuRuntimeRemote.getMyAuthMenusBySystemIdRT(userId,systemId));
        return jBuild4DCResponseVo;
    }
}
