package com.jb4dc.sso.client.proxy.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jb4dc.base.tools.JsonUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.cache.SSOCacheManager;
import com.jb4dc.sso.client.proxy.IOrganRuntimeProxy;
import com.jb4dc.sso.client.proxy.RuntimeProxyBase;
import com.jb4dc.sso.client.remote._OrganRuntimeRemote;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2020/11/1
 * To change this template use File | Settings | File Templates.
 */
@Service
public class OrganRuntimeProxyImpl extends RuntimeProxyBase implements IOrganRuntimeProxy {

    @Autowired
    _OrganRuntimeRemote organRuntimeRemote;

    @Override
    public JBuild4DCResponseVo<List<OrganEntity>> getFullEnableOrganRT() throws JBuild4DCGenerallyException {
        String methodName = "getFullEnableOrganRT";
        JBuild4DCResponseVo<List<OrganEntity>> jBuild4DCResponseVo =autoGetFromCache(this.getClass(), methodName, () -> organRuntimeRemote.getFullEnableOrganRT());
        return jBuild4DCResponseVo;
    }

    @Override
    public JBuild4DCResponseVo<List<OrganEntity>> getEnableOrganMinPropRT() throws JBuild4DCGenerallyException {
        String methodName = "getEnableOrganMinPropRT";
        JBuild4DCResponseVo<List<OrganEntity>> jBuild4DCResponseVo =autoGetFromCache(this.getClass(), methodName, () -> organRuntimeRemote.getEnableOrganMinPropRT());
        return jBuild4DCResponseVo;
    }

    @Override
    public Map<String,Map<String,String>> getEnableOrganMinMapJsonPropRT() throws JBuild4DCGenerallyException {
        String cacheKey =builderCacheKey(this.getClass(),"getEnableOrganMinMapPropRT",0);
        if(proxyBuilderCacheManager.exist(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME, cacheKey)){
            String cacheValue = proxyBuilderCacheManager.getString(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME,cacheKey);
            try {
                return JsonUtility.toObject(cacheValue,Map.class);
            } catch (IOException e) {
                throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_BUILDER_CODE,e);
            }
        }
        else{
            JBuild4DCResponseVo<List<OrganEntity>> jBuild4DCResponseVo =organRuntimeRemote.getEnableOrganMinPropRT();

            if(jBuild4DCResponseVo.getData()==null){
                throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_BUILDER_CODE,"获取不到组织数据!");
            }

            Map<String,Map<String,String>> organMinMap=new HashMap<>();
            for (OrganEntity organ : jBuild4DCResponseVo.getData()) {
                Map<String,String> mapOrgan=new HashMap<>();
                mapOrgan.put("organId",organ.getOrganId());
                mapOrgan.put("organName",organ.getOrganName());
                organMinMap.put(organ.getOrganId(),mapOrgan);
            }

            try {
                String json = JsonUtility.toObjectString(organMinMap);
                proxyBuilderCacheManager.put(SSOCacheManager.SSO_CLIENT_PROXY_CACHE_NAME,cacheKey,json);
            } catch (JsonProcessingException e) {
                throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_BUILDER_CODE,e);
            }


            return organMinMap;
        }
    }
}
