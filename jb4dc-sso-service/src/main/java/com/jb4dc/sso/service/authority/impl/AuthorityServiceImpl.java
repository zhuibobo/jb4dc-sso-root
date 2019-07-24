package com.jb4dc.sso.service.authority.impl;

import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.core.base.tools.UUIDUtility;
import com.jb4dc.sso.dao.authority.AuthorityMapper;
import com.jb4dc.sso.dbentities.authority.AuthorityEntity;
import com.jb4dc.sso.service.authority.IAuthorityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl extends BaseServiceImpl<AuthorityEntity> implements IAuthorityService
{
    AuthorityMapper authorityMapper;
    public AuthorityServiceImpl(AuthorityMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        authorityMapper=_defaultBaseMapper;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DSession, String id, AuthorityEntity record) throws JBuild4DCGenerallyException {
        return super.save(jb4DSession,id, record, new IAddBefore<AuthorityEntity>() {
            @Override
            public AuthorityEntity run(JB4DCSession jb4DSession,AuthorityEntity sourceEntity) throws JBuild4DCGenerallyException {
                //设置排序,以及其他参数--nextOrderNum()
                return sourceEntity;
            }
        });
    }

    @Override
    public void saveOwnerAuth(JB4DCSession session, String authOwnerType, String authOwnerId,String authObjType, List<String> systemAuthObjIdList, List<String> menuAuthObjIdList, String systemId) throws JBuild4DCGenerallyException {
        //if(authObjIdList)
        if(StringUtility.isEmpty(authOwnerId)){
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"authOwnerId 不能为空!");
        }
        if(authObjType.equals("Menu")){
            if(StringUtility.isEmpty(systemId)){
                throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"systemId 不能为空!");
            }
            //先删除掉旧的权限关联
            authorityMapper.deleteOldSystemAndMenuByOwnerId(authOwnerId,systemId);
            //保存新提交的权限绑定
            if(systemAuthObjIdList!=null&&systemAuthObjIdList.size()>0){
                for (String authObjId : systemAuthObjIdList) {
                    AuthorityEntity authorityEntity=new AuthorityEntity(UUIDUtility.getUUID(),systemId,"System",authOwnerId,authOwnerType,systemId,session.getUserId(),session.getOrganId(),"0","");
                    authorityMapper.insertSelective(authorityEntity);
                }
            }
            if(menuAuthObjIdList!=null&&menuAuthObjIdList.size()>0){
                for (String authObjId : menuAuthObjIdList) {
                    AuthorityEntity authorityEntity=new AuthorityEntity(UUIDUtility.getUUID(),systemId,"Menu",authOwnerId,authOwnerType,systemId,session.getUserId(),session.getOrganId(),"0","");
                    authorityMapper.insertSelective(authorityEntity);
                }
            }
        }
    }
}