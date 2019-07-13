package com.jb4dc.system.setting.dao;

import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.system.setting.dbentities.DictionaryGroupEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/11
 * To change this template use File | Settings | File Templates.
 */

public interface DictionaryGroupMapper extends BaseMapper<DictionaryGroupEntity> {
    List<DictionaryGroupEntity> selectChilds(String id);

    DictionaryGroupEntity selectByValue(String dictGroupValue);
}