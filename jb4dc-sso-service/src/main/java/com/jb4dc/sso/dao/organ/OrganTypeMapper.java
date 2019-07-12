package com.jb4dc.sso.dao.organ;


import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.sso.dbentities.OrganTypeEntity;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2018/7/27
 * To change this template use File | Settings | File Templates.
 */
public interface OrganTypeMapper extends BaseMapper<OrganTypeEntity> {
    OrganTypeEntity selectByOrganValue(String organTypeValue);

    OrganTypeEntity selectLessThanRecord(String id);

    OrganTypeEntity selectGreaterThanRecord(String id);
}
