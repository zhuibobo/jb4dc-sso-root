package com.jb4dc.sso.webserver.rest.sso.department;

import com.github.pagehelper.PageInfo;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.base.service.search.GeneralSearchUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.files.dbentities.FileInfoEntity;
import com.jb4dc.files.service.IFileInfoService;
import com.jb4dc.sso.po.DepartmentUserPO;
import com.jb4dc.sso.service.department.IDepartmentUserService;
import com.jb4dc.sso.dbentities.systemsetting.SettingEntity;
import com.jb4dc.sso.service.systemsetting.ISettingService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/Rest/SSO/Dept/DepartmentUser")
public class DepartmentUserRest {

    byte[] defaultImageByte=null;

    public DepartmentUserRest(){
        if(defaultImageByte==null){
            try {
                InputStream is = this.getClass().getResourceAsStream("/static/Themes/Default/Css/Images/UserImg.png");
                defaultImageByte = IOUtils.toByteArray(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Autowired
    IDepartmentUserService departmentUserService;

    @Autowired
    ISettingService settingService;

    @Autowired
    IFileInfoService fileInfoService;

    @RequestMapping(value = "/GetEmptyNewVo",method = RequestMethod.POST)
    public JBuild4DCResponseVo getEmptyNewVo(String departmentId) throws JBuild4DCGenerallyException {
        JB4DCSession jb4DSession = JB4DCSessionUtility.getSession();
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,departmentUserService.getEmptyNewVo(jb4DSession,departmentId));
    }

    @RequestMapping(value = "/GetVo",method = RequestMethod.POST)
    public JBuild4DCResponseVo getVo(String recordId) throws JBuild4DCGenerallyException {
        JB4DCSession jb4DSession = JB4DCSessionUtility.getSession();
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,departmentUserService.getVo(jb4DSession,recordId));
    }

    @RequestMapping(value = "/SaveEdit", method = RequestMethod.POST)
    public JBuild4DCResponseVo saveEdit(@RequestBody DepartmentUserPO entity, HttpServletRequest request) throws JBuild4DCGenerallyException {
        try {
            JB4DCSession jb4DSession = JB4DCSessionUtility.getSession();
            SettingEntity defaultPasswordSetting = settingService.getByKey(jb4DSession, ISettingService.SETTINGUSERDEFAULTPASSWORD);
            String defaultPassword=defaultPasswordSetting==null?"j4d123456":defaultPasswordSetting.getSettingValue();
            departmentUserService.save(jb4DSession, entity.getDepartmentUserEntity().getDuId(), entity, defaultPassword);
            return JBuild4DCResponseVo.success(JBuild4DCResponseVo.SUCCESSMSG);
        } catch (JBuild4DCGenerallyException e) {
            return JBuild4DCResponseVo.error(e.getMessage());
        }
        catch (Exception e){
            return JBuild4DCResponseVo.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/UploadUserHeadIMG", method = RequestMethod.POST, produces = "application/json")
    public JBuild4DCResponseVo uploadUserHeadIMG(HttpServletRequest request, @RequestParam("file") MultipartFile file,String userId) throws IOException, JBuild4DCGenerallyException {
        if(StringUtility.isEmpty(userId)){
            throw JBuild4DCGenerallyException.getEmptyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"userId");
        }
        FileInfoEntity fileInfoEntity=fileInfoService.addSmallFileToDB(JB4DCSessionUtility.getSession(),file,userId,"TSSO_USER",IFileInfoService.FILE_OBJ_TYPE_TABLE_NAME,IFileInfoService.FILE_CATEGORY_USER_HEADER);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.SUCCESSMSG,fileInfoEntity);
    }

    @RequestMapping(value = "/GetUserHeadIMG", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getUserHeadIMG(String fileId) throws JBuild4DCGenerallyException {
        FileInfoEntity fileInfoEntity=fileInfoService.getByPrimaryKey(JB4DCSessionUtility.getSession(),fileId);
        if(fileInfoEntity==null) {
            return defaultImageByte;
        }
        else{
            return fileInfoService.getContentInDB(fileId);
        }
    }

    @RequestMapping(value = "/GetListData",method = RequestMethod.POST)
    public JBuild4DCResponseVo getListData(Integer pageSize,Integer pageNum,String searchCondition) throws IOException, ParseException {
        JB4DCSession jb4DSession= JB4DCSessionUtility.getSession();
        Map<String,Object> searchMap= GeneralSearchUtility.deserializationToMap(searchCondition);
        //String departmentId="";
        if(searchMap.get("searchInALL").toString().equals("是")){
            searchMap.remove("departmentId");
        }
        PageInfo<List<Map<String,Object>>> pageInfo=departmentUserService.getDepartmentUser(jb4DSession,pageNum,pageSize,searchMap);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,pageInfo);
    }

    @RequestMapping(value = "/StatusChange",method = RequestMethod.POST)
    public JBuild4DCResponseVo statusChange(String ids, String status, HttpServletRequest request)
    {
        try {
            if(StringUtility.isEmpty(ids)){
                throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"参数Ids不能为空或空串!");
            }
            if(StringUtility.isEmpty(status)){
                throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"参数status不能为空或空串!");
            }
            JB4DCSession jb4DSession=JB4DCSessionUtility.getSession();
            //operationLogService.writeOperationLog(JB4DCSessionUtility.getSession(), "单点登录",getModuleName(),actionName,getLogTypeName(),text,data,this.getClass(),request);
            departmentUserService.statusChange(jb4DSession,ids,status);
            return JBuild4DCResponseVo.opSuccess();
        } catch (JBuild4DCGenerallyException e) {
            return JBuild4DCResponseVo.opError(e.getMessage());
        }
    }
}
