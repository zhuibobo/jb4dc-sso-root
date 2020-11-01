package com.jb4dc.sso.client.proxy;

import com.jb4dc.base.service.cache.IBuildGeneralObj;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.sso.client.cache.SSOCacheManager;
import com.jb4dc.sso.client.remote.CacheRuntimeRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeProxyBase {
    Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired(required = false)
    SSOCacheManager proxyBuilderCacheManager;

    @Autowired
    CacheRuntimeRemote cacheRuntimeRemote;

    public String builderCacheKey(Class aClass,String classInnerSingleValue,int version) {
        return aClass.getCanonicalName() +"_"+ classInnerSingleValue+"_"+version;
    }

    public <T> T autoGetFromCache(Class aClass,String classInnerSingleValue, IBuildGeneralObj<T> builder) throws JBuild4DCGenerallyException {
        int version = cacheRuntimeRemote.getPlatformGlobalCacheVersion().getData();
        String cacheKey = this.builderCacheKey(aClass, classInnerSingleValue, version);

        if (proxyBuilderCacheManager.exist(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME, cacheKey)) {
            logger.info("从缓存中获取数据"+cacheKey);
            return proxyBuilderCacheManager.getObject(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME, cacheKey);
        } else {
            Object obj = builder.BuildObj();
            if (obj == null) {
                throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_BUILDER_CODE, "不能将Null存入缓存,Key:" + cacheKey);
            }
            proxyBuilderCacheManager.put(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME, cacheKey, obj);
        }
        return proxyBuilderCacheManager.getObject(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME, cacheKey);
    }
}
