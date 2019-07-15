package com.jb4dc.sso.service.user.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.sso.dao.user.UserMapper;
import com.jb4dc.sso.dbentities.user.UserEntity;
import com.jb4dc.sso.service.user.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity> implements IUserService
{
    UserMapper userMapper;
    public UserServiceImpl(UserMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        userMapper=_defaultBaseMapper;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DSession, String id, UserEntity record) throws JBuild4DCGenerallyException {
        return super.save(jb4DSession,id, record, new IAddBefore<UserEntity>() {
            @Override
            public UserEntity run(JB4DCSession jb4DSession,UserEntity sourceEntity) throws JBuild4DCGenerallyException {
                //设置排序,以及其他参数--nextOrderNum()
                return sourceEntity;
            }
        });
    }

    @Override
    public UserEntity getByAccount(String userAccount) {
        return userMapper.selectByAccount(userAccount);
    }

    @Override
    public PageInfo<UserEntity> getBindRoleUsers(String roleId,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserEntity> list=userMapper.selectBindRoleUsers(roleId);
        PageInfo<UserEntity> pageInfo = new PageInfo<UserEntity>(list);
        if(pageInfo.getSize()==0&&pageInfo.getPageNum()>1){
            //如果查询的结果为0,退回查询前一页的数据;
            return getBindRoleUsers(roleId,pageNum-1,pageSize);
        }
        return pageInfo;
    }

    @Override
    public void statusChange(JB4DCSession jb4DSession, String ids, String status) throws JBuild4DCGenerallyException {
        if(StringUtility.isNotEmpty(ids)) {
            String[] idArray = ids.split(";");
            for (int i = 0; i < idArray.length; i++) {
                UserEntity entity = getByPrimaryKey(jb4DSession, idArray[i]);
                entity.setUserStatus(status);
                userMapper.updateByPrimaryKeySelective(entity);
            }
        }
    }
}

