package com.jb4dc.sso.service.department;

import com.github.pagehelper.PageInfo;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.po.DepartmentUserPO;
import com.jb4dc.sso.dbentities.department.DepartmentUserEntity;

import java.util.List;
import java.util.Map;

public interface IDepartmentUserService {
    int save(JB4DCSession jb4DSession, String id, DepartmentUserPO record, String accountPassword) throws JBuild4DCGenerallyException;

    DepartmentUserPO getEmptyNewVo(JB4DCSession jb4DSession, String departmentId) throws JBuild4DCGenerallyException;

    DepartmentUserEntity getByPrimaryKey(JB4DCSession jb4DSession, String recordId);

    boolean existUserInDepartment(JB4DCSession jb4DSession, String departmentId);

    DepartmentUserPO getVo(JB4DCSession jb4DSession, String departmentUserId) throws JBuild4DCGenerallyException;

    PageInfo<List<Map<String, Object>>> getDepartmentUser(JB4DCSession jb4DSession, Integer pageNum, Integer pageSize, Map<String, Object> searchMap);

    void statusChange(JB4DCSession jb4DSession, String ids, String status) throws JBuild4DCGenerallyException;

    void deleteDepartUserAndUser(JB4DCSession jb4DSession, String userId) throws JBuild4DCGenerallyException;

    void deleteDepartUserAndUserByDuId(JB4DCSession jb4DSession, String recordId) throws JBuild4DCGenerallyException;

}