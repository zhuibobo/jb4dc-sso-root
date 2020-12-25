package com.jb4dc.sso.webserver.rest.sso.runtime;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.webserver.rest.sso.organ.OrganRest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/10/9
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "/Rest/SSO/Runtime/OrganRuntime")
public class OrganRuntimeRest extends OrganRest {

    @RequestMapping(value = "/GetFullEnableOrganRT", method = RequestMethod.POST)
    public JBuild4DCResponseVo<List<OrganEntity>> getFullEnableOrganRT() {
        List<OrganEntity> organEntityList=organService.getALLEnableOrgan();
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,organEntityList);
    }

    @RequestMapping(value = "/GetEnableOrganMinPropRT", method = RequestMethod.POST)
    public JBuild4DCResponseVo<List<OrganEntity>> getEnableOrganMinPropRT() {
        List<OrganEntity> organEntityList=organService.getALLEnableOrganMinProp();
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,organEntityList);
    }

    @RequestMapping(value = "/GetOrganById", method = RequestMethod.GET)
    public JBuild4DCResponseVo<OrganEntity> getOrganById(String organId) throws JBuild4DCGenerallyException {
        return JBuild4DCResponseVo.getDataSuccess(organService.getByPrimaryKey(null,organId));
    }
}
