package com.jb4dc.sso.webserver.rest.systemsetting.parassetting;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.feb.dist.webserver.rest.base.GeneralRest;
import com.jb4dc.sso.dbentities.systemsetting.SettingEntity;
import com.jb4dc.sso.service.systemsetting.ISettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/Rest/SystemSetting/Para/ParasSetting")
public class ParasSettingRest extends GeneralRest<SettingEntity> {
    @Autowired
    ISettingService settingService;

    @Override
    protected IBaseService<SettingEntity> getBaseService() {
        return settingService;
    }

}
