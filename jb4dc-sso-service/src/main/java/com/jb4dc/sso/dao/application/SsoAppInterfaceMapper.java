package com.jb4dc.sso.dao.application;


import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.sso.dbentities.application.SsoAppInterfaceEntity;

import java.util.List;

public interface SsoAppInterfaceMapper extends BaseMapper<SsoAppInterfaceEntity> {
    List<SsoAppInterfaceEntity> selectAppInterfaces(String appId);
}
