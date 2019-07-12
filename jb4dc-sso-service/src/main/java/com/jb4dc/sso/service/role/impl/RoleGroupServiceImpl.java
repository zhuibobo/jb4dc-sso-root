package com.jb4dc.sso.service.role.impl;

import com.jb4dc.base.dbaccess.exenum.TrueFalseEnum;
import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.base.ymls.JBuild4DCYaml;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dao.role.RoleGroupMapper;
import com.jb4dc.sso.dbentities.RoleGroupEntity;
import com.jb4dc.sso.service.role.IRoleGroupService;
import com.jb4dc.sso.service.role.IRoleService;

import java.util.Date;
import java.util.List;

public class RoleGroupServiceImpl extends BaseServiceImpl<RoleGroupEntity> implements IRoleGroupService
{
    private String rootId="0";
    private String rootParentId="-1";

    //@Autowired
    IRoleService roleService;

    RoleGroupMapper roleGroupMapper;
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

                sourceEntity.setRoleGroupCreaterId(jb4DSession.getUserId());
                sourceEntity.setRoleGroupOrganId(jb4DSession.getOrganId());

                //设置排序,以及其他参数--nextOrderNum()
                return sourceEntity;
            }
        });
    }

    @Override
    public void initSystemData(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException {
        this.deleteByKeyNotValidate(jb4DSession,rootId, JBuild4DCYaml.getWarningOperationCode());
        RoleGroupEntity rootEntity=new RoleGroupEntity();
        rootEntity.setRoleGroupId(rootId);
        rootEntity.setRoleGroupParentId(rootParentId);
        rootEntity.setRoleGroupIssystem(TrueFalseEnum.True.getDisplayName());
        rootEntity.setRoleGroupName("角色组分组");
        rootEntity.setRoleGroupDelEnable(TrueFalseEnum.False.getDisplayName());
        this.saveSimple(jb4DSession,rootEntity.getRoleGroupId(),rootEntity);
        rootEntity=roleGroupMapper.selectByPrimaryKey(rootId);
        rootEntity.setRoleGroupOrderNum(1);
        roleGroupMapper.updateByPrimaryKeySelective(rootEntity);
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