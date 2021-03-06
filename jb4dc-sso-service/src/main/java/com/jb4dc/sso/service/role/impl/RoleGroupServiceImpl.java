package com.jb4dc.sso.service.role.impl;

import com.jb4dc.base.service.exenum.EnableTypeEnum;
import com.jb4dc.base.service.exenum.TrueFalseEnum;
import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.base.ymls.JBuild4DCYaml;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dao.role.RoleGroupMapper;
import com.jb4dc.sso.dbentities.role.RoleEntity;
import com.jb4dc.sso.dbentities.role.RoleGroupEntity;
import com.jb4dc.sso.service.role.IRoleGroupService;
import com.jb4dc.sso.service.role.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.Date;
import java.util.List;

@Service
public class RoleGroupServiceImpl extends BaseServiceImpl<RoleGroupEntity> implements IRoleGroupService
{
    private String rootId="0";
    private String rootParentId="-1";

    //@Autowired
    IRoleService roleService;

    RoleGroupMapper roleGroupMapper;

    @Autowired
    public RoleGroupServiceImpl(RoleGroupMapper _defaultBaseMapper,IRoleService _roleService){
        super(_defaultBaseMapper);
        roleGroupMapper=_defaultBaseMapper;
        roleService=_roleService;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DSession, String id, RoleGroupEntity record) throws JBuild4DCGenerallyException {
        return super.save(jb4DSession,id, record, new IAddBefore<RoleGroupEntity>() {
            @Override
            public RoleGroupEntity run(JB4DCSession jb4DSession,RoleGroupEntity sourceEntity) throws JBuild4DCGenerallyException {

                sourceEntity.setRoleGroupOrderNum(roleGroupMapper.nextOrderNum());
                sourceEntity.setRoleGroupCreateTime(new Date());
                sourceEntity.setRoleGroupPidList("");
                sourceEntity.setRoleGroupChildCount(0);

                String parentIdList;
                if(sourceEntity.getRoleGroupId().equals(rootId)){
                    parentIdList=rootParentId;
                    sourceEntity.setRoleGroupPidList(rootParentId);
                }
                else
                {
                    RoleGroupEntity parentEntity=roleGroupMapper.selectByPrimaryKey(sourceEntity.getRoleGroupParentId());
                    parentIdList=parentEntity.getRoleGroupPidList();
                    parentEntity.setRoleGroupChildCount(parentEntity.getRoleGroupChildCount()+1);
                    roleGroupMapper.updateByPrimaryKeySelective(parentEntity);
                }
                sourceEntity.setRoleGroupPidList(parentIdList+"*"+sourceEntity.getRoleGroupId());

                sourceEntity.setRoleGroupCreatorId(jb4DSession.getUserId());
                sourceEntity.setRoleGroupOrganId(jb4DSession.getOrganId());

                //设置排序,以及其他参数--nextOrderNum()
                return sourceEntity;
            }
        });
    }

