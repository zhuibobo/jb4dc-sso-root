package com.jb4dc.sso.service.department.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.jb4dc.base.dbaccess.exenum.EnableTypeEnum;
import com.jb4dc.base.dbaccess.exenum.TrueFalseEnum;
import com.jb4dc.core.base.encryption.digitaldigest.MD5Utility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.core.base.tools.UUIDUtility;
import com.jb4dc.sso.po.DepartmentUserPO;
import com.jb4dc.sso.dao.department.DepartmentUserMapper;
import com.jb4dc.sso.dbentities.department.DepartmentEntity;
import com.jb4dc.sso.dbentities.department.DepartmentUserEntity;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.user.UserEntity;
import com.jb4dc.sso.service.department.IDepartmentService;
import com.jb4dc.sso.service.department.IDepartmentUserService;
import com.jb4dc.sso.service.organ.IOrganService;
import com.jb4dc.sso.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentUserServiceImpl implements IDepartmentUserService
{
    DepartmentUserMapper departmentUserMapper;
    IUserService userService;

    @Autowired
    IDepartmentService departmentService;

    @Autowired
    IOrganService organService;

    @Autowired
    public DepartmentUserServiceImpl(DepartmentUserMapper _defaultBaseMapper, IUserService _userService){
        //super(_defaultBaseMapper, _sqlSessionTemplate, _sqlBuilderService);
        departmentUserMapper=_defaultBaseMapper;
        userService=_userService;
    }

    public static String getValueExistErrorMsg(String value){
        return String.format("已经存在账号为:%s的用户!", value);
    }

    @Override
    @Transactional(rollbackFor= JBuild4DCGenerallyException.class)
    public int save(JB4DCSession jb4DSession, String departmentUserId, DepartmentUserPO record, String accountPassword) throws JBuild4DCGenerallyException {
        DepartmentUserEntity departmentUserEntity=departmentUserMapper.selectByPrimaryKey(departmentUserId);

        if(record.getUserEntity()==null||record.getDepartmentUserEntity()==null){
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"用户实体于部门用户实体均不能为空!");
        }
        if(!record.getDepartmentUserEntity().getDuId().equals(departmentUserId)){
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"参数departmentUserId的值于record中的部门用户实体不一致");
        }
        if(record.getUserEntity().getUserId()==null||record.getUserEntity().getUserId().equals("")){
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"record中的用户实体的UserId不能为空");
        }
        if(record.getUserEntity().getUserOrganId()==null||record.getUserEntity().getUserOrganId().equals("")){
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"record中的用户实体的组织ID不能为空!");
        }

        if(departmentUserEntity==null){

            if (userService.getByAccount(record.getUserEntity().getUserAccount()) == null) {
                //新增用户
                UserEntity addUser = record.getUserEntity();
                addUser.setUserPassword(MD5Utility.GetMD5Code(accountPassword, true));
                addUser.setUserCreateTime(new Date());
                addUser.setUserCreatorId(jb4DSession.getUserId());
                addUser.setUserStatus(EnableTypeEnum.enable.getDisplayName());
                addUser.setUserOrderNum(userService.getNextOrderNum(jb4DSession));
                userService.saveSimple(jb4DSession, addUser.getUserId(), addUser);

                //新增部门用户
                DepartmentUserEntity addDepartmentUserEntity = record.getDepartmentUserEntity();
                addDepartmentUserEntity.setDuId(departmentUserId);
                addDepartmentUserEntity.setDuUserId(addUser.getUserId());
                addDepartmentUserEntity.setDuIsMain(TrueFalseEnum.True.getDisplayName());
                addDepartmentUserEntity.setDuCreateTime(new Date());
                addDepartmentUserEntity.setDuCreatorId(jb4DSession.getUserId());
                addDepartmentUserEntity.setDuStatus(EnableTypeEnum.enable.getDisplayName());
                addDepartmentUserEntity.setDuOrderNum(departmentUserMapper.nextOrderNum());
                departmentUserMapper.insert(addDepartmentUserEntity);
            }
            else{
                throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,getValueExistErrorMsg(record.getUserEntity().getUserAccount()));
            }
        }
        else{
            UserEntity oldEntity=userService.getByAccount(record.getUserEntity().getUserAccount());
            if (oldEntity!=null){
                if(!oldEntity.getUserId().equals(record.getUserEntity().getUserId())){
                    throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,getValueExistErrorMsg(record.getUserEntity().getUserAccount()));
                }
            }

            //修改用户
            UserEntity updateUser=record.getUserEntity();
            userService.saveSimple(jb4DSession,updateUser.getUserId(),updateUser);

            //修改部门用户
            DepartmentUserEntity updateDepartmentUserEntity=record.getDepartmentUserEntity();
            departmentUserMapper.updateByPrimaryKeySelective(updateDepartmentUserEntity);
        }
        return 1;
    }

    @Override
    public DepartmentUserPO getEmptyNewVo(JB4DCSession jb4DSession, String departmentId) throws JBuild4DCGenerallyException {
        DepartmentEntity departmentEntity=departmentService.getByPrimaryKey(jb4DSession,departmentId);
        OrganEntity organEntity=organService.getByPrimaryKey(jb4DSession,departmentEntity.getDeptOrganId());

        DepartmentUserPO departmentUserVo=new DepartmentUserPO();
        departmentUserVo.setDepartmentEntity(departmentEntity);
        departmentUserVo.setOrganEntity(organEntity);

        DepartmentUserEntity departmentUserEntity=new DepartmentUserEntity();
        UserEntity userEntity=new UserEntity();

        String userId= UUIDUtility.getUUID();

        userEntity.setUserId(userId);
        userEntity.setUserOrganId(organEntity.getOrganId());
        userEntity.setUserStatus(EnableTypeEnum.enable.getDisplayName());

        departmentUserEntity.setDuId(UUIDUtility.getUUID());
        departmentUserEntity.setDuIsMain(TrueFalseEnum.True.getDisplayName());
        departmentUserEntity.setDuUserId(userId);
        departmentUserEntity.setDuStatus(EnableTypeEnum.enable.getDisplayName());
        departmentUserEntity.setDuDeptId(departmentId);

        departmentUserVo.setUserEntity(userEntity);
        departmentUserVo.setDepartmentUserEntity(departmentUserEntity);

        return departmentUserVo;
    }

    @Override
    public DepartmentUserPO getVo(JB4DCSession jb4DSession, String departmentUserId) throws JBuild4DCGenerallyException {
        DepartmentUserEntity departmentUserEntity=departmentUserMapper.selectByPrimaryKey(departmentUserId);
        UserEntity userEntity=userService.getByPrimaryKey(jb4DSession,departmentUserEntity.getDuUserId());
        DepartmentEntity departmentEntity=departmentService.getByPrimaryKey(jb4DSession,departmentUserEntity.getDuDeptId());
        OrganEntity organEntity=organService.getByPrimaryKey(jb4DSession,departmentEntity.getDeptOrganId());

        DepartmentUserPO departmentUserVo=new DepartmentUserPO();
        departmentUserVo.setDepartmentUserEntity(departmentUserEntity);
        departmentUserVo.setUserEntity(userEntity);
        departmentUserVo.setOrganEntity(organEntity);
        departmentUserVo.setDepartmentEntity(departmentEntity);

        return departmentUserVo;
    }

    @Override
    public PageInfo<List<Map<String, Object>>> getDepartmentUser(JB4DCSession jb4DSession, Integer pageNum, Integer pageSize, Map<String, Object> searchMap) {
        PageHelper.startPage(pageNum, pageSize);
        //PageHelper.
        List<Map<String,Object>> list=departmentUserMapper.selectDUByDepartment(searchMap);
        PageInfo<List<Map<String,Object>>> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public void statusChange(JB4DCSession jb4DSession, String ids, String status) throws JBuild4DCGenerallyException {
        if(StringUtility.isNotEmpty(ids)) {
            String[] idArray = ids.split(";");
            for (int i = 0; i < idArray.length; i++) {
                DepartmentUserEntity entity = getByPrimaryKey(jb4DSession, idArray[i]);
                userService.statusChange(jb4DSession,entity.getDuUserId(),status);
                //entity.setOrganStatus(status);
                //organMapper.updateByPrimaryKeySelective(entity);
            }
        }
    }

    @Override
    public DepartmentUserEntity getByPrimaryKey(JB4DCSession jb4DSession, String recordId) {
        return departmentUserMapper.selectByPrimaryKey(recordId);
    }

    @Override
    public boolean existUserInDepartment(JB4DCSession jb4DSession, String departmentId) {
        int count=departmentUserMapper.selectDepartmentUserCount(departmentId);
        return count>0;
    }


}
