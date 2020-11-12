package com.jb4dc.sso.client.proxy.impl;

import com.jb4dc.base.service.po.SsoAppPO;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.proxy.IAppRuntimeProxy;
import com.jb4dc.sso.client.proxy.RuntimeProxyBase;
import com.jb4dc.sso.client.remote._AppRuntimeRemote;
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
public class AppRuntimeProxy extends RuntimeProxyBase implements IAppRuntimeProxy {

    @Autowired
    _AppRuntimeRemote appRuntimeRemote;

    @Override
    public JBuild4DCResponseVo<List<SsoAppPO>> getHasAuthorityAppSSO(String userId) throws JBuild4DCGenerallyException{
        return appRuntimeRemote.getHasAuthorityAppSSO(userId);
    }
}
