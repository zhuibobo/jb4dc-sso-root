package com.jb4dc.sso.service.authority.impl;

import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
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
    public void saveOwnerAuth(JB4DCSession session, String authOwnerType, String authOwnerId, List<String> authObjIdList, String authObjType, String cleanAboutKey) {
        //if(authObjIdList)
        if(authObjType.equals("Menu")){
            //先删除掉旧的权限关联
            String systemId=cleanAboutKey;
            authorityMapper.deleteOldSystemAndMenuByOwnerId(authOwnerId,systemId);
        }
        //保存新提交的权限绑定
        if(authObjIdList!=null&&authObjIdList.size()>0){
            for (String authObjId : authObjIdList) {
                AuthorityEntity authorityEntity=new AuthorityEntity();
                authorityEntity.setAuthId(UUIDUtility.getUUID());
                authorityEntity.setAuthObjId(authObjId);
                authorityEntity.setAuthObjType(authObjType);
                authorityEntity.setAuthOwnerId(authOwnerId);
                authorityEntity.setAuthOwnerType(authOwnerType);
                authorityEntity.setAuthCreatorId(session.getUserId());
                authorityEntity.setAuthCreatorOrganId(session.getOrganId());
                authorityEntity.setAuthOrganId("0");
                authorityEntity.setAuthDesc("");
                authorityMapper.insertSelective(authorityEntity);
            }
        }
    }
}