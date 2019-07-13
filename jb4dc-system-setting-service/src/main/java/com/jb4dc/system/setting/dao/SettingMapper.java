package com.jb4dc.system.setting.dao;

import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.system.setting.dbentities.SettingEntity;
import org.springframework.stereotype.Repository;

public interface SettingMapper extends BaseMapper<SettingEntity> {
    SettingEntity selectByKeyField(String key);
}