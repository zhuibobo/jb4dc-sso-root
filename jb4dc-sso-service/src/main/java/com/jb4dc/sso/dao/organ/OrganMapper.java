package com.jb4dc.sso.dao.organ;

import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2018/7/27
 * To change this template use File | Settings | File Templates.
 */
public interface OrganMapper extends BaseMapper<OrganEntity> {
    OrganEntity selectLessThanRecord(@Param("id") String id, @Param("parentId") String organParentId);

    OrganEntity selectGreaterThanRecord(@Param("id") String id, @Param("parentId") String organParentId);

    void deleteByOrganName(String organName);

    int countChildsOrgan(String organId);
}
