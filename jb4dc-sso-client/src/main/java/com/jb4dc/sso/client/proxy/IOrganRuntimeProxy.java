package com.jb4dc.sso.client.proxy;

import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.organ.OrganEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
public interface IOrganRuntimeProxy {
    JBuild4DCResponseVo<List<OrganEntity>> getFullEnableOrganRT() throws JBuild4DCGenerallyException;

    JBuild4DCResponseVo<List<OrganEntity>> getEnableOrganMinPropRT() throws JBuild4DCGenerallyException;
}
