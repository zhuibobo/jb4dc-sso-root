package com.jb4dc.sso.service.department;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dbentities.DepartmentEntity;

import java.util.List;

public interface IDepartmentService extends IBaseService<DepartmentEntity> {

    boolean existOrganRootDept(JB4DCSession jb4DSession, String organId);

    boolean existChildsDepartment(JB4DCSession jb4DSession, String id);

    List<DepartmentEntity> getDepartmentsByOrganId(String organId);

    DepartmentEntity getOrganRootDepartment(JB4DCSession jb4DSession, String organId);
}