package com.jb4dc.sso.service.authority;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dbentities.authority.AuthorityEntity;

import java.util.List;

public interface IAuthorityService extends IBaseService<AuthorityEntity> {
    void saveOwnerAuth(JB4DCSession session, String authOwnerType, String authOwnerId, List<String> authObjIdList, String authObjType, String cleanAboutKey);
}
