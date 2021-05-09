package com.jb4dc.sso.service.organ;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dbentities.organ.OrganEntity;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2018/7/27
 * To change this template use File | Settings | File Templates.
 */
public interface IOrganService extends IBaseService<OrganEntity> {

    OrganEntity createRootOrgan(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException;

    void deleteByOrganName(JB4DCSession jb4DSession, String organName, String warningOperationCode);

    void initSystemData(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException;

    List<OrganEntity> getALLEnableOrgan();

    List<OrganEntity> getALLEnableOrganMinProp();

    List<String> getAllChildOrganIdIncludeSelf(String organId);

    List<OrganEntity> getEnableChildOrgan(String organId);

    Map<String, Map<String,String>> getEnableOrganMinMapJsonPropRT(JB4DCSession jb4DSession);
}
