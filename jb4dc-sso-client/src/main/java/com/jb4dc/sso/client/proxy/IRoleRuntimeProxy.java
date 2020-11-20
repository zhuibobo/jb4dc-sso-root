package com.jb4dc.sso.client.proxy;

import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.role.RoleEntity;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
public interface IRoleRuntimeProxy {
    JBuild4DCResponseVo<List<RoleEntity>> getFullEnableRoleRT() throws JBuild4DCGenerallyException, IOException;
}
