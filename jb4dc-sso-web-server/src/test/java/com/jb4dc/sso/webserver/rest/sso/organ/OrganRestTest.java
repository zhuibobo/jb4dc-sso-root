package com.jb4dc.sso.webserver.rest.sso.organ;

import com.jb4dc.base.dbaccess.exenum.EnableTypeEnum;
import com.jb4dc.base.dbaccess.exenum.TrueFalseEnum;
import com.jb4dc.base.tools.JsonUtility;
import com.jb4dc.base.ymls.JBuild4DCYaml;
import com.jb4dc.core.base.tools.DateUtility;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.po.DepartmentUserPO;
import com.jb4dc.sso.dbentities.department.DepartmentEntity;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.service.department.IDepartmentUserService;
import com.jb4dc.sso.webserver.RestTestBase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/2/26
 * To change this template use File | Settings | File Templates.
 */
public class OrganRestTest extends RestTestBase {

    @Test
    public void addOrganAndDepartmentAndUserTest() throws Exception {
        DeleteOrgan("10001");
        NewOrgan("10001","新德家园","0","Logo_1.png");
        String deptId="DA4B";

        DeleteDepartment(deptId);
        Map<String,String> paras=new HashMap<>();
        paras.put("organId", "10001");
        JBuild4DCResponseVo departmentVo=simpleGetData("/Rest/SSO/Dept/Department/GetOrganRootDepartment",paras);
        String rootDepartmentId=((LinkedHashMap) departmentVo.getData()).get("deptId").toString();
        NewDepartment(deptId,rootDepartmentId, "10001");

        for (int i=1;i<11;i++) {
            String organIdL1="Root_"+i;
            DeleteOrgan(organIdL1);
            NewOrgan(organIdL1,organIdL1,"0","Logo_"+i+".png");
            for(int j=1;j<11;j++) {
                String organIdL2 = organIdL1 + "_" + j;
                DeleteOrgan(organIdL2);
                NewOrgan(organIdL2, organIdL2,organIdL1, "Logo_" + j + ".png");
            }
        }
    }

    private void DeleteOrgan(String organId) throws Exception {
        Map<String,String> paras=new HashMap<>();
        paras.put("warningOperationCode", JBuild4DCYaml.getWarningOperationCode());
        paras.put("organId", organId);
        JBuild4DCResponseVo responseVo =simpleDelete("/Rest/SSO/Org/Organ/DeleteByOrganId",organId,paras);
    }

    private void NewOrgan(String organId,String organName,String parentId,String logoFileName) throws Exception {
        OrganEntity organEntity=new OrganEntity();
        organEntity.setOrganParentId(parentId);
        //String organId="Root_"+i;
        organEntity.setOrganId(organId);
        organEntity.setOrganName(organName);
        organEntity.setOrganShortName(organName);
        organEntity.setOrganIsVirtual("否");
        organEntity.setOrganStatus(EnableTypeEnum.enable.getDisplayName());

        BufferedInputStream fi1= (BufferedInputStream) this.getClass().getResourceAsStream("/OrganLogo/"+logoFileName);
        MockMultipartFile fstmp = new MockMultipartFile("file", "logo.jpg", "multipart/form-data",fi1);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/Rest/SSO/Org/Organ/UploadOrganLogo")
                .file(fstmp).sessionAttr("JB4DCSession",getSession())
                .param("name","OrganLogo")).andReturn();
        String json = result.getResponse().getContentAsString();
        JBuild4DCResponseVo responseVo = JsonUtility.toObject(json, JBuild4DCResponseVo.class);
        String logoFileId=((LinkedHashMap) responseVo.getData()).get("fileId").toString();
        organEntity.setOrganMainImageId(logoFileId);
        responseVo=simpleSaveEdit("/Rest/SSO/Org/Organ/SaveEdit",organEntity);
        Assert.assertTrue(responseVo.isSuccess());


        //获取根部门
        Map<String,String> paras=new HashMap<>();
        paras.put("organId", organId);
        JBuild4DCResponseVo departmentVo=simpleGetData("/Rest/SSO/Dept/Department/GetOrganRootDepartment",paras);
        String rootDepartmentId=((LinkedHashMap) departmentVo.getData()).get("deptId").toString();
        for (int i=1;i<4;i++) {
            String deptIdL1="Dept_1_"+i+"_ORG="+organId;
            DeleteDepartment(deptIdL1);
            NewDepartment(deptIdL1,rootDepartmentId,organId);
            for(int j=1;j<4;j++) {
                String deptIdL2 = "Dept_"+i+"_"+j+"_"+organId;
                DeleteDepartment(deptIdL2);
                NewDepartment(deptIdL2, deptIdL1, organId);
            }
        }
    }

    private void DeleteDepartment(String departmentId) throws Exception {
        Map<String,String> paras=new HashMap<>();
        paras.put("warningOperationCode", JBuild4DCYaml.getWarningOperationCode());
        paras.put("departmentId", departmentId);
        JBuild4DCResponseVo responseVo =simpleDelete("/Rest/SSO/Dept/Department/DeleteByDepartmentId",departmentId,paras);
    }

    private void NewDepartment(String deptId,String parentId,String organId) throws Exception {
        DepartmentEntity departmentEntity=new DepartmentEntity();
        departmentEntity.setDeptId(deptId);
        departmentEntity.setDeptName(deptId);
        departmentEntity.setDeptShortName(deptId);
        departmentEntity.setDeptNo(deptId);
        departmentEntity.setDeptPerCharge("PerCharge");
        departmentEntity.setDeptPerChargePhone("PerChargePhone");
        departmentEntity.setDeptIsVirtual(TrueFalseEnum.False.getDisplayName());
        departmentEntity.setDeptParentId(parentId);
        departmentEntity.setDeptStatus(EnableTypeEnum.enable.getDisplayName());
        departmentEntity.setDeptOrganId(organId);
        departmentEntity.setDeptDesc("DeptDesc");
        JBuild4DCResponseVo responseVo=simpleSaveEdit("/Rest/SSO/Dept/Department/SaveEdit",departmentEntity);
        Assert.assertTrue(responseVo.isSuccess());

        for(int i=0;i<4;i++) {
            NewDepartmentUser(deptId, deptId + "_UserName_" + i + DateUtility.getDate_yyyyMMddHHmmssSSS(), deptId + "_Account_" + i + DateUtility.getDate_yyyyMMddHHmmssSSS());
        }
    }

    @Autowired
    IDepartmentUserService departmentUserService;
    private void NewDepartmentUser(String departmentId,String userName,String account) throws Exception {
        //DepartmentUserVo departmentUserVo=simpleGetData("/PlatFormRest/SSO/Department",)
        DepartmentUserPO newDepartmentUserVo=departmentUserService.getEmptyNewVo(null,departmentId);
        newDepartmentUserVo.getUserEntity().setUserAccount(account);
        newDepartmentUserVo.getUserEntity().setUserName(userName);
        newDepartmentUserVo.getUserEntity().setUserPhoneNumber("13927425407");
        newDepartmentUserVo.getDepartmentUserEntity().setDuTitle("清洁工");
        JBuild4DCResponseVo responseVo=simpleSaveEdit("/Rest/SSO/Dept/DepartmentUser/SaveEdit",newDepartmentUserVo);
        System.out.println(responseVo.getMessage());
        Assert.assertTrue(responseVo.isSuccess());
    }
}
