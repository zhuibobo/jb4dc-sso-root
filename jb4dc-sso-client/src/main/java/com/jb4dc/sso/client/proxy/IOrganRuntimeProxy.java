package com.jb4dc.sso.client.proxy;

import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.remote._OrganRuntimeRemote;
import com.jb4dc.sso.dbentities.organ.OrganEntity;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
public interface IOrganRuntimeProxy extends _OrganRuntimeRemote {
    /*JBuild4DCResponseVo<List<OrganEntity>> getFullEnableOrganRT() throws JBuild4DCGenerallyException;

    JBuild4DCResponseVo<List<OrganEntity>> getEnableOrganMinPropRT() throws JBuild4DCGenerallyException;*/

    Map<String,Map<String,String>> getEnableOrganMinMapJsonPropRT() throws JBuild4DCGenerallyException;


}
