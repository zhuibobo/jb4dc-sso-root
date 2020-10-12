package com.jb4dc.sso.webserver.rest.sso.organ;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.feb.dist.webserver.rest.base.GeneralRest;
import com.jb4dc.files.dbentities.FileInfoEntity;
import com.jb4dc.files.service.IFileInfoService;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.organ.OrganTypeEntity;
import com.jb4dc.sso.service.organ.IOrganService;
import com.jb4dc.sso.service.organ.IOrganTypeService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/Rest/SSO/Org/Organ")
public class OrganRest extends GeneralRest<OrganEntity> {

    byte[] defaultImageByte=null;

    public OrganRest(){
        if(defaultImageByte==null){
            try {
                InputStream is = this.getClass().getResourceAsStream("/static/Themes/Default/Css/Images/DefaultLogo.png");
                defaultImageByte = IOUtils.toByteArray(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getModuleName() {
        return "组织机构";
    }

    @Autowired
    protected IOrganService organService;

    @Autowired
    protected IOrganTypeService organTypeService;

    @Autowired
    protected IFileInfoService fileInfoService;

    @Override
    protected IBaseService<OrganEntity> getBaseService() {
        return organService;
    }

    @Override
    protected Map<String, Object> bindObjectsToMV() {
        List<OrganTypeEntity> organTypeEntityList=organTypeService.getALL(JB4DCSessionUtility.getSession());
        Map<String,Object> result=new HashMap<>();
        result.put("OrganType",organTypeEntityList);
        return result;
    }

    @RequestMapping(value = "/UploadOrganLogo", method = RequestMethod.POST, produces = "application/json")
    public JBuild4DCResponseVo uploadOrganLogo(HttpServletRequest request, @RequestParam("file") MultipartFile file,String organId) throws IOException, JBuild4DCGenerallyException {
        if(StringUtility.isEmpty(organId)){
            throw JBuild4DCGenerallyException.getEmptyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"organId");
        }
        FileInfoEntity fileInfoEntity=fileInfoService.addSmallFileToDB(JB4DCSessionUtility.getSession(),file, organId,"TSSO_ORGAN",IFileInfoService.FILE_OBJ_TYPE_TABLE_NAME,IFileInfoService.FILE_CATEGORY_MAIN_IMAGE);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.SUCCESSMSG,fileInfoEntity);
    }

    @RequestMapping(value = "/GetOrganLogo", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getOrganLogo(String fileId) throws IOException, JBuild4DCGenerallyException {
        FileInfoEntity fileInfoEntity=fileInfoService.getByPrimaryKey(JB4DCSessionUtility.getSession(),fileId);
        if(fileInfoEntity==null) {
            return defaultImageByte;
        }
        else{
            return fileInfoService.getContentInDB(fileId);
        }
    }

    @RequestMapping(value = "/GetFullOrgan", method = RequestMethod.POST)
    public JBuild4DCResponseVo getFullOrgan() {
        List<OrganEntity> organEntityList=organService.getALL(JB4DCSessionUtility.getSession());
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.GETDATASUCCESSMSG,organEntityList);
    }

    @RequestMapping(value = "/DeleteByName",method = RequestMethod.DELETE)
    public JBuild4DCResponseVo deleteByName(String organName,String warningOperationCode){
        organService.deleteByOrganName(JB4DCSessionUtility.getSession(),organName,warningOperationCode);
        return JBuild4DCResponseVo.deleteSuccess();
    }

    @RequestMapping(value = "/DeleteByOrganId",method = RequestMethod.DELETE)
    public JBuild4DCResponseVo deleteByOrganId(String organId,String warningOperationCode) throws JBuild4DCGenerallyException {
        organService.deleteByKeyNotValidate(JB4DCSessionUtility.getSession(),organId,warningOperationCode);
        return JBuild4DCResponseVo.deleteSuccess();
    }
}
