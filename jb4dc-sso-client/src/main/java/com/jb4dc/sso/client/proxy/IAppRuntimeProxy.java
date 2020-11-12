package com.jb4dc.sso.client.proxy;

import com.jb4dc.base.service.po.SsoAppPO;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/12
 * To change this template use File | Settings | File Templates.
 */
public interface IAppRuntimeProxy {
    JBuild4DCResponseVo<List<SsoAppPO>> getHasAuthorityAppSSO(String userId) throws JBuild4DCGenerallyException;
}
