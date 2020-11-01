package com.jb4dc.sso.service.systemsetting.impl;

import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dao.systemsetting.CacheVersionMapper;
import com.jb4dc.sso.dbentities.systemsetting.CacheVersionEntity;
import com.jb4dc.sso.service.systemsetting.ICacheVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CacheVersionServiceImpl extends BaseServiceImpl<CacheVersionEntity> implements ICacheVersionService
{
    CacheVersionMapper cacheVersionMapper;

    @Autowired
    public CacheVersionServiceImpl(CacheVersionMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        cacheVersionMapper=_defaultBaseMapper;
    }

    private static String PLATFORM_GLOBAL_KEY ="JB4DC_PLATFORM_GLOBAL_CACHE";

    @Override
    public int saveSimple(JB4DCSession jb4DCSession, String id, CacheVersionEntity record) throws JBuild4DCGenerallyException {
        return super.save(jb4DCSession,id, record, new IAddBefore<CacheVersionEntity>() {
            @Override
            public CacheVersionEntity run(JB4DCSession jb4DCSession,CacheVersionEntity sourceEntity) throws JBuild4DCGenerallyException {
                //设置排序,以及其他参数--nextOrderNum()
                return sourceEntity;
            }
        });
    }

    private static boolean isTestGlobalCache=false;
    private void createPlatformGlobalCache() throws JBuild4DCGenerallyException {
        if(!this.isTestGlobalCache){
            CacheVersionEntity cacheVersionEntity=cacheVersionMapper.selectByPrimaryKey(PLATFORM_GLOBAL_KEY);
            if(cacheVersionEntity==null){
                cacheVersionEntity=new CacheVersionEntity();
                cacheVersionEntity.setCacheId(PLATFORM_GLOBAL_KEY);
                cacheVersionEntity.setCacheName("通用平台数据全局缓存版本号");
                cacheVersionEntity.setCacheVersion(1000);
                this.saveSimple(null, PLATFORM_GLOBAL_KEY,cacheVersionEntity);
            }
        }
        this.isTestGlobalCache=true;
    }

    @Override
    public int getPlatformGlobalCacheVersion() throws JBuild4DCGenerallyException {
        this.createPlatformGlobalCache();
        CacheVersionEntity cacheVersionEntity=cacheVersionMapper.selectByPrimaryKey(PLATFORM_GLOBAL_KEY);
        return cacheVersionEntity.getCacheVersion();
    }

    @Override
    public void updatePlatformGlobalCacheVersion() throws JBuild4DCGenerallyException {
        this.createPlatformGlobalCache();
        //int nextVersion=cacheVersionMapper.selectByPrimaryKey(GLOBAL_KEY).getCacheVersion()+1;
        CacheVersionEntity cacheVersionEntity=cacheVersionMapper.selectByPrimaryKey(PLATFORM_GLOBAL_KEY);
        cacheVersionEntity.setCacheVersion(cacheVersionEntity.getCacheVersion()+1);
        cacheVersionMapper.updateByPrimaryKey(cacheVersionEntity);
    }
}