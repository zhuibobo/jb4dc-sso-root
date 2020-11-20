package com.jb4dc.sso.client.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jb4dc.base.service.cache.IBuildGeneralObj;
import com.jb4dc.base.tools.JsonUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.cache.SSOCacheManager;
import com.jb4dc.sso.client.remote.CacheRuntimeRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

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

    public <T> JBuild4DCResponseVo<List<T>> autoGetFromCache(Class aClass, String classInnerSingleValue, IBuildGeneralObj<JBuild4DCResponseVo<List<T>>> builder) throws JBuild4DCGenerallyException {
        try {
            int version = cacheRuntimeRemote.getPlatformGlobalCacheVersion().getData();
            String cacheKey = this.builderCacheKey(aClass, classInnerSingleValue, version);

            if (proxyBuilderCacheManager.exist(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME, cacheKey)) {
                logger.info("从缓存中获取数据"+cacheKey);
                String cacheValue = proxyBuilderCacheManager.getString(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME,cacheKey);
                return JsonUtility.toObject(cacheValue,JBuild4DCResponseVo.class);
            } else {
                JBuild4DCResponseVo<List<T>> obj=builder.BuildObj();
                if(obj==null){
                    throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_BUILDER_CODE, "不能将Null存入缓存,Key:"+cacheKey);
                }
                String json= null;

                    json = JsonUtility.toObjectString(obj);

                proxyBuilderCacheManager.put(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME,cacheKey,json);
                //proxyBuilderCacheManager.put(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME, cacheKey, obj);
            }
            String cacheValue=proxyBuilderCacheManager.getString(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME,cacheKey);
            return JsonUtility.toObject(cacheValue,JBuild4DCResponseVo.class);

        } catch (JsonProcessingException e) {
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_BUILDER_CODE,e);
        } catch (IOException e) {
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_BUILDER_CODE,e);
        }
    }

    /*public <T> List<T> autoGetFromCacheList(Class aClass, String classInnerSingleValue, IBuildGeneralObj<List<T>> builder, Class<T> valueType) throws JBuild4DCGenerallyException, IOException {
        int version = cacheRuntimeRemote.getPlatformGlobalCacheVersion().getData();

        String cacheKey=this.builderCacheKey(aClass,classInnerSingleValue,version);
        if(proxyBuilderCacheManager.exist(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME,cacheKey)){
            logger.info("从缓存中获取数据"+cacheKey);
            String cacheValue = proxyBuilderCacheManager.getString(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME,cacheKey);
            return JsonUtility.toObjectList(cacheValue,valueType);
        }
        else{
            logger.info("不从缓存中获取数据"+cacheKey);
            List<T> obj=builder.BuildObj();
            if(obj==null){
                throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_BUILDER_CODE, "不能将Null存入缓存,Key:"+cacheKey);
            }
            String json= JsonUtility.toObjectString(obj);
            proxyBuilderCacheManager.put(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME,cacheKey,json);
        }
        String cacheValue=proxyBuilderCacheManager.getString(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME,cacheKey);
        return JsonUtility.toObjectList(cacheValue,valueType);
    }*/
}
