package com.jb4dc.sso.service.authority;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dbentities.authority.AuthorityEntity;

import java.util.List;
import java.util.Map;

public interface IAuthorityService extends IBaseService<AuthorityEntity> {
    void saveOwnerAuth(JB4DCSession session, String authOwnerType, String authOwnerId,String authObjType, List<String> systemAuthObjIdList, List<String> menuAuthObjIdList, String systemId) throws JBuild4DCGenerallyException;

    void saveOwnerAuth(JB4DCSession session, String authOwnerType, String authOwnerId, List<AuthorityEntity> authorityEntities,List<String> removeAuthObjIdList);

    List<AuthorityEntity> getOwnerAuth(JB4DCSession session, String authOwnerType, String authOwnerId);

    List<Map<String,Object>> getObjAuthOwnerDesc(JB4DCSession session, String authObjId)  throws JBuild4DCGenerallyException;
}
