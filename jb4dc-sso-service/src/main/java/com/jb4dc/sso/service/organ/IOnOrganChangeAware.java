package com.jb4dc.sso.service.organ;

import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dbentities.organ.OrganEntity;

public interface IOnOrganChangeAware {
     boolean organCreated(JB4DCSession jb4DSession, OrganEntity organEntity);
     boolean organUpdated(JB4DCSession jb4DSession, OrganEntity organEntity);
}
