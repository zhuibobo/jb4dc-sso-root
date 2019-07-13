package com.jb4dc.sso.service.user.impl;

import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.UUIDUtility;
import com.jb4dc.sso.dao.user.UserRoleMapper;
import com.jb4dc.sso.dbentities.user.UserRoleEntity;
import com.jb4dc.sso.service.user.IUserRoleService;

import java.util.Date;
import java.util.List;

public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleEntity> implements IUserRoleService
{
    UserRoleMapper userRoleMapper;
    public UserRoleServiceImpl(UserRoleMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        userRoleMapper=_defaultBaseMapper;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DSession, String id, UserRoleEntity record) throws JBuild4DCGenerallyException {
        return super.save(jb4DSession,id, record, new IAddBefore<UserRoleEntity>() {
            @Override
            public UserRoleEntity run(JB4DCSession jb4DSession,UserRoleEntity sourceEntity) throws JBuild4DCGenerallyException {

                sourceEntity.setBindOrderNum(userRoleMapper.nextOrderNum());
                sourceEntity.setBindCreateTime(new Date());
                sourceEntity.setBindCreatorId(jb4DSession.getUserId());
                sourceEntity.setBindOrganId(jb4DSession.getOrganId());
                //设置排序,以及其他参数--nextOrderNum()
                return sourceEntity;
            }
        });
    }

    @Override
    public void bindUsersWithRole(JB4DCSession jb4DSession,String roleId, List<String> userIds) throws JBuild4DCGenerallyException {
        if(roleId!=null&&!roleId.equals("")){
            for (String userId : userIds) {
                if(!this.bindExist(jb4DSession,roleId,userId)) {
                    UserRoleEntity userRoleEntity = new UserRoleEntity();
                    userRoleEntity.setBindId(UUIDUtility.getUUID());
                    userRoleEntity.setBindRoleId(roleId);
                    userRoleEntity.setBindUserId(userId);
                    this.saveSimple(jb4DSession, roleId, userRoleEntity);
                }
            }
        }
    }

    @Override
    public boolean bindExist(JB4DCSession jb4DSession, String roleId, String userId) {
        if(userRoleMapper.bindExist(roleId,userId)>0){
            return true;
        }
        return false;
    }

    @Override
    public void deleteUserRoleBind(String roleId, String userId) {
        userRoleMapper.deleteUserRoleBind(roleId,userId);
    }

    @Override
    public void clearAllRoleMember(String roleId) {
        userRoleMapper.clearAllRoleMember(roleId);
    }
}
