/*
package com.jb4dc.sso.webserver.rest.sso.organ;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.feb.dist.webserver.rest.base.GeneralRest;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.dbentities.organ.OrganTypeEntity;
import com.jb4dc.sso.service.organ.IOrganService;
import com.jb4dc.sso.service.organ.IOrganTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/PlatFormRest/SSO/Organ")
public class OrganRest extends GeneralRest<OrganEntity> {

    @Autowired
    IOrganService organService;

    @Autowired
    IOrganTypeService organTypeService;

    @Autowired
    IFileInfoService fileInfoService;

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
    public JBuild4DCResponseVo uploadOrganLogo(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        FileInfoEntity fileInfoEntity=fileInfoService.addSmallFileToDB(JB4DCSessionUtility.getSession(),file);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.SUCCESSMSG,fileInfoEntity);
    }

    @RequestMapping(value = "/GetOrganLogo", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getOrganLogo(String fileId) throws IOException, JBuild4DCGenerallyException {
        FileInfoEntity fileInfoEntity=fileInfoService.getByPrimaryKey(JB4DCSessionUtility.getSession(),fileId);
        if(fileInfoEntity==null) {
            if (JB4DCacheManager.exist(JB4DCacheManager.jb4dPlatformBuilderCacheName, JB4DCacheManager.CACHE_KEY_ORGAN_LOGO)) {
                return JB4DCacheManager.getObject(JB4DCacheManager.jb4dPlatformBuilderCacheName, JB4DCacheManager.CACHE_KEY_ORGAN_LOGO);
            } else {
                InputStream is = this.getClass().getResourceAsStream("/static/Themes/Default/Css/Images/DefaultLogo.png");
                byte[] defaultImageByte = IOUtils.toByteArray(is);
                is.close();
                JB4DCacheManager.put(JB4DCacheManager.jb4dPlatformBuilderCacheName, JB4DCacheManager.CACHE_KEY_ORGAN_LOGO, defaultImageByte);
                return defaultImageByte;
            }
        }
        else{
            return fileInfoService.getContent(fileId);
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
*/
