package com.jb4dc.sso.service.department.impl;

import com.jb4dc.base.dbaccess.exenum.EnableTypeEnum;
import com.jb4dc.base.dbaccess.exenum.TrueFalseEnum;
import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.UUIDUtility;
import com.jb4dc.sso.dao.department.DepartmentMapper;
import com.jb4dc.sso.dbentities.department.DepartmentEntity;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.service.department.IDepartmentService;
import com.jb4dc.sso.service.department.IDepartmentUserService;
import com.jb4dc.sso.service.organ.IOnOrganChangeAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DepartmentServiceImpl extends BaseServiceImpl<DepartmentEntity> implements IDepartmentService, IOnOrganChangeAware
{
    DepartmentMapper departmentMapper;

    @Autowired
    IDepartmentUserService departmentUserService;

    @Autowired
    public DepartmentServiceImpl(DepartmentMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        departmentMapper=_defaultBaseMapper;
        //departmentUserService=_departmentUserService;
        //departmentUserService=_departmentUserService;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DSession, String id, DepartmentEntity record) throws JBuild4DCGenerallyException {
        return super.save(jb4DSession,id, record, new IAddBefore<DepartmentEntity>() {
            @Override
            public DepartmentEntity run(JB4DCSession jb4DSession, DepartmentEntity sourceEntity) throws JBuild4DCGenerallyException {
                //父节点
                DepartmentEntity parentDepartmentEntity = departmentMapper.selectByPrimaryKey(sourceEntity.getDeptParentId());

                sourceEntity.setDeptChildCount(0);
                sourceEntity.setDeptOrderNum(departmentMapper.nextOrderNum());
                sourceEntity.setDeptCreateTime(new Date());
                String parentIdList;

                parentIdList = parentDepartmentEntity.getDeptParentIdList();
                parentDepartmentEntity.setDeptChildCount(parentDepartmentEntity.getDeptChildCount() + 1);
                departmentMapper.updateByPrimaryKeySelective(parentDepartmentEntity);

                sourceEntity.setDeptParentIdList(parentIdList + "*" + sourceEntity.getDeptId());
                sourceEntity.setDeptOrganId(parentDepartmentEntity.getDeptOrganId());
                sourceEntity.setDeptCreatorId(jb4DSession.getUserId());
                sourceEntity.setDeptIsRoot(TrueFalseEnum.False.getDisplayName());

                return sourceEntity;
            }
        });
    }

    @Override
    public boolean existOrganRootDept(JB4DCSession jb4DSession, String organId){
        int count=departmentMapper.existOrganRootDept(organId);
        return count>0;
    }

    @Override
    public boolean existChildsDepartment(JB4DCSession jb4DSession, String id){
        int count=departmentMapper.countChildsDepartment(id);
        return count>0;
    }

    @Override
    public List<DepartmentEntity> getDepartmentsByOrganId(String organId) {
        return departmentMapper.selectDepartmentsByOrganId(organId);
    }

    @Override
    public DepartmentEntity getOrganRootDepartment(JB4DCSession jb4DSession, String organId){
        return departmentMapper.selectOrganRootDepartment(organId);
    }

    @Override
    public int deleteByKey(JB4DCSession jb4DSession, String id) throws JBuild4DCGenerallyException {
        DepartmentEntity departmentEntity=departmentMapper.selectByPrimaryKey(id);
        if(departmentEntity.getDeptIsRoot().equals(TrueFalseEnum.True.getDisplayName())){
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"不能删除根部门!");
        }
        if(departmentUserService.existUserInDepartment(jb4DSession,id)){
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"该部门下存在部门用户,请先删除或者进行迁移!");
        }
        if(!this.existChildsDepartment(jb4DSession,id)){
            return super.deleteByKey(jb4DSession, id);
        }
        else{
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"该部门下存在子部门,请先删除子部门!");
        }
    }

    @Override
    public boolean organCreated(JB4DCSession jb4DSession, OrganEntity organEntity) {
        if(!this.existOrganRootDept(jb4DSession,organEntity.getOrganId())) {
            DepartmentEntity departmentEntity = new DepartmentEntity();
            departmentEntity.setDeptId(UUIDUtility.getUUID());
            departmentEntity.setDeptName(organEntity.getOrganName());
            departmentEntity.setDeptShortName(organEntity.getOrganShortName());
            departmentEntity.setDeptNo(organEntity.getOrganNo());
            departmentEntity.setDeptIsVirtual(TrueFalseEnum.True.getDisplayName());
            departmentEntity.setDeptChildCount(0);
            departmentEntity.setDeptCreateTime(new Date());
            departmentEntity.setDeptCreatorId(jb4DSession.getUserId());
            departmentEntity.setDeptOrderNum(departmentMapper.nextOrderNum());
            departmentEntity.setDeptIsRoot(TrueFalseEnum.True.getDisplayName());
            departmentEntity.setDeptParentId("0");
            departmentEntity.setDeptParentIdList("0");
            departmentEntity.setDeptStatus(EnableTypeEnum.enable.getDisplayName());
            departmentEntity.setDeptOrganId(organEntity.getOrganId());
            departmentEntity.setDeptDesc("组织下的根部门");
            departmentMapper.insert(departmentEntity);
        }
        return true;
    }

    @Override
    public boolean organUpdated(JB4DCSession jb4DSession, OrganEntity organEntity) {
        DepartmentEntity departmentEntity=getOrganRootDepartment(jb4DSession,organEntity.getOrganId());
        departmentEntity.setDeptName(organEntity.getOrganName());
        departmentEntity.setDeptShortName(organEntity.getOrganShortName());
        departmentMapper.updateByPrimaryKeySelective(departmentEntity);
        return true;
    }
}