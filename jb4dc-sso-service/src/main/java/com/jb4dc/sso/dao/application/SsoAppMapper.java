package com.jb4dc.sso.dao.application;

import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.sso.dbentities.application.SsoAppEntity;

import java.util.List;

public interface SsoAppMapper extends BaseMapper<SsoAppEntity> {
    List<SsoAppEntity> selectALLSubApp(String appId);

    List<SsoAppEntity> selectAllMainApp();

    List<SsoAppEntity> selectHasAuthorityAppSSO(String userId);
}
