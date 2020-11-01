package com.jb4dc.sso.service.systemsetting;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.sso.dbentities.systemsetting.CacheVersionEntity;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
public interface ICacheVersionService extends IBaseService<CacheVersionEntity> {

    int getPlatformGlobalCacheVersion() throws JBuild4DCGenerallyException;

    void updatePlatformGlobalCacheVersion() throws JBuild4DCGenerallyException;
}