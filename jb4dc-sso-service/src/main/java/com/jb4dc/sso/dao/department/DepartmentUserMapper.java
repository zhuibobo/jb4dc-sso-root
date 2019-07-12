package com.jb4dc.sso.dao.department;


import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.sso.dbentities.DepartmentUserEntity;

import java.util.List;
import java.util.Map;

public interface DepartmentUserMapper extends BaseMapper<DepartmentUserEntity> {
    int selectDepartmentUserCount(String departmentId);

    List<Map<String, Object>> selectDUByDepartment(Map<String, Object> searchMap);
}
