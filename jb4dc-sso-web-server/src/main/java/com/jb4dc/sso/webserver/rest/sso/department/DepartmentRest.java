package com.jb4dc.sso.webserver.rest.sso.department;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.feb.dist.webserver.rest.base.GeneralRest;
import com.jb4dc.sso.dbentities.department.DepartmentEntity;
import com.jb4dc.sso.service.department.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/Rest/SSO/Dept/Department")
public class DepartmentRest extends GeneralRest<DepartmentEntity> {

    @Autowired
    IDepartmentService departmentService;

    @Override
    protected IBaseService<DepartmentEntity> getBaseService() {
        return departmentService;
    }

    @RequestMapping(value = "/GetDepartmentsByOrganId", method = RequestMethod.POST)
    public JBuild4DCResponseVo getDepartmentsByOrganId(String organId){
        List<DepartmentEntity> departmentEntityList=departmentService.getDepartmentsByOrganId(organId);
        return JBuild4DCResponseVo.getDataSuccess(departmentEntityList);
    }

    @RequestMapping(value = "/GetOrganRootDepartment",method = RequestMethod.POST)
    public JBuild4DCResponseVo getOrganRootDepartment(String organId)
    {
        DepartmentEntity rootEntity=departmentService.getOrganRootDepartment(JB4DCSessionUtility.getSession(),organId);
        return JBuild4DCResponseVo.getDataSuccess(rootEntity);
    }

    @RequestMapping(value = "/DeleteByDepartmentId",method = RequestMethod.DELETE)
    public JBuild4DCResponseVo deleteByDepartmentId(String departmentId,String warningOperationCode) throws JBuild4DCGenerallyException {
        departmentService.deleteByKeyNotValidate(JB4DCSessionUtility.getSession(),departmentId,warningOperationCode);
        return JBuild4DCResponseVo.deleteSuccess();
    }
}
