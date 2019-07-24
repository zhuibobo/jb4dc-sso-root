package com.jb4dc.sso.dao.authority;


import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.sso.dbentities.authority.AuthorityEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthorityMapper extends BaseMapper<AuthorityEntity> {
    void deleteOldSystemAndMenuByOwnerId(@Param("authOwnerId") String authOwnerId,@Param("systemId") String systemId);

    void deleteAuthByOwnerId(@Param("authOwnerType") String authOwnerType,@Param("authOwnerId")  String authOwnerId,@Param("authObjIdList")  List<String> authObjIdList);

    List<AuthorityEntity> selectOwnerAuth(@Param("authOwnerType") String authOwnerType,@Param("authOwnerId") String authOwnerId);
}
