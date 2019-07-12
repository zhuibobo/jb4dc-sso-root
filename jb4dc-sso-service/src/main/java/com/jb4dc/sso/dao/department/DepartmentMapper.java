package com.jb4dc.sso.dao.department;


import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.sso.dbentities.department.DepartmentEntity;

import java.util.List;

public interface DepartmentMapper extends BaseMapper<DepartmentEntity> {
    int existOrganRootDept(String organId);

    DepartmentEntity selectOrganRootDepartment(String organId);

    List<DepartmentEntity> selectDepartmentsByOrganId(String organId);

    int countChildsDepartment(String id);
}
