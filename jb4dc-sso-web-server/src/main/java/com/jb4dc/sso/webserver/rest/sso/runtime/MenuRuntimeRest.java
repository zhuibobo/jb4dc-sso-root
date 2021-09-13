/*
package com.jb4dc.sso.webserver.rest.sso.runtime;

import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.dbentities.menu.MenuEntity;
import com.jb4dc.sso.webserver.rest.sso.menu.MenuRest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

*/
/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/10/9
 * To change this template use File | Settings | File Templates.
 *//*

@RestController
@RequestMapping(value = "/Rest/SSO/Runtime/MenuRuntime")
public class MenuRuntimeRest extends MenuRest {

    @RequestMapping(value = "/GetMyAuthMenusBySystemIdRT", method = RequestMethod.POST)
    public JBuild4DCResponseVo getMyAuthMenusBySystemIdRT(String userId,String systemId) throws JBuild4DCGenerallyException {
        List<MenuEntity> menuEntities=menuService.getMyAuthMenusBySystemId(userId,systemId);
        return JBuild4DCResponseVo.getDataSuccess(menuEntities);
    }
}
*/
