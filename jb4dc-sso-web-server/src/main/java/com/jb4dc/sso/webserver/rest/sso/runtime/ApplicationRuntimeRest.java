package com.jb4dc.sso.webserver.rest.sso.runtime;

import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.application.SsoAppEntity;
import com.jb4dc.sso.service.application.ISsoAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/Rest/SSO/Runtime/ApplicationRuntime")
public class ApplicationRuntimeRest {

    @Autowired
    ISsoAppService ssoAppService;

    @RequestMapping(value = "/GetHasAuthorityAppSSO", method = RequestMethod.GET, produces = "application/json")
    public JBuild4DCResponseVo getHasAuthorityAppSSO(String userId) throws JBuild4DCGenerallyException {
        List<SsoAppEntity> ssoAppEntityList=ssoAppService.getHasAuthorityAppSSO(userId);
        return JBuild4DCResponseVo.getDataSuccess(ssoAppEntityList);
    }
}
