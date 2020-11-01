package com.jb4dc.sso.dbentities.systemsetting;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :tsys_cache_version
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class CacheVersionEntity {
    //CACHE_ID:
    @DBKeyField
    private String cacheId;

    //CACHE_NAME:缓存名称
    private String cacheName;

    //CACHE_VERSION:缓存版本号
    private Integer cacheVersion;

    /**
     * 构造函数
     * @param cacheId
     * @param cacheName 缓存名称
     * @param cacheVersion 缓存版本号
     **/
    public CacheVersionEntity(String cacheId, String cacheName, Integer cacheVersion) {
        this.cacheId = cacheId;
        this.cacheName = cacheName;
        this.cacheVersion = cacheVersion;
    }

    public CacheVersionEntity() {
        super();
    }

    /**
     *
     * @return java.lang.String
     **/
    public String getCacheId() {
        return cacheId;
    }

    /**
     *
     * @param cacheId
     **/
    public void setCacheId(String cacheId) {
        this.cacheId = cacheId == null ? null : cacheId.trim();
    }

    /**
     * 缓存名称
     * @return java.lang.String
     **/
    public String getCacheName() {
        return cacheName;
    }

    /**
     * 缓存名称
     * @param cacheName 缓存名称
     **/
    public void setCacheName(String cacheName) {
        this.cacheName = cacheName == null ? null : cacheName.trim();
    }

    /**
     * 缓存版本号
     * @return java.lang.Integer
     **/
    public Integer getCacheVersion() {
        return cacheVersion;
    }

    /**
     * 缓存版本号
     * @param cacheVersion 缓存版本号
     **/
    public void setCacheVersion(Integer cacheVersion) {
        this.cacheVersion = cacheVersion;
    }
}