    @Override
    public void initSystemData(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException {

        RoleGroupEntity rootGroupEntity = createRoleGroup(jb4DSession, rootId, rootParentId, "角色分组");
        rootGroupEntity.setRoleGroupOrderNum(1);
        roleGroupMapper.updateByPrimaryKeySelective(rootGroupEntity);

        RoleGroupEntity systemManageGroupEntity = createRoleGroup(jb4DSession, "SystemManageRoleGroup", rootId, "系统管理角色组");
        this.createRole(jb4DSession,"SystemOrganAdmin","SystemOrganAdmin",systemManageGroupEntity.getRoleGroupId(),"系统管理员");

        RoleGroupEntity ssoMainAppGroupEntity = createRoleGroup(jb4DSession, "SSOMainAppRoleGroup", rootId, "单点登录系统角色组");
        this.createRole(jb4DSession,"SSOMainAppRole-General-Role","SSOMainAppRole-General-Role",ssoMainAppGroupEntity.getRoleGroupId(),"单点登录通用角色");

        RoleGroupEntity builderMainAppGroupEntity = createRoleGroup(jb4DSession, "BuilderMainAppRoleGroup", rootId, "应用构建系统角色组");
        this.createRole(jb4DSession,"BuilderMainAppRole-General-Role","BuilderMainAppRole-General-Role",builderMainAppGroupEntity.getRoleGroupId(),"应用构建通用角色");

        RoleGroupEntity qcMainAppGroupEntity = createRoleGroup(jb4DSession, "QCMainAppRoleGroup", rootId, "运维管理系统角色组");
        this.createRole(jb4DSession,"QCMainAppRoleGroup-General-Role","QCMainAppRoleGroup-General-Role",qcMainAppGroupEntity.getRoleGroupId(),"运维管理通用角色");

        RoleGroupEntity gridMainAppGroupEntity = createRoleGroup(jb4DSession, "GridMainAppRoleGroup", rootId, "网格化社会管理系统角色组");
        this.createRole(jb4DSession,"GridMainAppRoleGroup-Grid-Role","GridMainAppRoleGroup-Grid-Role",gridMainAppGroupEntity.getRoleGroupId(),"网格工作人员角色");
        this.createRole(jb4DSession,"GridMainAppRoleGroup-Community-Role","GridMainAppRoleGroup-Community-Role",gridMainAppGroupEntity.getRoleGroupId(),"村社区角色");
        this.createRole(jb4DSession,"GridMainAppRoleGroup-Street-Role","GridMainAppRoleGroup-Street-Role",gridMainAppGroupEntity.getRoleGroupId(),"乡镇街道角色");
        this.createRole(jb4DSession,"GridMainAppRoleGroup-Dept-Role","GridMainAppRoleGroup-Dept-Role",gridMainAppGroupEntity.getRoleGroupId(),"部门角色");
        this.createRole(jb4DSession,"GridMainAppRoleGroup-Center-Role","GridMainAppRoleGroup-Center-Role",gridMainAppGroupEntity.getRoleGroupId(),"指挥中心角色");

        //RoleGroupEntity devMockAppGroupEntity = createRoleGroup(jb4DSession, "DevMockAppRoleGroup", rootId, "开发样例系统角色组");
        //this.createRole(jb4DSession,"DevMockAppRole-General-Role","DevMockAppRole-General-Role",devMockAppGroupEntity.getRoleGroupId(),"通用用户角色");
    }

    private RoleGroupEntity createRoleGroup(JB4DCSession jb4DSession,String groupId,String parentId,String groupName) throws JBuild4DCGenerallyException {
        this.deleteByKeyNotValidate(jb4DSession,groupId, JBuild4DCYaml.getWarningOperationCode());
        RoleGroupEntity roleGroupEntity=new RoleGroupEntity();
        roleGroupEntity.setRoleGroupId(groupId);
        roleGroupEntity.setRoleGroupParentId(parentId);
        roleGroupEntity.setRoleGroupIsSystem(TrueFalseEnum.True.getDisplayName());
        roleGroupEntity.setRoleGroupName(groupName);
        roleGroupEntity.setRoleGroupDelEnable(TrueFalseEnum.False.getDisplayName());
        this.saveSimple(jb4DSession,roleGroupEntity.getRoleGroupId(),roleGroupEntity);
        return roleGroupEntity;
    }

    private RoleEntity createRole(JB4DCSession jb4DSession,String roleId,String roleKey,String groupId,String roleName) throws JBuild4DCGenerallyException {
        roleService.deleteByKeyNotValidate(jb4DSession,roleId, JBuild4DCYaml.getWarningOperationCode());
        RoleEntity roleEntity=new RoleEntity();
        roleEntity.setRoleId(roleId);
        roleEntity.setRoleKey(roleKey);
        roleEntity.setRoleName(roleName);
        roleEntity.setRoleGroupId(groupId);
        roleEntity.setRoleDesc("");
        roleEntity.setRoleStatus(EnableTypeEnum.enable.getDisplayName());
        roleService.saveSimple(jb4DSession,roleEntity.getRoleId(),roleEntity);
        return roleEntity;
    }

    @Override
    public List<RoleGroupEntity> getALLOrderByAsc(JB4DCSession session) {
        return roleGroupMapper.selectAllOrderByAsc();
    }

    @Override
    public int deleteByKey(JB4DCSession jb4DSession, String id) throws JBuild4DCGenerallyException {
        if(roleGroupMapper.countChildsRoleGroup(id)>0){
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"该分组下存在子节点,请先删除子节点!");
        }
        if(roleService.countInRoleGroup(id)>0){
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"该分组下存在角色,请先删除角色!");
        }
        return super.deleteByKey(jb4DSession, id);
    }

    @Override
    public void moveUp(JB4DCSession jb4DSession, String id) throws JBuild4DCGenerallyException {
        RoleGroupEntity selfEntity=roleGroupMapper.selectByPrimaryKey(id);
        RoleGroupEntity ltEntity=roleGroupMapper.selectLessThanRecord(id,selfEntity.getRoleGroupParentId());
        switchOrder(ltEntity,selfEntity);
    }

    @Override
    public void moveDown(JB4DCSession jb4DSession, String id) throws JBuild4DCGenerallyException {
        RoleGroupEntity selfEntity=roleGroupMapper.selectByPrimaryKey(id);
        RoleGroupEntity ltEntity=roleGroupMapper.selectGreaterThanRecord(id,selfEntity.getRoleGroupParentId());
        switchOrder(ltEntity,selfEntity);
    }

    private void switchOrder(RoleGroupEntity toEntity,RoleGroupEntity selfEntity) {
        if(toEntity !=null){
            int newNum= toEntity.getRoleGroupOrderNum();
            toEntity.setRoleGroupOrderNum(selfEntity.getRoleGroupOrderNum());
            selfEntity.setRoleGroupOrderNum(newNum);
            roleGroupMapper.updateByPrimaryKeySelective(toEntity);
            roleGroupMapper.updateByPrimaryKeySelective(selfEntity);
        }
    }
}