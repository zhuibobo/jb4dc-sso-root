package com.jb4dc.sso.service.role.impl;

import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dao.role.RoleMapper;
import com.jb4dc.sso.dbentities.RoleEntity;
import com.jb4dc.sso.service.role.IRoleService;

import java.util.Date;

public class RoleServiceImpl extends BaseServiceImpl<RoleEntity> implements IRoleService
{
    RoleMapper roleMapper;
    public RoleServiceImpl(RoleMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        roleMapper=_defaultBaseMapper;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DSession, String id, RoleEntity record) throws JBuild4DCGenerallyException {
        return super.save(jb4DSession,id, record, new IAddBefore<RoleEntity>() {
            @Override
            public RoleEntity run(JB4DCSession jb4DSession,RoleEntity sourceEntity) throws JBuild4DCGenerallyException {
                sourceEntity.setRoleOrderNum(roleMapper.nextOrderNum());
                sourceEntity.setRoleCreateTime(new Date());
                sourceEntity.setRoleCreaterId(jb4DSession.getUserId());
                sourceEntity.setRoleOrganId(jb4DSession.getOrganId());
                return sourceEntity;
            }
        });
    }

    @Override
    public int countInRoleGroup(String groupId) {
        return roleMapper.countInRoleGroup(groupId);
    }

    @Override
    public void moveUp(JB4DCSession jb4DSession, String id) throws JBuild4DCGenerallyException {
        RoleEntity selfEntity=roleMapper.selectByPrimaryKey(id);
        RoleEntity ltEntity=roleMapper.selectGreaterThanRecord(id,selfEntity.getRoleGroupId());
        switchOrder(ltEntity,selfEntity);
    }

    @Override
    public void moveDown(JB4DCSession jb4DSession, String id) throws JBuild4DCGenerallyException {
        RoleEntity selfEntity=roleMapper.selectByPrimaryKey(id);
        RoleEntity ltEntity=roleMapper.selectLessThanRecord(id,selfEntity.getRoleGroupId());
        switchOrder(ltEntity,selfEntity);
    }

    private void switchOrder(RoleEntity toEntity,RoleEntity selfEntity) {
        if(toEntity !=null){
            int newNum= toEntity.getRoleOrderNum();
            toEntity.setRoleOrderNum(selfEntity.getRoleOrderNum());
            selfEntity.setRoleOrderNum(newNum);
            roleMapper.updateByPrimaryKeySelective(toEntity);
            roleMapper.updateByPrimaryKeySelective(selfEntity);
        }
    }
}
