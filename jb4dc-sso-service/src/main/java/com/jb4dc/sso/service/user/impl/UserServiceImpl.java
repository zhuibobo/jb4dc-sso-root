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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity> implements IUserService
{
    UserMapper userMapper;

    @Autowired
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
    public UserEntity getByAccount(JB4DCSession jb4DCSession,String userAccount) {
        return userMapper.selectByAccount(userAccount);
    }

    @Override
    public PageInfo<UserEntity> getBindRoleUsers(JB4DCSession jb4DCSession,String roleId,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserEntity> list=userMapper.selectBindRoleUsers(roleId);
        PageInfo<UserEntity> pageInfo = new PageInfo<UserEntity>(list);
        if(pageInfo.getSize()==0&&pageInfo.getPageNum()>1){
            //如果查询的结果为0,退回查询前一页的数据;
            return getBindRoleUsers(jb4DCSession,roleId,pageNum-1,pageSize);
        }
        return pageInfo;
    }

    @Override
    public List<UserEntity> getByOrganId(JB4DCSession jb4DCSession,String organId) {
        return userMapper.selectByOrganId(organId);
    }

    @Override
    public List<UserEntity> getALLEnableUserMinProp(JB4DCSession jb4DCSession) {
        return userMapper.selectEnableUserMinProp();
    }

    @Override
    public PageInfo<List<Map<String, Object>>> getUserByOrganSearch(JB4DCSession jb4DSession, Integer pageNum, Integer pageSize, Map<String, Object> searchMap) {
        PageHelper.startPage(pageNum, pageSize);
        //PageHelper.
        List<Map<String,Object>> list=userMapper.selectUserByOrganSearch(searchMap);
        PageInfo<List<Map<String,Object>>> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public List<UserEntity> getByUserIdList(JB4DCSession jb4DCSession, List<String> userIdList) {
        return userMapper.selectByUserIdList(userIdList);
    }

    @Override
    public List<UserEntity> getUserByRoleId(JB4DCSession jb4DCSession, String roleId) {
        return userMapper.selectUserByRoleId(roleId);
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

